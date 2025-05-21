package commands;

public class SetCommand implements ParameterizedCommand {
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
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Value cannot be empty");
        }
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
            String value = valueBuilder.toString().trim();
            if (value.isEmpty()) {
                throw new IllegalArgumentException("Value cannot be empty");
            }
            setValue(value);
        } else {
            throw new IllegalArgumentException("Invalid usage for set command. Expected format: set <id> <key> <value>");
        }
    }

    @Override
    public void execute() throws Exception {
        handler.set(id, key, value);
    }
    
    @Override
    public String getHelp() {
        return "set <id> <key> <value>   - Set attribute value (value can contain spaces)\n" +
               "                          - Use 'text' as key to set text content\n" +
               "                          - Use '*tagname' to create/update a child element";
    }
} 