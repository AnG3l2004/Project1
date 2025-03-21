import java.util.Scanner;
import java.util.List;
import java.util.Map;

public class Main {
    private static XMLParser parser = new XMLParser();
    private static Scanner scanner = new Scanner(System.in);

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";

    public static void main(String[] args) {
        System.out.println("Simple XML Parser - Type 'help' for commands");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            handleCommand(input);
        }
    }

    private static void handleCommand(String input) {
        String[] parts = input.trim().split("\\s+");
        String command = parts[0].toLowerCase();

        try {
            switch (command) {
                case "open":
                    if (parts.length < 2) {
                        System.out.println("Usage: open <filename>");
                        return;
                    }
                    parser.open(parts[1]);
                    System.out.println("File opened");
                    break;

                case "save":
                    parser.save();
                    System.out.println("File saved");
                    break;

                case "select":
                    if (parts.length != 3) {
                        System.out.println("Usage: select <id> <key>");
                        return;
                    }
                    XMLElement element = parser.getRoot().findById(parts[1]);
                    if (element != null) {
                        String value = element.getAttribute(parts[2]);
                        if (value != null) {
                            System.out.println(value);
                        } else {
                            System.out.println("Attribute not found: " + parts[2]);
                        }
                    } else {
                        System.out.println("Element not found with id: " + parts[1]);
                    }
                    break;

                case "set":
                    if (parts.length != 4) {
                        System.out.println("Usage: set <id> <key> <value>");
                        return;
                    }
                    element = parser.getRoot().findById(parts[1]);
                    if (element != null) {
                        element.setAttribute(parts[2], parts[3]);
                        System.out.println("Attribute set successfully");
                    } else {
                        System.out.println("Element not found with id: " + parts[1]);
                    }
                    break;

                case "children":
                    if (parts.length != 2) {
                        System.out.println("Usage: children <id>");
                        return;
                    }
                    element = parser.getRoot().findById(parts[1]);
                    if (element != null) {
                        List<XMLElement> children = element.getChildren();
                        if (children.isEmpty()) {
                            System.out.println("No children");
                        } else {
                            for (XMLElement child : children) {
                                System.out.println("Child: " + child.getTag() + " (ID: " + child.getId() + ")");
                                for (Map.Entry<String, String> attr : child.getAttributes().entrySet()) {
                                    System.out.println("  " + attr.getKey() + "=\"" + attr.getValue() + "\"");
                                }
                            }
                        }
                    } else {
                        System.out.println("Element not found with id: " + parts[1]);
                    }
                    break;

                case "newchild":
                    if (parts.length != 2) {
                        System.out.println("Usage: newchild <id>");
                        return;
                    }
                    element = parser.getRoot().findById(parts[1]);
                    if (element != null) {
                        XMLElement newChild = new XMLElement("new_element");
                        element.addChild(newChild);
                        System.out.println("New child added with ID: " + newChild.getId());
                    } else {
                        System.out.println("Element not found with id: " + parts[1]);
                    }
                    break;

                case "help":
                    printHelp();
                    break;

                case "exit":
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;

                case "print":
                    if (parser.getRoot() != null) {
                        System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                        printElement(parser.getRoot(), 0);
                    } else {
                        System.out.println("No file is currently open");
                    }
                    break;

                default:
                    System.out.println("Unknown command. Type 'help' for commands.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void printHelp() {
        System.out.println(ANSI_CYAN + "╔════════════════════════════════════════╗");
        System.out.println("║           Available Commands            ║");
        System.out.println("╚════════════════════════════════════════╝" + ANSI_RESET);
        
        System.out.println(ANSI_GREEN + "\nFile Operations:" + ANSI_RESET);
        System.out.println("  open <filename>  - Open XML file");
        System.out.println("  save            - Save changes");
        System.out.println("  save as <file>  - Save to new file");
        
        System.out.println(ANSI_GREEN + "\nViewing Commands:" + ANSI_RESET);
        System.out.println("  print           - Show formatted XML structure");
        System.out.println("  list            - Show tree view of elements");
        
        System.out.println(ANSI_GREEN + "\nElement Operations:" + ANSI_RESET);
        System.out.println("  select <id> <key>           - Get attribute value");
        System.out.println("  set <id> <key> <value>      - Set attribute");
        System.out.println("  children <id>               - List child elements");
        System.out.println("  newchild <id>               - Add new child element");
        
        System.out.println(ANSI_YELLOW + "\nExample Usage:" + ANSI_RESET);
        System.out.println("  1. " + ANSI_CYAN + "open test.xml" + ANSI_RESET + "          - Open a file");
        System.out.println("  2. " + ANSI_CYAN + "list" + ANSI_RESET + "                   - View structure");
        System.out.println("  3. " + ANSI_CYAN + "select auto_1 name" + ANSI_RESET + "     - Get attribute");
        System.out.println("  4. " + ANSI_CYAN + "set auto_1 color blue" + ANSI_RESET + "  - Set attribute");
        
        System.out.println(ANSI_YELLOW + "\nTip:" + ANSI_RESET + " Use 'list' command to see all available IDs and attributes");
    }

    private static void printElement(XMLElement element, int indent) {
        // Print indentation
        String indentStr = "    ".repeat(indent);
        
        // Print tag with ID in color
        System.out.print(indentStr + ANSI_GREEN + "<" + element.getTag() + ANSI_RESET);
        System.out.print(ANSI_BLUE + " [ID: " + element.getId() + "]" + ANSI_RESET);
        
        // Print attributes in a different color
        for (Map.Entry<String, String> attr : element.getAttributes().entrySet()) {
            System.out.print(" " + ANSI_YELLOW + attr.getKey() + ANSI_RESET + "=\"" + 
                            ANSI_CYAN + attr.getValue() + ANSI_RESET + "\"");
        }

        List<XMLElement> children = element.getChildren();
        String text = element.getTextContent();

        if (children.isEmpty() && text == null) {
            System.out.println(ANSI_GREEN + "/>" + ANSI_RESET);
            return;
        }

        System.out.print(ANSI_GREEN + ">" + ANSI_RESET);

        if (text != null) {
            System.out.print(text);
            System.out.println(ANSI_GREEN + "</" + element.getTag() + ">" + ANSI_RESET);
        } else {
            System.out.println();
            for (XMLElement child : children) {
                printElement(child, indent + 1);
            }
            System.out.print(indentStr + ANSI_GREEN + "</" + element.getTag() + ">" + ANSI_RESET);
            System.out.println();
        }
    }

    private static void listElements(XMLElement element, String prefix) {
        // Element name and ID
        System.out.println(prefix + ANSI_GREEN + "└─ " + element.getTag() + ANSI_RESET + 
                          ANSI_BLUE + " [ID: " + element.getId() + "]" + ANSI_RESET);
        
        String childPrefix = prefix + "   ";
        
        // Attributes
        if (!element.getAttributes().isEmpty()) {
            System.out.println(childPrefix + ANSI_YELLOW + "Attributes:" + ANSI_RESET);
            for (Map.Entry<String, String> attr : element.getAttributes().entrySet()) {
                System.out.println(childPrefix + "├─ " + ANSI_CYAN + attr.getKey() + 
                                 ANSI_RESET + " = \"" + attr.getValue() + "\"");
            }
        }
        
        // Text content
        if (element.getTextContent() != null) {
            System.out.println(childPrefix + ANSI_YELLOW + "Text:" + ANSI_RESET + " \"" + 
                             element.getTextContent() + "\"");
        }
        
        // Children
        List<XMLElement> children = element.getChildren();
        if (!children.isEmpty()) {
            System.out.println(childPrefix + ANSI_YELLOW + "Children:" + ANSI_RESET);
            for (int i = 0; i < children.size(); i++) {
                boolean isLast = (i == children.size() - 1);
                listElements(children.get(i), childPrefix + (isLast ? "   " : "│  "));
            }
        }
    }
} 