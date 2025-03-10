import java.util.*;

public class XMLElement {
    private String tag;
    private Map<String, String> attributes;
    private List<XMLElement> children;
    private String textContent;

    public XMLElement(String tag) {
        this.tag = tag;
        this.attributes = new HashMap<>();
        this.children = new ArrayList<>();
    }

    public String getTag() { return tag; }
    public String getTextContent() { return textContent; }
    public void setTextContent(String text) { this.textContent = text; }

    public void setAttribute(String key, String value) { attributes.put(key, value); }
    public String getAttribute(String key) { return attributes.get(key); }

    public void addChild(XMLElement child) { children.add(child); }
    public List<XMLElement> getChildren() { return children; }
} 