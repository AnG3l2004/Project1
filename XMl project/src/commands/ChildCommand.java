package commands;

public class ChildCommand implements ParameterizedCommand {
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
        if (n < 0) {
            throw new IllegalArgumentException("Child index must be non-negative");
        }
        this.n = n;
    }

    @Override
    public void setParameters(String[] tokens) throws Exception {
        if (tokens.length == 3) {
            setId(tokens[1]);
            try {
                setN(Integer.parseInt(tokens[2]));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Child index must be a valid number");
            }
        } else {
            throw new IllegalArgumentException("Invalid usage for child command.");
        }
    }

    @Override
    public void execute() throws Exception {
        handler.child(id, n);
    }
    
    @Override
    public String getHelp() {
        return "child <id> <n>           - Access nth child";
    }
} 