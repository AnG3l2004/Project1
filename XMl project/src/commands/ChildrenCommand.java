package commands;

public class ChildrenCommand implements XMLCommand {
    private XMLCommandHandler handler;
    private String id;

    public ChildrenCommand(XMLCommandHandler handler, String id) {
        this.handler = handler;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setParameters(String[] tokens) throws Exception {
        if (tokens.length == 2) {
            setId(tokens[1]);
        } else {
            throw new IllegalArgumentException("Invalid usage for children command.");
        }
    }

    @Override
    public void execute() throws Exception {
        handler.children(id);
    }
} 