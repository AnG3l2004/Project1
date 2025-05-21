package commands;

/**
 * Команда за изтриване на атрибут или дъщерен елемент от XML елемент.
 * Тази команда поддържа изтриване на:
 * - Атрибути на елемент
 * - Дъщерни елементи по име на таг
 * - Текстово съдържание
 */
public class DeleteCommand implements ParameterizedCommand {
    private XMLCommandHandler handler;
    private String id;
    private String key;

    /**
     * Създава нова команда за изтриване.
     *
     * @param handler обработчикът на XML команди
     * @param id ID на елемента, който ще бъде модифициран
     * @param key името на атрибута или тага за изтриване
     */
    public DeleteCommand(XMLCommandHandler handler, String id, String key) {
        this.handler = handler;
        this.id = id;
        this.key = key;
    }

    /**
     * Задава ID-то на елемента.
     *
     * @param id ID на елемента
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Задава ключа (име на атрибут или таг) за изтриване.
     *
     * @param key името на атрибута или тага
     */
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
    
    @Override
    public String getHelp() {
        return "delete <id> <key>        - Delete attribute or child element";
    }
} 