package commands;

import java.io.IOException;

public class DeleteCommand implements XMLCommand {
    private XMLCommandHandler handler;
    private String id;
    private String key;

    public DeleteCommand(XMLCommandHandler handler, String id, String key) {
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
            throw new IllegalArgumentException("Invalid usage for delete command.");
        }
    }

    @Override
    public void execute() throws Exception {
        handler.delete(id, key);
    }
} 