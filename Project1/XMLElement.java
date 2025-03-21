import java.util.*;

public class XMLElement {
    private String id;
    private String tag;
    private Map<String, String> attributes;
    private List<XMLElement> children;
    private String textContent;
    private static int nextAutoId = 1;

    public XMLElement(String tag) {
        this.tag = tag;
        this.attributes = new HashMap<>();
        this.children = new ArrayList<>();
        generateId();
    }

    private void generateId() {
        // Use existing id attribute if present
        String existingId = attributes.get("id");
        if (existingId != null) {
            this.id = existingId;
        } else {
            // Generate new unique id
            this.id = "auto_" + nextAutoId++;
        }
    }

    public String getTag() { return tag; }
    public Map<String, String> getAttributes() { return attributes; }
    public String getTextContent() { return textContent; }
    public void setTextContent(String text) { this.textContent = text; }

    public void setAttribute(String key, String value) { attributes.put(key, value); }
    public String getAttribute(String key) { return attributes.get(key); }

    public void addChild(XMLElement child) { children.add(child); }
    public List<XMLElement> getChildren() { return children; }

    public String getId() { return id; }
    
    // Method to find element by ID (searches this element and all children)
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
} 