package commands;

public class SaveCommand implements XMLCommand {
    private XMLCommandHandler handler;

    public SaveCommand(XMLCommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public void setParameters(String[] tokens) throws Exception {
        if (tokens.length != 1) {
            throw new IllegalArgumentException("Invalid usage for save command.");
        }
    }

    @Override
    public void execute() throws Exception {
        handler.save();
    }
} 