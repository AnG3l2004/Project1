import commands.XMLCommandHandler;
import commands.ConsoleHandler;

public class Main {
    public static void main(String[] args) {
        XMLCommandHandler handler = new XMLCommandHandler();
        ConsoleHandler console = new ConsoleHandler(handler);
        console.start();
    }
}