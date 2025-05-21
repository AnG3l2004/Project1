package commands;

/**
 * Базов интерфейс за всички XML команди, които могат да бъдат изпълнени
 */
public interface XMLCommand {
    void execute() throws Exception;
    
    /**
     * Връща помощен текст, описващ предназначението и употребата на командата
     * @return String съдържащ помощна информация
     */
    String getHelp();
} 