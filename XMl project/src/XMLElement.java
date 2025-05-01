import java.util.*;

public class XMLElement {
    private String id;
    private String tag;
    private String namespaceURI;
    private String prefix;
    private Map<String, String> attributes;
    private List<XMLElement> children;
    private String textContent;
    private XMLElement parent;
    private static int nextAutoId = 1;

    public XMLElement(String tag) {
        this.tag = tag;
        this.attributes = new HashMap<>();
        this.children = new ArrayList<>();
        generateId();
    }

    private void generateId() {
        String existingId = attributes.get("id");
        if (existingId != null) {
            this.id = existingId;
        } else {
            this.id = "auto_" + nextAutoId++;
        }
    }

    public String getId() { return id; }
    public String getTag() { return tag; }
    public String getNamespaceURI() { return namespaceURI; }
    public void setNamespaceURI(String namespaceURI) { this.namespaceURI = namespaceURI; }
    public String getPrefix() { return prefix; }
    public void setPrefix(String prefix) { this.prefix = prefix; }
    public String getTextContent() { return textContent; }
    public void setTextContent(String text) { this.textContent = text; }

    public void setAttribute(String key, String value) { attributes.put(key, value); }
    public String getAttribute(String key) { return attributes.get(key); }

    public void addChild(XMLElement child) { children.add(child); }
    public List<XMLElement> getChildren() { return children; }

    public Map<String, String> getAttributes() { return attributes; }

    public XMLElement findById(String targetId) {
        if (this.id.equals(targetId)) {
            return this;
        }
        for (XMLElement child : children) {
            XMLElement found = child.findById(targetId);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public XMLElement getParent() { return parent; }
    public void setParent(XMLElement parent) { this.parent = parent; }

    public List<XMLElement> getChildrenByTag(String tag) {
        List<XMLElement> result = new ArrayList<>();
        for (XMLElement child : children) {
            if (child.getTag().equals(tag)) {
                result.add(child);
            }
        }
        return result;
    }

    public List<XMLElement> getDescendantsByTag(String tag) {
        List<XMLElement> result = new ArrayList<>();
        for (XMLElement child : children) {
            if (child.getTag().equals(tag)) {
                result.add(child);
            }
            result.addAll(child.getDescendantsByTag(tag));
        }
        return result;
    }

    public List<XMLElement> getAncestors() {
        List<XMLElement> result = new ArrayList<>();
        XMLElement current = this.parent;
        while (current != null) {
            result.add(current);
            current = current.getParent();
        }
        return result;
    }
} 