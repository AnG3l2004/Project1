package commands;

public class SelectCommand implements ParameterizedCommand {
    private XMLCommandHandler handler;
    private String id;
    private String key;

    public SelectCommand(XMLCommandHandler handler, String id, String key) {
        this.handler = handler;
        this.id = id;
        this.key = key;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public void setParameters(String[] tokens) throws Exception {
        if (tokens.length == 3) {
            setId(tokens[1]);
            setKey(tokens[2]);
        } else {
            throw new IllegalArgumentException("Invalid usage for select command.");
        }
    }

    @Override
    public void execute() throws Exception {
        handler.select(id, key);
    }
    
    @Override
    public String getHelp() {
        return "select <id> <key>        - Get attribute value";
    }
} 