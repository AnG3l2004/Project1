package commands;

public class SaveAsCommand implements XMLCommand {
    private XMLCommandHandler handler;
    private String filename;

    public SaveAsCommand(XMLCommandHandler handler, String filename) {
        this.handler = handler;
        this.filename = filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public void setParameters(String[] tokens) throws Exception {
        if (tokens.length == 2) {
            setFilename(tokens[1]);
        } else {
            throw new IllegalArgumentException("Invalid usage for saveas command.");
        }
    }

    @Override
    public void execute() throws Exception {
        handler.saveAs(filename);
    }
} 