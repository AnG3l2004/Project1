import java.io.*;
import java.util.*;

public class XMLParser {
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