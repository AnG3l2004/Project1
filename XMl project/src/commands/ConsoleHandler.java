package commands;

import java.util.Scanner;
import java.util.Map;
import java.util.EnumMap;

/**
 * Обработва конзолната интеракция за XML парсър приложението.
 * Този клас отговаря за:
 * - Четене на команди от конзолата
 * - Парсване на въведените команди
 * - Изпълнение на съответните операции
 * - Показване на помощна информация
 */
public class ConsoleHandler {
    private final Map<CommandType, XMLCommand> commandMap;
    private final Scanner scanner;
    
    /**
     * Създава нов конзолен обработчик с дадения команден обработчик
     * @param handler XML команден обработчик за използване при операциите
     */
    public ConsoleHandler(XMLCommandHandler handler) {
        this.scanner = new Scanner(System.in);
        this.commandMap = new EnumMap<>(CommandType.class);

        commandMap.put(CommandType.OPEN, new OpenCommand(handler, null));
        commandMap.put(CommandType.PRINT, new PrintCommand(handler));
        commandMap.put(CommandType.SELECT, new SelectCommand(handler, null, null));
        commandMap.put(CommandType.SET, new SetCommand(handler, null, null, null));
        commandMap.put(CommandType.CHILDREN, new ChildrenCommand(handler, null));
        commandMap.put(CommandType.CHILD, new ChildCommand(handler, null, 0));
        commandMap.put(CommandType.TEXT, new TextCommand(handler, null));
        commandMap.put(CommandType.DELETE, new DeleteCommand(handler, null, null));
        commandMap.put(CommandType.NEWCHILD, new NewChildCommand(handler, null));
        commandMap.put(CommandType.QUERY, new QueryCommand(handler, null));
        commandMap.put(CommandType.SAVE, new SaveCommand(handler));
        commandMap.put(CommandType.SAVEAS, new SaveAsCommand(handler, null));
    }
    
    /**
     * Стартира интерактивната конзола
     */
    public void start() {
        System.out.println("XML Parser Interactive Console. Type 'help' for commands. Type 'exit' to quit.");
        try {
            runConsole();
        } finally {
            scanner.close();
        }
    }
    
    /**
     * Изпълнява конзолния интерактивен цикъл
     */
    private void runConsole() {
        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if (line.equalsIgnoreCase("exit")) {
                System.out.println("Exiting.");
                break;
            }
            if (line.isEmpty()) continue;
            String[] tokens = line.split("\\s+");
            String commandStr = tokens[0].toLowerCase();
            if (commandStr.equals("help")) {
                if (tokens.length > 1) {
                    printCommandHelp(tokens[1].toLowerCase());
                } else {
                    printHelp();
                }
                continue;
            }
            try {
                CommandType commandType = CommandType.fromString(commandStr);
                if (commandType == null) {
                    System.out.println("Unknown command. Type 'help' for available commands.");
                    continue;
                }
                XMLCommand cmd = commandMap.get(commandType);
                if (cmd instanceof ParameterizedCommand) {
                    ((ParameterizedCommand) cmd).setParameters(tokens);
                }
                cmd.execute();
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Type 'help " + tokens[0].toLowerCase() + "' for usage information.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    /**
     * Отпечатва помощна информация за всички команди
     */
    private void printHelp() {
        System.out.println("\nAvailable commands:");
        for (XMLCommand cmd : commandMap.values()) {
            System.out.println(cmd.getHelp());
        }
        System.out.println("\nType 'help <command>' for detailed information about a specific command.");
    }
    
    /**
     * Отпечатва подробна помощна информация за конкретна команда
     * @param command името на командата
     */
    private void printCommandHelp(String command) {
        CommandType type = CommandType.fromString(command);
        if (type == null) {
            System.out.println("Unknown command: " + command);
            return;
        }

        switch (type) {
            case OPEN:
                System.out.println("\nDetails:");
                System.out.println("  The open command loads and displays an XML file.");
                System.out.println("  Usage: open <filename>");
                System.out.println("\nExample:");
                System.out.println("  open test.xml");
                break;
            case PRINT:
                System.out.println("\nDetails:");
                System.out.println("  The print command displays the current XML structure in a formatted way.");
                System.out.println("  Usage: print");
                break;
            case SELECT:
                System.out.println("\nDetails:");
                System.out.println("  The select command retrieves the value of an attribute for a given element by ID.");
                System.out.println("  Usage: select <id> <key>");
                System.out.println("  - <id>: The ID of the element");
                System.out.println("  - <key>: The attribute name");
                System.out.println("\nExample:");
                System.out.println("  select 1 title");
                break;
            case SET:
                System.out.println("\nDetails:");
                System.out.println("  The set command allows you to set attribute values, text content, or create child elements.");
                System.out.println("  Usage: set <id> <key> <value>");
                System.out.println("  - <id>: The ID of the element to modify");
                System.out.println("  - <key>: Attribute name, 'text' for text content, or '*tagname' for child elements");
                System.out.println("  - <value>: The value to set (can contain spaces)");
                System.out.println("\nExamples:");
                System.out.println("  set element1 title \"My Document Title\"     - Sets attribute title=\"My Document Title\"");
                System.out.println("  set element2 text \"This is content\"        - Sets text content");
                System.out.println("  set element3 *title \"Chapter One\"          - Creates child <title>Chapter One</title>");
                break;
            case CHILDREN:
                System.out.println("\nDetails:");
                System.out.println("  The children command lists all child elements of a given parent element by ID.");
                System.out.println("  Usage: children <id>");
                System.out.println("  - <id>: The ID of the parent element");
                System.out.println("\nExample:");
                System.out.println("  children 1");
                break;
            case CHILD:
                System.out.println("\nDetails:");
                System.out.println("  The child command allows you to access the nth child of an element by its index.");
                System.out.println("  Usage: child <id> <n>");
                System.out.println("  - <id>: The ID of the parent element");
                System.out.println("  - <n>: The zero-based index of the child element");
                System.out.println("\nExamples:");
                System.out.println("  child 1 0");
                System.out.println("  child 2 1");
                break;
            case TEXT:
                System.out.println("\nDetails:");
                System.out.println("  The text command prints the text content of a given element by ID.");
                System.out.println("  Usage: text <id>");
                System.out.println("  - <id>: The ID of the element");
                System.out.println("\nExample:");
                System.out.println("  text 1");
                break;
            case DELETE:
                System.out.println("\nDetails:");
                System.out.println("  The delete command removes attributes or child elements.");
                System.out.println("  Usage: delete <id> <key>");
                System.out.println("  - To delete an attribute: delete <id> <attribute_name>");
                System.out.println("  - To delete text content: delete <id> text");
                System.out.println("  - To delete child elements: delete <id> <child_tag_name>");
                break;
            case NEWCHILD:
                System.out.println("\nDetails:");
                System.out.println("  The newchild command adds a new child element to a given parent element by ID.");
                System.out.println("  Usage: newchild <id>");
                System.out.println("  - <id>: The ID of the parent element");
                System.out.println("\nExample:");
                System.out.println("  newchild 1");
                break;
            case QUERY:
                System.out.println("\nDetails:");
                System.out.println("  The query command allows you to query the XML structure using expressions.");
                System.out.println("  Usage: query <expression>");
                System.out.println("  - <expression>: The query expression (supports paths, attributes, filters, etc.)");
                System.out.println("\nExamples:");
                System.out.println("  query book/title");
                System.out.println("  query bk:book/bk:title");
                System.out.println("  query book(@id)");
                break;
            case SAVE:
                System.out.println("\nDetails:");
                System.out.println("  The save command saves the current XML structure to the last used file.");
                System.out.println("  Usage: save");
                break;
            case SAVEAS:
                System.out.println("\nDetails:");
                System.out.println("  The saveas command saves the current XML structure to a new file.");
                System.out.println("  Usage: saveas <filename>");
                System.out.println("  - <filename>: The name of the file to save to");
                System.out.println("\nExample:");
                System.out.println("  saveas output.xml");
                break;
            default:
                break;
        }
    }
} 