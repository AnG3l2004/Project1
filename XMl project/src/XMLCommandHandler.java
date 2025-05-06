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
     *
     * @param id ID на елемента, който ще бъде модифициран
     * @param key ключът на атрибута
     * @param value новата стойност на атрибута
     *
     * Пример:
     * <pre>
     * handler.set("element1", "name", "Jane");
     * </pre>
     */
    public void set(String id, String key, String value) {
        XMLElement element = findElementById(currentRoot, id);
        if (element == null) {
            System.out.println("Element with id '" + id + "' not found.");
            return;
        }

        element.setAttribute(key, value);
        System.out.println("Attribute set successfully.");
    }
    /**
     * Извежда списък с всички дъщерни елементи на даден родителски елемент.
     *
     * @param id ID на родителския елемент
     *
     * Пример:
     * <pre>
     * handler.children("parent1");
     * // Резултат:
     * // child1 [id: auto_1] Атрибути: name="value"
     * // child2 [id: auto_2] Атрибути: type="text"
     * </pre>
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
     * Изтрива атрибут от даден елемент.
     * Забележка: Атрибутът 'id' не може да бъде изтрит.
     *
     * @param id ID на елемента
     * @param key ключът на атрибута за изтриване
     *
     * Пример:
     * <pre>
     * handler.delete("element1", "name");
     * </pre>
     */
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
    /**
     * Добавя нов дъщерен елемент към даден родител.
     * Новият елемент получава автоматично генерирано ID.
     *
     * @param id ID на родителския елемент
     *
     * Пример:
     * <pre>
     * handler.newchild("parent1");
     * // Резултат: Добавено е ново дете с id: auto_3
     * </pre>
     */
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

    /**
     * Запазва текущата XML структура във файл.
     * 
     * @param filename пътят до файла, в който да се запази XML структурата
     * @throws IOException ако файлът не може да бъде записан
     * 
     * Пример:
     * <pre>
     * handler.save("output.xml");
     * </pre>
     */
    public void save(String filename) throws IOException {
        if (currentRoot == null) {
            System.out.println("Няма отворен файл за запазване.");
            return;
        }

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            saveElement(writer, currentRoot, 0);
            System.out.println("Файлът е запазен успешно.");
        }
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

        // Запис на атрибути
        for (Map.Entry<String, String> attr : element.getAttributes().entrySet()) {
            writer.write(" " + attr.getKey() + "=\"" + attr.getValue() + "\"");
        }

        if (element.getTextContent() != null && !element.getTextContent().isEmpty()) {
            writer.write(">" + element.getTextContent() + "</" + element.getTag() + ">\n");
        } else if (element.getChildren().isEmpty()) {
            writer.write("/>\n");
        } else {
            writer.write(">\n");
            for (XMLElement child : element.getChildren()) {
                saveElement(writer, child, indent + 1);
            }
            writer.write(indentation + "</" + element.getTag() + ">\n");
        }
    }
}
