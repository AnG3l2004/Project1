package commands;

public class SetCommand implements XMLCommand {
    private XMLCommandHandler handler;
    private String id;
    private String key;
    private String value;

    public SetCommand(XMLCommandHandler handler, String id, String key, String value) {
        this.handler = handler;
        this.id = id;
        this.key = key;
        this.value = value;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void setParameters(String[] tokens) throws Exception {
        if (tokens.length >= 4) {
            setId(tokens[1]);
            setKey(tokens[2]);
            // Join all remaining tokens for the value
            StringBuilder valueBuilder = new StringBuilder();
            for (int i = 3; i < tokens.length; i++) {
                if (i > 3) valueBuilder.append(" ");
                valueBuilder.append(tokens[i]);
            }
            setValue(valueBuilder.toString());
        } else {
            throw new IllegalArgumentException("Invalid usage for set command.");
        }
    }

    @Override
    public void execute() throws Exception {
        handler.set(id, key, value);
    }
} 