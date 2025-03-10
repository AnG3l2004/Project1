import java.util.Scanner;

public class Main {
    private static XMLParser parser = new XMLParser();
    private static Scanner scanner = new Scanner(System.in);

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

                case "help":
                    System.out.println("Commands:");
                    System.out.println("  open <filename> - Open XML file");
                    System.out.println("  save - Save changes");
                    System.out.println("  help - Show commands");
                    System.out.println("  exit - Exit program");
                    break;

                case "exit":
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Unknown command. Type 'help' for commands.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
} 