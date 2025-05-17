package commands;

public class QueryCommand implements XMLCommand {
    private XMLCommandHandler handler;
    private String expression;

    public QueryCommand(XMLCommandHandler handler, String expression) {
        this.handler = handler;
        this.expression = expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public void setParameters(String[] tokens) throws Exception {
        if (tokens.length == 2) {
            setExpression(tokens[1]);
        } else {
            throw new IllegalArgumentException("Invalid usage for query command.");
        }
    }

    @Override
    public void execute() throws Exception {
        handler.query(expression);
    }
} 