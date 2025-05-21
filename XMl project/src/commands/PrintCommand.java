package commands;

public class PrintCommand implements XMLCommand {
    private XMLCommandHandler handler;

    public PrintCommand(XMLCommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public void execute() {
        handler.print();
    }
    
    @Override
    public String getHelp() {
        return "print                    - Display the current XML structure";
    }
} 