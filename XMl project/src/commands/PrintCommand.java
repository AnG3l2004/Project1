package commands;

public class PrintCommand implements XMLCommand {
    private XMLCommandHandler handler;

    public PrintCommand(XMLCommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public void setParameters(String[] tokens) throws Exception {
        if (tokens.length != 1) {
            throw new IllegalArgumentException("Invalid usage for print command.");
        }
    }

    @Override
    public void execute() {
        handler.print();
    }
} 