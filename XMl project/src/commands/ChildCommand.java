package commands;

public class ChildCommand implements XMLCommand {
    private XMLCommandHandler handler;
    private String id;
    private int n;

    public ChildCommand(XMLCommandHandler handler, String id, int n) {
        this.handler = handler;
        this.id = id;
        this.n = n;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setN(int n) {
        this.n = n;
    }

    @Override
    public void setParameters(String[] tokens) throws Exception {
        if (tokens.length == 3) {
            setId(tokens[1]);
            setN(Integer.parseInt(tokens[2]));
        } else {
            throw new IllegalArgumentException("Invalid usage for child command.");
        }
    }

    @Override
    public void execute() throws Exception {
        handler.child(id, n);
    }
} 