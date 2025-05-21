package commands;

import java.io.IOException;

public class NewChildCommand implements ParameterizedCommand {
    private XMLCommandHandler handler;
    private String id;
    private String tagName;

    public NewChildCommand(XMLCommandHandler handler, String id) {
        this.handler = handler;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public void setParameters(String[] tokens) throws Exception {
        if (tokens.length == 2) {
            setId(tokens[1]);
            setTagName(null);
        } else if (tokens.length == 3) {
            setId(tokens[1]);
            setTagName(tokens[2]);
        } else {
            throw new IllegalArgumentException("Invalid usage for newchild command.");
        }
    }

    @Override
    public void execute() throws Exception {
        if (tagName != null) {
            handler.newchild(id, tagName);
        } else {
            handler.newchild(id);
        }
    }
    
    @Override
    public String getHelp() {
        return "newchild <id> [tagname]    - Add new child element, optionally with specific tag name";
    }
} 