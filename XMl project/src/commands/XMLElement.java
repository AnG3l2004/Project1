package commands;

import java.util.*;
/**
 * Представлява XML елемент в структурата на документа.
 * Този клас предоставя функционалност за управление на XML елементи, включително:
 * - Атрибути на елемента
 * - Дъщерни елементи
 * - Текстово съдържание
 * - Информация за пространството от имена (namespace)
 * - Връзки между елементите (родител/дете)
 *
 * Пример за използване:
 * <pre>
 * XMLElement element = new XMLElement("person");
 * element.setAttribute("name", "John");
 * element.setTextContent("John Doe");
 * </pre>
 */
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
    /**
     * Създава нов XML елемент с подадено име на таг.
     * Автоматично генерира уникален идентификатор за елемента.
     *
     * @param tag името на XML елемента
     */
    public XMLElement(String tag) {
        this.tag = tag;
        this.attributes = new HashMap<>();
        this.children = new ArrayList<>();
        generateId();
        this.attributes.put("id", this.id);
    }
    /**
     * Генерира уникален идентификатор за елемента.
     * Ако вече има зададен ID чрез атрибут, се използва той.
     * В противен случай се генерира автоматичен нарастващ идентификатор.
     */
    private void generateId() {
        String existingId = attributes.get("id");
        if (existingId != null) {
            this.id = existingId;
        } else {
            this.id = "auto_" + nextAutoId++;
        }
    }
    /**
     * Връща ID-то на елемента.
     * @return идентификаторът на елемента
     */
    public String getId() { return id; }
    /**
     * Връща името на тага на елемента.
     * @return името на тага
     */
    public String getTag() { return tag; }
    /**
     * Връща URI адреса на пространството от имена на елемента.
     * @return URI на namespace-а
     */
    public String getNamespaceURI() { return namespaceURI; }
    /**
     * Задава URI на пространството от имена.
     * @param namespaceURI URI на namespace-а
     */

    public void setNamespaceURI(String namespaceURI) { this.namespaceURI = namespaceURI; }
    /**
     * Връща префикса на пространството от имена.
     * @return namespace префиксът
     */
    public String getPrefix() { return prefix; }
    /**
     * Задава префикс на пространството от имена.
     * @param prefix префиксът за задаване
     */
    public void setPrefix(String prefix) { this.prefix = prefix; }
    /**
     * Връща текстовото съдържание на елемента.
     * @return текстовото съдържание
     */
    public String getTextContent() { return textContent; }
    /**
     * Задава текстовото съдържание на елемента.
     * @param text съдържанието за задаване
     */
    public void setTextContent(String text) { this.textContent = text; }
    /**
     * Задава атрибут на елемента.
     * @param key името на атрибута
     * @param value стойността на атрибута
     */
    public void setAttribute(String key, String value) { attributes.put(key, value); }
    /**
     * Връща стойността на атрибут по неговото име.
     * @param key името на атрибута
     * @return стойността или null, ако не е намерен
     */
    public String getAttribute(String key) { return attributes.get(key); }
    /**
     * Добавя дъщерен елемент към този елемент.
     * @param child дъщерният елемент за добавяне
     */
    public void addChild(XMLElement child) { children.add(child); }
    /**
     * Връща списък с всички дъщерни елементи.
     * @return списък с дъщерни елементи
     */
    public List<XMLElement> getChildren() { return children; }
    /**
     * Връща всички атрибути на елемента.
     * @return map от имена към стойности на атрибути
     */
    public Map<String, String> getAttributes() { return attributes; }
    /**
     * Търси елемент по ID в поддървото, започващо от този елемент.
     * @param targetId ID-то, което се търси
     * @return намереният елемент или null, ако не е открит
     *
     * Пример:
     * <pre>
     * XMLElement found = root.findById("person1");
     * </pre>
     */
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
    /**
     * Връща родителския елемент.
     * @return родителят или null, ако е коренов елемент
     */
    public XMLElement getParent() { return parent; }
    /**
     * Задава родителския елемент.
     * @param parent родителят за задаване
     */
    public void setParent(XMLElement parent) { this.parent = parent; }
    /**
     * Връща всички директни дъщерни елементи с дадено име на таг.
     * @param tag името на тага
     * @return списък с отговарящи дъщерни елементи
     *
     * Пример:
     * <pre>
     * List&lt;XMLElement&gt; persons = element.getChildrenByTag("person");
     * </pre>
     */
    public List<XMLElement> getChildrenByTag(String tag) {
        List<XMLElement> result = new ArrayList<>();
        for (XMLElement child : children) {
            if (child.getTag().equals(tag)) {
                result.add(child);
            }
        }
        return result;
    }
    /**
     * Връща всички наследници с дадено име на таг.
     * Извършва рекурсивно търсене в поддървото.
     * @param tag името на тага
     * @return списък с отговарящи наследници
     *
     * Пример:
     * <pre>
     * List&lt;XMLElement&gt; allPersons = element.getDescendantsByTag("person");
     * </pre>
     */
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
    /**
     * Връща всички родителски елементи на текущия елемент.
     * @return списък с родителите, започвайки от непосредствения родител
     *
     * Пример:
     * <pre>
     * List&lt;XMLElement&gt; ancestors = element.getAncestors();
     * </pre>
     */
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