package commands;

import java.io.IOException;
import java.io.FileWriter;
import java.util.*;

/**
 * Обработчик на команди за работа с XML, предоставящ набор от методи за манипулация и заявяване на XML документи.
 * Този клас служи като основен интерфейс за работа с XML, позволявайки на потребителите да:
 * - Отварят и анализират XML файлове
 * - Отпечатват структурата на XML
 * - Избират и променят атрибути
 * - Навигират между XML елементи
 * - Извършват заявки към съдържанието на XML
 * - Запазват промените във файл
 *
 * Пример за употреба:
 * <pre>
 * XMLCommandHandler handler = new XMLCommandHandler();
 * handler.open("example.xml");
 * handler.print();
 * handler.select("element1", "attribute1");
 * handler.save("modified.xml");
 * </pre>
 */

public class XMLCommandHandler {
    private XMLParser parser;
    private XMLElement currentRoot;
    private String lastSavedFilename;

    /**
     * Създава нов XMLCommandHandler с подразбиращ се XMLParser.
     */
    public XMLCommandHandler() {
        parser = new XMLParser();
    }

    /**
     * Отваря и анализира XML файл.
     *
     * @param filename пътят до XML файла, който трябва да се отвори
     * @throws IOException ако файлът не може да бъде прочетен или анализиран
     *
     * Пример:
     * <pre>
     * handler.open("data.xml");
     * </pre>
     */
    public void open(String filename) throws IOException {
        currentRoot = parser.parseXML(filename);
        lastSavedFilename = filename;
        System.out.println("File opened successfully.");
    }
    /**
     * Отпечатва текущата структура на XML във форматиран вид.
     * Ако не е отворен файл, се показва подходящо съобщение.
     *
     * Пример:
     * <pre>
     * handler.print();
     * // Резултат:
     * // <root>
     * //   <child id="1">content</child>
     * // </root>
     * </pre>
     */
    public void print() {
        if (currentRoot == null) {
            System.out.println("No file is currently open.");
            return;
        }
        printElement(currentRoot, 0);
    }
    /**
     * Избира и отпечатва стойността на определен атрибут за даден елемент по ID.
     *
     * @param id ID на елемента, който търсим
     * @param key ключ на атрибута, чиято стойност искаме
     *
     * Пример:
     * <pre>
     * handler.select("element1", "name");
     * // Резултат: "John"
     * </pre>
     */
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
    /**
     * Задава стойност на атрибут за даден елемент.
     * Поддържа стойности на атрибути, които съдържат интервали.
     *
     * @param id ID на елемента, който ще бъде модифициран
     * @param key ключът на атрибута или 'text' за текстово съдържание
     * @param value новата стойност на атрибута (може да съдържа интервали)
     *
     * Пример:
     * <pre>
     * handler.set("element1", "name", "Jane Doe");  // Sets name attribute to "Jane Doe"
     * handler.set("element1", "text", "This is multiword content");  // Sets text content
     * </pre>
     */
    public void set(String id, String key, String value) {
        XMLElement element = findElementById(currentRoot, id);
        if (element == null) {
            System.out.println("Element with id '" + id + "' not found.");
            return;
        }
        
        if (key.equals("id")) {
            System.out.println("Cannot modify 'id' attribute.");
            return;
        }
        
        if (key.equals("text")) {
            element.setTextContent(value);
            System.out.println("Text content set successfully.");
            return;
        }
        
        if (key.startsWith("*")) {
            String tagName = key.substring(1); // Remove the *
            
            boolean childUpdated = false;
            for (XMLElement child : element.getChildren()) {
                if (child.getTag().equals(tagName)) {
                    child.setTextContent(value);
                    System.out.println("Updated existing child element <" + tagName + "> with new text content.");
                    childUpdated = true;
                    break;
                }
            }
            
            if (!childUpdated) {
                XMLElement newChild = new XMLElement(tagName);
                newChild.setTextContent(value);
                newChild.setParent(element);
                element.addChild(newChild);
                System.out.println("Created new child element <" + tagName + "> with text content.");
            }
            return;
        }
        
        boolean isChildElementTag = false;
        for (XMLElement child : element.getChildren()) {
            if (child.getTag().equals(key)) {
                isChildElementTag = true;
                child.setTextContent(value);
                System.out.println("Child element's text content updated successfully.");
                return;
            }
        }
        
        element.setAttribute(key, value);
        System.out.println("Attribute set successfully.");
    }
    /**
     * Показва списък с всички дъщерни елементи на даден родителски елемент,
     * с форматиране, което разграничава между елементите по подразбиране и специализираните тагове.
     *
     * @param id ID на родителския елемент
     */
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

        List<XMLElement> defaultChildren = new ArrayList<>();
        List<XMLElement> specialTagChildren = new ArrayList<>();
        
        for (XMLElement child : children) {
            if (child.getTag().equals("new_element")) {
                defaultChildren.add(child);
            } else {
                specialTagChildren.add(child);
            }
        }

        if (!defaultChildren.isEmpty()) {
            System.out.println("Default children:");
            for (XMLElement child : defaultChildren) {
                System.out.print("  " + child.getTag() + " [id: " + child.getAttribute("id") + "]");
                Map<String, String> attrs = child.getAttributes();
                if (attrs.size() > 1) { // More than just the id attribute
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
        
        if (!specialTagChildren.isEmpty()) {
            if (!defaultChildren.isEmpty()) {
                System.out.println();
            }
            System.out.println("Specialized tag children:");
            for (XMLElement child : specialTagChildren) {
                System.out.print("  <" + child.getTag() + "> [id: " + child.getAttribute("id") + "]");
                Map<String, String> attrs = child.getAttributes();
                if (attrs.size() > 1) {
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
    }
    /**
     * Извежда информация за конкретно дете на даден елемент по индекс.
     *
     * @param id ID на родителския елемент
     * @param n индекс на дъщерния елемент (започва от 0)
     *
     * Пример:
     * <pre>
     * handler.child("parent1", 0);
     * // Резултат: Child 0: child1 [id: auto_1]
     * </pre>
     */
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
    /**
     * Връща и отпечатва текстовото съдържание на конкретен елемент.
     *
     * @param id ID на елемента
     *
     * Пример:
     * <pre>
     * handler.text("element1");
     * // Резултат: "This is the text content"
     * </pre>
     */
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
    /**
     * Изтрива атрибут или дъщерен елемент от даден XML елемент.
     * 
     * @param id ID на елемента за модифициране
     * @param key Име на атрибута или таг на дъщерния елемент за изтриване
     */
    public void delete(String id, String key) {
        XMLElement element = findElementById(currentRoot, id);
        if (element == null) {
            System.out.println("Element with id '" + id + "' not found.");
            return;
        }

        if (key.equals("id")) {
            System.out.println("Cannot delete 'id' attribute as it is required for element identification.");
            return;
        }

        if (key.equals("text")) {
            deleteTextContent(element);
            return;
        }

        if (deleteAttribute(element, key)) {
            return;
        }

        deleteChildElements(element, key);
    }

    /**
     * Изтрива текстовото съдържание на елемент.
     */
    private void deleteTextContent(XMLElement element) {
        if (element.getTextContent() != null && !element.getTextContent().isEmpty()) {
            element.setTextContent("");
            System.out.println("Text content deleted successfully.");
        } else {
            System.out.println("No text content to delete.");
        }
    }

    /**
     * Опитва се да изтрие атрибут от елемент.
     * @return true ако атрибутът е намерен и изтрит, false в противен случай
     */
    private boolean deleteAttribute(XMLElement element, String key) {
        if (element.getAttributes().containsKey(key)) {
            element.getAttributes().remove(key);
            System.out.println("Attribute '" + key + "' deleted successfully.");
            return true;
        }
        return false;
    }

    /**
     * Опитва се да изтрие дъщерни елементи с посочения таг.
     */
    private void deleteChildElements(XMLElement element, String tag) {
        List<XMLElement> children = element.getChildren();
        boolean removed = false;
        Iterator<XMLElement> it = children.iterator();
        while (it.hasNext()) {
            XMLElement child = it.next();
            if (child.getTag().equals(tag)) {
                it.remove();
                removed = true;
            }
        }
        
        if (removed) {
            System.out.println("Child element(s) with tag '" + tag + "' deleted successfully.");
        } else {
            System.out.println("No attribute or child element with name '" + tag + "' found.");
        }
    }
    /**
     * Добавя нов дъщерен елемент към даден родител.
     * Новият елемент получава автоматично генерирано ID.
     *
     * @param id ID на родителския елемент
     * @param tagName Optional tag name for the new element (defaults to "new_element" if null)
     *
     * Пример:
     * <pre>
     * handler.newchild("parent1", "new_child");
     * // Резултат: Добавено е ново дете с id: auto_3
     * </pre>
     */
    public void newchild(String id, String tagName) {
        XMLElement parent = findElementById(currentRoot, id);
        if (parent == null) {
            System.out.println("Element with id '" + id + "' not found.");
            return;
        }

        String newTag = (tagName != null && !tagName.isEmpty()) ? tagName : "new_element";
        XMLElement newChild = new XMLElement(newTag);
        
        newChild.setParent(parent);
        parent.addChild(newChild);
        System.out.println("New child <" + newTag + "> added with id: " + newChild.getAttribute("id"));
    }

    /**
     * Legacy version of newchild that doesn't specify tag name
     */
    public void newchild(String id) {
        newchild(id, null);
    }
    /**
     * Изпълнява заявка в стил XPath върху XML структурата.
     * Поддържа три вида заявки:
     * 1. Заявки към атрибути: element(@attribute)
     * 2. Филтриращи заявки: element(key=value)
     * 3. Пътища с индекси: element1/element2[0]
     *
     * @param expression изразът на заявката, която ще се изпълни
     *
     * Примери:
     * <pre>
     * handler.query("person(@name)");
     * handler.query("person(name=\"John\")");
     * handler.query("root/child[0]");
     * </pre>
     */
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

    private List<XMLElement> findElementsByTag(XMLElement root, String tag) {
        List<XMLElement> result = new ArrayList<>();
        if (root.getTag().equals(tag)) {
            result.add(root);
        }
        for (XMLElement child : root.getChildren()) {
            result.addAll(findElementsByTag(child, tag));
        }
        return result;
    }

    private void handleAttributeQuery(String expression) {
        String[] parts = expression.split("\\(@");
        String tag = parts[0];
        String attr = parts[1].replace(")", "");
        
        List<XMLElement> elements = findElementsByTag(currentRoot, tag);
        if (elements.isEmpty()) {
            System.out.println("No elements found with tag: " + tag);
            return;
        }
        
        boolean foundAny = false;
        for (XMLElement el : elements) {
            String val = el.getAttribute(attr);
            if (val != null) {
                System.out.println(val);
                foundAny = true;
            }
        }
        
        if (!foundAny) {
            System.out.println("No attributes found with name: " + attr + " on elements with tag: " + tag);
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
        if (elements.isEmpty()) {
            System.out.println("No elements found with tag: " + beforeParen);
            return;
        }
        
        List<XMLElement> filtered = new ArrayList<>();
        for (XMLElement el : elements) {
            String attrValue = el.getAttribute(filterKey);
            if (filterValue.equals(attrValue)) {
                filtered.add(el);
                continue;
            }
            
            for (XMLElement child : el.getChildren()) {
                if (child.getTag().equals(filterKey) && 
                    filterValue.equals(child.getTextContent())) {
                    filtered.add(el);
                    break;
                }
            }
            
            if (filterValue.equals(el.getTextContent())) {
                filtered.add(el);
            }
        }
        
        if (filtered.isEmpty()) {
            System.out.println("No matching elements found.");
            return;
        }
        
        if (afterParen.startsWith("/")) afterParen = afterParen.substring(1);
        if (!afterParen.isEmpty()) {
            boolean foundAny = false;
            for (XMLElement el : filtered) {
                List<XMLElement> children = findElementsByTag(el, afterParen);
                for (XMLElement child : children) {
                    String text = child.getTextContent();
                    if (text != null && !text.isEmpty()) {
                        System.out.println(text);
                        foundAny = true;
                    }
                }
            }
            if (!foundAny) {
                System.out.println("No matching child elements found.");
            }
        } else {
            for (XMLElement el : filtered) {
                String text = el.getTextContent();
                if (text != null && !text.isEmpty()) {
                    System.out.println(text);
                } else {
                    System.out.println(el.getTag() + " (no text)");
                }
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
                String idxStr = part.substring(part.indexOf('[') + 1, part.indexOf(']'));
                try {
                    idx = Integer.parseInt(idxStr);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid index: " + idxStr);
                    return;
                }
            }
            for (XMLElement el : current) {
                List<XMLElement> children = findElementsByTag(el, tag);
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
        if (root == null || id == null) {
            return null;
        }

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
        String tagName = element.getTag();
        System.out.print(indentation + "<" + tagName);

        for (Map.Entry<String, String> attr : element.getAttributes().entrySet()) {
            System.out.print(" " + attr.getKey() + "=\"" + attr.getValue() + "\"");
        }
        System.out.print(">\n");

        if (element.getTextContent() != null && !element.getTextContent().isEmpty()) {
            System.out.print(indentation + "  " + element.getTextContent() + "\n");
        }
        for (XMLElement child : element.getChildren()) {
            printElement(child, indent + 1);
        }
        System.out.println(indentation + "</" + tagName + ">");
    }

    /**
     * Запазва текущата XML структура във файл (вътрешен метод).
     *
     * @param filename пътят до файла, в който да се запази XML структурата
     * @throws IOException ако файлът не може да бъде записан
     */
    private void saveToFile(String filename) throws IOException {
        if (currentRoot == null) {
            System.out.println("No file is currently open for saving.");
            return;
        }
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            saveElement(writer, currentRoot, 0);
            System.out.println("File saved successfully.");
        }
    }

    public void save() throws IOException {
        if (lastSavedFilename == null) {
            System.out.println("No previous filename. Use saveas <filename>.");
            return;
        }
        saveToFile(lastSavedFilename);
    }

    public void saveAs(String filename) throws IOException {
        saveToFile(filename);
        lastSavedFilename = filename;
    }

    /**
     * Рекурсивно запазва XML елемент и неговите дъщерни елементи.
     * 
     * @param writer FileWriter за запис
     * @param element елементът за запазване
     * @param indent ниво на отстъп
     * @throws IOException ако възникне грешка при запис
     */
    private void saveElement(FileWriter writer, XMLElement element, int indent) throws IOException {
        String indentation = "  ".repeat(indent);
        writer.write(indentation + "<" + element.getTag());

        for (Map.Entry<String, String> attr : element.getAttributes().entrySet()) {
            writer.write(" " + attr.getKey() + "=\"" + attr.getValue() + "\"");
        }

        if (element.getTextContent() != null && !element.getTextContent().isEmpty()) {
            writer.write(">" + element.getTextContent() + "</" + element.getTag() + ">\n");
        } else if (element.getChildren().isEmpty()) {
            writer.write("></"+element.getTag()+">\n");
        } else {
            writer.write(">\n");
            for (XMLElement child : element.getChildren()) {
                saveElement(writer, child, indent + 1);
            }
            writer.write(indentation + "</" + element.getTag() + ">\n");
        }
    }
}
