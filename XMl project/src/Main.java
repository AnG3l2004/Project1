import java.util.Scanner;
import java.io.IOException;

public class Main {
    private static XMLCommandHandler handler = new XMLCommandHandler();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("XML Parser Interactive Console. Type 'help' for commands. Type 'exit' to quit.");
        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if (line.equalsIgnoreCase("exit")) {
                System.out.println("Exiting.");
                break;
            }
            if (line.isEmpty()) continue;
            String[] tokens = line.split("\\s+");
            String command = tokens[0].toLowerCase();
            try {
                switch (command) {
                    case "open":
                        if (tokens.length != 2) {
                            System.out.println("Usage: open <filename>");
                            break;
                        }
                        handler.open(tokens[1]);
                        handler.print();
                        break;
                    case "print":
                        handler.print();
                        break;
                    case "select":
                        if (tokens.length != 3) {
                            System.out.println("Usage: select <id> <key>");
                            break;
                        }
                        handler.select(tokens[1], tokens[2]);
                        break;
                    case "set":
                        if (tokens.length != 4) {
                            System.out.println("Usage: set <id> <key> <value>");
                            break;
                        }
                        handler.set(tokens[1], tokens[2], tokens[3]);
                        break;
                    case "children":
                        if (tokens.length != 2) {
                            System.out.println("Usage: children <id>");
                            break;
                        }
                        handler.children(tokens[1]);
                        break;
                    case "child":
                        if (tokens.length != 3) {
                            System.out.println("Usage: child <id> <n>");
                            break;
                        }
                        handler.child(tokens[1], Integer.parseInt(tokens[2]));
                        break;
                    case "text":
                        if (tokens.length != 2) {
                            System.out.println("Usage: text <id>");
                            break;
                        }
                        handler.text(tokens[1]);
                        break;
                    case "delete":
                        if (tokens.length != 3) {
                            System.out.println("Usage: delete <id> <key>");
                            break;
                        }
                        handler.delete(tokens[1], tokens[2]);
                        break;
                    case "newchild":
                        if (tokens.length != 2) {
                            System.out.println("Usage: newchild <id>");
                            break;
                        }
                        handler.newchild(tokens[1]);
                        break;
                    case "query":
                        if (tokens.length != 2) {
                            System.out.println("Usage: query <expression>");
                            break;
                        }
                        handler.query(tokens[1]);
                        break;
                    case "save":
                        if (tokens.length != 2) {
                            System.out.println("Usage: save <filename>");
                            break;
                        }
                        try {
                            handler.save(tokens[1]);
                        } catch (IOException e) {
                            System.out.println("Error saving file: " + e.getMessage());
                        }
                        break;
                    case "help":
                        printHelp();
                        break;
                    default:
                        System.out.println("Unknown command. Type 'help' for available commands.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void printHelp() {
        System.out.println("\nXML Parser Commands:");
        System.out.println("open <filename>          - Open and display an XML file");
        System.out.println("print                    - Display the current XML structure");
        System.out.println("select <id> <key>        - Get attribute value");
        System.out.println("set <id> <key> <value>   - Set attribute value");
        System.out.println("children <id>            - List child elements");
        System.out.println("child <id> <n>           - Access nth child");
        System.out.println("text <id>                - Get element's text content");
        System.out.println("delete <id> <key>        - Delete attribute");
        System.out.println("newchild <id>            - Add new child element");
        System.out.println("query <expression>       - Query the XML structure");
        System.out.println("save <filename>          - Save current XML structure to file");
        System.out.println("help                     - Show this help message");
        System.out.println("exit                     - Exit the program");
    }
}