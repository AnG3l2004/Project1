package commands;

public enum CommandType {
    OPEN("open"),
    PRINT("print"),
    SELECT("select"),
    SET("set"),
    CHILDREN("children"),
    CHILD("child"),
    TEXT("text"),
    DELETE("delete"),
    NEWCHILD("newchild"),
    QUERY("query"),
    SAVE("save"),
    SAVEAS("saveas");

    private final String commandString;

    CommandType(String commandString) {
        this.commandString = commandString;
    }

    public String getCommandString() {
        return commandString;
    }

    public static CommandType fromString(String text) {
        for (CommandType type : CommandType.values()) {
            if (type.commandString.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }
} 