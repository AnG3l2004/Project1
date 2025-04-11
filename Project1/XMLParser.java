import java.io.*;
import java.util.*;

public class XMLParser {
    private XMLElement root;
    private String currentFile;
    private Map<String, String> namespaceMap = new HashMap<>();

    public void open(String filename) throws IOException {
        currentFile = filename;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            StringBuilder xmlContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                xmlContent.append(line).append("\n");
            }
            root = parseXML(xmlContent.toString());
        }
    }

    private XMLElement parseXML(String xml) throws IOException {
        int start = 0;
        while (start < xml.length()) {
            if (xml.startsWith("<?xml", start)) {
                start = xml.indexOf("?>", start) + 2;
            } else if (xml.startsWith("<!--", start)) {
                start = xml.indexOf("-->", start) + 3;
            } else {
                break;
            }
        }

        Stack<XMLElement> elementStack = new Stack<>();
        StringBuilder textContent = new StringBuilder();
        int i = start;

        while (i < xml.length()) {
            char c = xml.charAt(i);

            if (c == '<') {
                if (textContent.length() > 0 && !elementStack.isEmpty()) {
                    String text = decodeXMLEntities(textContent.toString().trim());
                    if (!text.isEmpty()) {
                        elementStack.peek().setTextContent(text);
                    }
                    textContent.setLength(0);
                }

                if (xml.startsWith("<!--", i)) {
                    i = xml.indexOf("-->", i) + 3;
                    continue;
                }

                if (xml.startsWith("<![CDATA[", i)) {
                    int cdataEnd = xml.indexOf("]]>", i);
                    if (cdataEnd == -1) throw new IOException("Unclosed CDATA section");
                    if (!elementStack.isEmpty()) {
                        String cdata = xml.substring(i + 9, cdataEnd);
                        elementStack.peek().setTextContent(cdata);
                    }
                    i = cdataEnd + 3;
                    continue;
                }

                if (xml.charAt(i + 1) == '/') {
                    int endTag = xml.indexOf('>', i);
                    if (endTag == -1) throw new IOException("Invalid XML: unclosed tag");
                    String tagName = xml.substring(i + 2, endTag).trim();
                    
                    if (elementStack.isEmpty()) {
                        throw new IOException("Invalid XML: unexpected closing tag " + tagName);
                    }
                    
                    XMLElement current = elementStack.pop();
                    if (!current.getTag().equals(tagName)) {
                        throw new IOException("Invalid XML: mismatched tags, expected " + current.getTag() + " but found " + tagName);
                    }
                    
                    if (elementStack.isEmpty()) {
                        root = current;
                    }
                    
                    i = endTag + 1;
                } else {
                    int endTag = xml.indexOf('>', i);
                    if (endTag == -1) throw new IOException("Invalid XML: unclosed tag");
                    
                    boolean selfClosing = xml.charAt(endTag - 1) == '/';
                    int tagEnd = selfClosing ? endTag - 1 : endTag;
                    
                    String tagContent = xml.substring(i + 1, tagEnd);
                    String[] parts = tagContent.split("\\s+", 2);
                    String tagName = parts[0];
                    
                    XMLElement element = new XMLElement(tagName);
                    
                    if (parts.length > 1) {
                        parseAttributes(element, parts[1]);
                    }
                    
                    if (!elementStack.isEmpty()) {
                        elementStack.peek().addChild(element);
                        element.setParent(elementStack.peek());
                    }
                    
                    if (!selfClosing) {
                        elementStack.push(element);
                    }
                    
                    i = endTag + 1;
                }
            } else {
                textContent.append(c);
                i++;
            }
        }

        if (!elementStack.isEmpty()) {
            throw new IOException("Invalid XML: unclosed tags remaining");
        }

        return root;
    }

    private void parseAttributes(XMLElement element, String attributeString) throws IOException {
        int i = 0;
        while (i < attributeString.length()) {
            while (i < attributeString.length() && Character.isWhitespace(attributeString.charAt(i))) {
                i++;
            }
            if (i >= attributeString.length()) break;

            int equals = attributeString.indexOf('=', i);
            if (equals == -1) break;

            String name = attributeString.substring(i, equals).trim();
            i = equals + 1;

            while (i < attributeString.length() && Character.isWhitespace(attributeString.charAt(i))) {
                i++;
            }
            if (i >= attributeString.length()) break;

            char quote = attributeString.charAt(i);
            if (quote != '"' && quote != '\'') {
                throw new IOException("Invalid XML: attribute value must be quoted");
            }
            i++;

            int closeQuote = attributeString.indexOf(quote, i);
            if (closeQuote == -1) {
                throw new IOException("Invalid XML: unclosed attribute value");
            }

            String value = decodeXMLEntities(attributeString.substring(i, closeQuote));
            if (name.startsWith("xmlns")) {
                if (name.length() > 5 && name.charAt(5) == ':') {
                    String prefix = name.substring(6);
                    namespaceMap.put(prefix, value);
                    if (element.getPrefix() == null) {
                        element.setPrefix(prefix);
                        element.setNamespaceURI(value);
                    }
                } else {
                    namespaceMap.put("", value);
                    element.setNamespaceURI(value);
                }
            } else {
                element.setAttribute(name, value);
            }
            i = closeQuote + 1;
        }
    }

    private String decodeXMLEntities(String text) {
        return text.replace("&lt;", "<")
                  .replace("&gt;", ">")
                  .replace("&amp;", "&")
                  .replace("&quot;", "\"")
                  .replace("&apos;", "'");
    }

    public void save() throws IOException {
        if (currentFile == null) throw new IOException("No file is open");
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(currentFile), "UTF-8"))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            if (root != null) {
                writeElement(writer, root, 0);
            }
        }
    }

    private void writeElement(PrintWriter writer, XMLElement element, int indent) {
        writeIndent(writer, indent);
        
        writer.print("<" + element.getTag());
        
        for (Map.Entry<String, String> attr : element.getAttributes().entrySet()) {
            writer.print(" " + attr.getKey() + "=\"" + encodeXMLEntities(attr.getValue()) + "\"");
        }

        List<XMLElement> children = element.getChildren();
        String text = element.getTextContent();

        if (children.isEmpty() && text == null) {
            writer.println("/>");
            return;
        }

        writer.print(">");

        if (text != null) {
            writer.print(encodeXMLEntities(text));
        } else {
            writer.println();
            for (XMLElement child : children) {
                writeElement(writer, child, indent + 1);
            }
            writeIndent(writer, indent);
        }

        writer.println("</" + element.getTag() + ">");
    }

    private void writeIndent(PrintWriter writer, int indent) {
        for (int i = 0; i < indent; i++) {
            writer.print("    ");
        }
    }

    private String encodeXMLEntities(String text) {
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&apos;");
    }

    public XMLElement getRoot() {
        return root;
    }
} 
