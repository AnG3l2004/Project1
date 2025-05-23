package commands;

public class TextCommand implements ParameterizedCommand {
    private XMLCommandHandler handler;
    private String id;

    public TextCommand(XMLCommandHandler handler, String id) {
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
            throw new IllegalArgumentException("Invalid usage for text command.");
        }
    }

    @Override
    public void execute() throws Exception {
        handler.text(id);
    }
    
    @Override
    public String getHelp() {
        return "text <id>                - Get element's text content";
    }
} 