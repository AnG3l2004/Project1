import java.io.IOException;
import java.util.*;

public class XMLCommandHandler {
    private XMLParser parser;
    private XMLElement currentRoot;

    public XMLCommandHandler() {
        parser = new XMLParser();
    }

    public void open(String filename) throws IOException {
        currentRoot = parser.parseXML(filename);
        System.out.println("File opened successfully.");
    }

    public void print() {
        if (currentRoot == null) {
            System.out.println("No file is currently open.");
            return;
        }
        printElement(currentRoot, 0);
    }

    public void select(String id, String key) {
        XMLElement element = findElementById(currentRoot, id);
        if (element == null) {
            System.out.println("Element with id '" + id + "' not found.");
            return;
        }

        String value = element.getAttribute(key);
        if (value == null) {
            System.out.println("Attribute '" + key + "' not found.");
            return;
        }

        System.out.println(value);
    }

    public void set(String id, String key, String value) {
        XMLElement element = findElementById(currentRoot, id);
        if (element == null) {
            System.out.println("Element with id '" + id + "' not found.");
            return;
        }

        element.setAttribute(key, value);
        System.out.println("Attribute set successfully.");
    }

    public void children(String id) {
        XMLElement element = findElementById(currentRoot, id);
        if (element == null) {
            System.out.println("Element with id '" + id + "' not found.");
            return;
        }

        List<XMLElement> children = element.getChildren();
        if (children.isEmpty()) {
            System.out.println("No children found.");
            return;
        }

        for (XMLElement child : children) {
            System.out.print(child.getTag() + " [id: " + child.getAttribute("id") + "]");
            Map<String, String> attrs = child.getAttributes();
            if (!attrs.isEmpty()) {
                System.out.print(" Attributes: ");
                for (Map.Entry<String, String> attr : attrs.entrySet()) {
                    if (!attr.getKey().equals("id")) {
                        System.out.print(attr.getKey() + "=\"" + attr.getValue() + "\" ");
                    }
                }
            }
            System.out.println();
        }
    }

    public void child(String id, int n) {
        XMLElement element = findElementById(currentRoot, id);
        if (element == null) {
            System.out.println("Element with id '" + id + "' not found.");
            return;
        }

        List<XMLElement> children = element.getChildren();
        if (n < 0 || n >= children.size()) {
            System.out.println("Child index out of range.");
            return;
        }

        XMLElement child = children.get(n);
        System.out.println("Child " + n + ": " + child.getTag() + " [id: " + child.getAttribute("id") + "]");
    }

    public void text(String id) {
        XMLElement element = findElementById(currentRoot, id);
        if (element == null) {
            System.out.println("Element with id '" + id + "' not found.");
            return;
        }

        String text = element.getTextContent();
        if (text == null || text.isEmpty()) {
            System.out.println("No text content.");
            return;
        }

        System.out.println(text);
    }

    public void delete(String id, String key) {
        XMLElement element = findElementById(currentRoot, id);
        if (element == null) {
            System.out.println("Element with id '" + id + "' not found.");
            return;
        }

        if (key.equals("id")) {
            System.out.println("Cannot delete 'id' attribute.");
            return;
        }

        element.getAttributes().remove(key);
        System.out.println("Attribute deleted successfully.");
    }

    public void newchild(String id) {
        XMLElement element = findElementById(currentRoot, id);
        if (element == null) {
            System.out.println("Element with id '" + id + "' not found.");
            return;
        }

        XMLElement newChild = new XMLElement("new_element");
        element.addChild(newChild);
        System.out.println("New child added with id: " + newChild.getAttribute("id"));
    }

    public void query(String expression) {
        if (currentRoot == null) {
            System.out.println("No file is currently open.");
            return;
        }
        expression = expression.replace(" ", "");
        if (expression.contains("(@")) {
            handleAttributeQuery(expression);
        } else if (expression.contains("(") && expression.contains("=") && expression.contains(")")) {
            handleFilterQuery(expression);
        } else {
            handlePathAndIndexQuery(expression);
        }
    }

    private void handleAttributeQuery(String expression) {
        String[] parts = expression.split("\\(@");
        String tag = parts[0];
        String attr = parts[1].replace(")", "");
        List<XMLElement> elements = findElementsByTag(currentRoot, tag);
        for (XMLElement el : elements) {
            String val = el.getAttribute(attr);
            if (val != null) System.out.println(val);
        }
    }

    private void handleFilterQuery(String expression) {
        String beforeParen = expression.substring(0, expression.indexOf('('));
        String insideParen = expression.substring(expression.indexOf('(') + 1, expression.indexOf(')'));
        String afterParen = expression.substring(expression.indexOf(')') + 1);
        String[] filterParts = insideParen.split("=");
        String filterKey = filterParts[0];
        String filterValue = filterParts[1].replaceAll("\"", "");
        List<XMLElement> elements = findElementsByTag(currentRoot, beforeParen);
        List<XMLElement> filtered = new ArrayList<>();
        for (XMLElement el : elements) {
            if (filterValue.equals(el.getAttribute(filterKey)) || filterValue.equals(el.getTextContent())) {
                filtered.add(el);
            }
        }
        if (afterParen.startsWith("/")) afterParen = afterParen.substring(1);
        if (!afterParen.isEmpty()) {
            for (XMLElement el : filtered) {
                List<XMLElement> children = findElementsByTag(el, afterParen);
                for (XMLElement child : children) {
                    System.out.println(child.getTextContent());
                }
            }
        } else {
            for (XMLElement el : filtered) {
                System.out.println(el.getTextContent());
            }
        }
    }

    private void handlePathAndIndexQuery(String expression) {
        String[] pathParts = expression.split("/");
        List<XMLElement> current = new ArrayList<>();
        current.add(currentRoot);
        for (String part : pathParts) {
            List<XMLElement> next = new ArrayList<>();
            String tag = part;
            int idx = -1;
            if (part.contains("[")) {
                tag = part.substring(0, part.indexOf('['));
                String idxStr = part.substring(part.indexOf('[') + 1, part.indexOf(']'));
                try {
                    idx = Integer.parseInt(idxStr);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid index: " + idxStr);
                    return;
                }
            }
            for (XMLElement el : current) {
                List<XMLElement> children = el.getChildrenByTag(tag);
                if (idx >= 0) {
                    if (idx < children.size()) {
                        next.add(children.get(idx));
                    }
                } else {
                    next.addAll(children);
                }
            }
            current = next;
        }
        for (XMLElement el : current) {
            String text = el.getTextContent();
            if (text != null && !text.isEmpty()) {
                System.out.println(text);
            } else {
                System.out.println(el.getTag() + " (no text)");
            }
        }
    }

    private XMLElement findElementById(XMLElement root, String id) {
        if (root == null) return null;

        if (id.equals(root.getAttribute("id"))) {
            return root;
        }

        for (XMLElement child : root.getChildren()) {
            XMLElement found = findElementById(child, id);
            if (found != null) {
                return found;
            }
        }

        return null;
    }

    private void printElement(XMLElement element, int indent) {
        String indentation = "  ".repeat(indent);
        System.out.print(indentation + "<" + element.getTag());

        for (Map.Entry<String, String> attr : element.getAttributes().entrySet()) {
            System.out.print(" " + attr.getKey() + "=\"" + attr.getValue() + "\"");
        }
        System.out.print(">");

        if (element.getTextContent() != null) {
            System.out.print(element.getTextContent());
            System.out.println("</" + element.getTag() + ">");
        } else {
            System.out.println();
            for (XMLElement child : element.getChildren()) {
                printElement(child, indent + 1);
            }
            System.out.println(indentation + "</" + element.getTag() + ">");
        }
    }

    private List<XMLElement> findElementsByTag(XMLElement root, String tag) {
        List<XMLElement> result = new ArrayList<>();
        if (tag.equals(root.getTag())) {
            result.add(root);
        }
        for (XMLElement child : root.getChildren()) {
            result.addAll(findElementsByTag(child, tag));
        }
        return result;
    }
}
