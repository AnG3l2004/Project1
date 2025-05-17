package commands;

public interface XMLCommand {
    void setParameters(String[] tokens) throws Exception;
    void execute() throws Exception;
} 