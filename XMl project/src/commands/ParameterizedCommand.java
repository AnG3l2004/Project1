package commands;

/**
 * Интерфейс за команди, които изискват задаване на параметри
 */
public interface ParameterizedCommand extends XMLCommand {
    void setParameters(String[] tokens) throws Exception;
} 