package commands;

import java.io.*;
import java.util.*;
/**
 * Парсър за XML документи, който конвертира XML текст в дървовидна структура от XMLElement обекти.
 * Този парсър поддържа:
 * - Основно парсване на XML структура
 * - Обработка на XML декларация
 * - Извличане на атрибути
 * - Парсване на вложени елементи
 * - Извличане на текстово съдържание
 *
 * Пример за употреба:
 * <pre>
 * XMLParser parser = new XMLParser();
 * XMLElement root = parser.parseXML("example.xml");
 * </pre>
 */
public class XMLParser {
    /**
     * Парсва XML файл и връща коренния елемент на полученото дърво.
     *
     * @param filename път до XML файла за парсване
     * @return коренен елемент на парснатото XML дърво
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

        // Remove XML declaration if present
        if (xml.contains("<?xml")) {
            xml = xml.substring(xml.indexOf("?>") + 2).trim();
        }

        // Verify balanced tags
        if (!hasBalancedTags(xml)) {
            throw new IOException("XML is malformed: tags are not properly balanced");
        }

        return parseElement(xml);
    }
    /**
     * Парсва XML елемент и неговите дъщерни елементи от даден низ.
     *
     * @param xml низът, съдържащ XML за парсване
     * @return парснат XMLElement
     * @throws IOException ако XML не е добре форматиран
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

        String tagNameFull = tagContent.split("\\s+")[0];

        XMLElement element = new XMLElement(tagNameFull);

        if (tagContent.contains("=")) {
            String attrPart = tagContent.substring(tagNameFull.length()).trim();
            String[] attrs = attrPart.split("\\s+");
            for (String attr : attrs) {
                if (attr.contains("=") && !attr.startsWith("xmlns")) {
                    String[] parts = attr.split("=");
                    String name = parts[0];
                    String value = parts[1].replace("\"", "");
                    element.setAttribute(name, value);
                }
            }
        }

        if (tagContent.endsWith("/")) {
            return element;
        }

        String closeTag = "</" + tagNameFull + ">";
        int closeStart = xml.lastIndexOf(closeTag);
        if (closeStart == -1) {
            throw new IOException("No closing tag found for: " + tagNameFull);
        }

        String content = xml.substring(endTag + 1, closeStart).trim();
        if (!content.isEmpty()) {
            if (content.contains("<")) {
                int currentPos = 0;
                while (currentPos < content.length()) {
                    int childStart = content.indexOf('<', currentPos);
                    if (childStart == -1) break;
                    int childEnd = findChildEnd(content, childStart);
                    if (childEnd == -1) {
                        throw new IOException("Malformed XML: unclosed child element");
                    }
                    String childXml = content.substring(childStart, childEnd + 1);
                    XMLElement child = parseElement(childXml);
                    child.setParent(element);
                    element.addChild(child);
                    currentPos = childEnd + 1;
                }
            } else {
                element.setTextContent(content);
            }
        }
        return element;
    }
    /**
     * Намира крайната позиция на дъщерен елемент, обработвайки правилно вложените елементи.
     * @param content XML съдържанието, в което се търси
     * @param startPos Началната позиция на дъщерния елемент
     * @return Крайната позиция на дъщерния елемент или -1, ако не е намерен
     */
    private int findChildEnd(String content, int startPos) {
        int depth = 0;
        int pos = startPos;
        
        while (pos < content.length()) {
            char c = content.charAt(pos);
            if (c == '<') {
                if (pos + 1 < content.length() && content.charAt(pos + 1) == '/') {
                    depth--;
                    if (depth == 0) {
                        // Find the closing '>'
                        int endPos = content.indexOf('>', pos);
                        return endPos;
                    }
                } else {
                    depth++;
                }
            }
            pos++;
        }
        
        return -1;
    }
    /**
     * Проверява дали всички отварящи тагове имат съответстващи затварящи тагове
     * @param xml XML низът за проверка
     * @return true ако таговете са балансирани, false в противен случай
     */
    private boolean hasBalancedTags(String xml) {
        Stack<String> tagStack = new Stack<>();
        int i = 0;
        while (i < xml.length()) {
            int openBracket = xml.indexOf('<', i);
            if (openBracket == -1) break;
            
            if (xml.startsWith("<!--", openBracket)) {
                int endComment = xml.indexOf("-->", openBracket);
                if (endComment == -1) return false; // Unclosed comment
                i = endComment + 3;
                continue;
            }
            
            boolean isClosingTag = xml.charAt(openBracket + 1) == '/';
            
            int tagNameEnd = xml.indexOf('>', openBracket);
            if (tagNameEnd == -1) return false;
            
            boolean isSelfClosing = xml.charAt(tagNameEnd - 1) == '/';
            
            if (isClosingTag) {
                String tagName = xml.substring(openBracket + 2, tagNameEnd).trim();
                
                if (tagStack.isEmpty() || !tagStack.peek().equals(tagName)) {
                    return false;
                }
                
                tagStack.pop();
            } else if (!isSelfClosing) {
                String tagWithAttributes = xml.substring(openBracket + 1, tagNameEnd);
                String tagName = tagWithAttributes.split("\\s+")[0].trim();
                
                tagStack.push(tagName);
            }
            
            i = tagNameEnd + 1;
        }
        
        return tagStack.isEmpty();
    }
}