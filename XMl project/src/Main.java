import java.util.Scanner;
import java.io.IOException;
import commands.XMLCommand;
import commands.XMLCommandHandler;
import commands.OpenCommand;
import commands.PrintCommand;
import commands.SelectCommand;
import commands.SetCommand;
import commands.ChildrenCommand;
import commands.ChildCommand;
import commands.TextCommand;
import commands.DeleteCommand;
import commands.NewChildCommand;
import commands.QueryCommand;
import commands.SaveCommand;
import commands.SaveAsCommand;
import commands.CommandType;
import java.util.EnumMap;
import java.util.Map;

public class Main {
    private static XMLCommandHandler handler = new XMLCommandHandler();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("XML Parser Interactive Console. Type 'help' for commands. Type 'exit' to quit.");

        Map<CommandType, XMLCommand> commandMap = new EnumMap<>(CommandType.class);
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

        runConsole(scanner, commandMap);
    }

    private static void runConsole(Scanner scanner, Map<CommandType, XMLCommand> commandMap) {
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
                printHelp();
                continue;
            }
            try {
                CommandType commandType = CommandType.fromString(commandStr);
                if (commandType == null) {
                    System.out.println("Unknown command. Type 'help' for available commands.");
                    continue;
                }
                XMLCommand cmd = commandMap.get(commandType);
                cmd.setParameters(tokens);
                cmd.execute();
            } catch (Exception e) {
                System.out.println("Invalid usage. Type 'help' for available commands.");
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
        System.out.println("save                    - Save current XML structure to last used file");
        System.out.println("saveas <filename>        - Save current XML structure to a new file");
        System.out.println("help                     - Show this help message");
        System.out.println("exit                     - Exit the program");
    }
}