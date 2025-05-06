import java.io.*;
import java.util.*;
/**
 * Парсер за XML документи, който преобразува XML текст в дървовидна структура от обекти XMLElement.
 * Този парсер поддържа:
 * - Основно парсване на XML структура
 * - Обработка на XML декларации
 * - Извличане на атрибути
 * - Парсване на вложени елементи
 * - Извличане на текстово съдържание
 *
 * Пример за използване:
 * <pre>
 * XMLParser parser = new XMLParser();
 * XMLElement root = parser.parseXML("example.xml");
 * </pre>
 */
public class XMLParser {
    /**
     * Парсира XML файл и връща кореновия елемент от полученото дърво.
     *
     * @param filename път до XML файла за парсване
     * @return кореновият XMLElement от парсираното XML дърво
     * @throws IOException ако файлът не може да бъде прочетен или съдържа невалиден XML
     *
     * Пример:
     * <pre>
     * XMLElement root = parser.parseXML("data.xml");
     * // root вече съдържа цялата XML структура
     * </pre>
     */
    public XMLElement parseXML(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        String xml = content.toString();

        if (xml.contains("<?xml")) {
            xml = xml.substring(xml.indexOf("?>") + 2).trim();
        }

        return parseElement(xml);
    }
    /**
     * Парсира един XML елемент и неговите дъщерни елементи от подаден низ.
     *
     * @param xml низът, съдържащ XML за парсване
     * @return парсираният XMLElement
     * @throws IOException ако XML-ът е некоректен
     *
     * Пример:
     * <pre>
     * XMLElement element = parser.parseElement("&lt;person name=\"John\"&gt;John Doe&lt;/person&gt;");
     * </pre>
     */
    private XMLElement parseElement(String xml) throws IOException {
        int startTag = xml.indexOf('<') + 1;
        int endTag = xml.indexOf('>');
        String tagContent = xml.substring(startTag, endTag);

        String tagName = tagContent.split("\\s+")[0];
        XMLElement element = new XMLElement(tagName);

        if (tagContent.contains("=")) {
            String attrPart = tagContent.substring(tagName.length()).trim();
            String[] attrs = attrPart.split("\\s+");
            for (String attr : attrs) {
                if (attr.contains("=")) {
                    String[] parts = attr.split("=");
                    String name = parts[0];
                    String value = parts[1].replace("\"", "");
                    element.setAttribute(name, value);
                }
            }
        }

        String closeTag = "</" + tagName + ">";
        int closeStart = xml.lastIndexOf(closeTag);

        if (closeStart == -1) {
            throw new IOException("No closing tag found for: " + tagName);
        }

        String content = xml.substring(endTag + 1, closeStart).trim();

        if (content.contains("<")) {
            int pos = 0;
            while (pos < content.length()) {
                int childStart = content.indexOf('<', pos);
                if (childStart == -1) break;

                if (content.charAt(childStart + 1) == '/') {
                    pos = content.indexOf('>', childStart) + 1;
                    continue;
                }

                int childEnd = findChildEnd(content.substring(childStart));
                if (childEnd == -1) break;

                String childXml = content.substring(childStart, childStart + childEnd);
                XMLElement child = parseElement(childXml);
                element.addChild(child);

                pos = childStart + childEnd;
            }
        } else if (!content.isEmpty()) {
            element.setTextContent(content);
        }

        return element;
    }
    /**
     * Намира крайното място на дъщерен елемент в XML низ.
     * Този метод правилно обработва вложени елементи.
     *
     * @param xml низът с дъщерен XML елемент
     * @return позицията, където дъщерният елемент завършва, или -1 ако не е намерен
     *
     * Пример:
     * <pre>
     * int end = findChildEnd("&lt;child&gt;content&lt;/child&gt;");
     * // end ще бъде позицията след затварящия таг
     * </pre>
     */
    private int findChildEnd(String xml) {
        int tagEnd = xml.indexOf('>');
        if (tagEnd == -1) return -1;

        String tagName = xml.substring(1, tagEnd).split("\\s+")[0];
        String closeTag = "</" + tagName + ">";
        int closePos = xml.indexOf(closeTag);
        if (closePos == -1) return -1;

        return closePos + closeTag.length();
    }
}