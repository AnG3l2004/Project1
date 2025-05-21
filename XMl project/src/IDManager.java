import java.util.HashSet;
import java.util.Set;
/**
 * Управлява уникални идентификатори (ID) за XML елементи.
 * Този клас предоставя функционалност за:
 * - Генериране на уникални идентификатори за XML елементи
 * - Проследяване на вече използвани идентификатори
 * - Обработка на дублирани идентификатори
 * - Нулиране на проследяването на идентификатори
 *
 * Пример за употреба:
 * <pre>
 * IDManager manager = new IDManager();
 * String id1 = manager.generateId("person");  // връща "person"
 * String id2 = manager.generateId("person");  // връща "person_1"
 * </pre>
 */
public class IDManager {
    private Set<String> usedIds;
    private int autoIdCounter;
    /**
     * Създава нов IDManager с празен регистър на използвани идентификатори.
     */
    public IDManager() {
        usedIds = new HashSet<>();
        autoIdCounter = 0;
    }
    /**
     * Нулира проследяването на идентификатори.
     * Изчиства всички записани идентификатори и нулира брояча за автоматично генерирани ID.
     */
    public void reset() {
        usedIds.clear();
        autoIdCounter = 0;
    }
    /**
     * Генерира уникален идентификатор базиран на предоставения.
     * Ако предоставеният ID е null или празен, се генерира автоматичен идентификатор.
     * Ако предоставеният ID вече е използван, се добавя числов суфикс за уникалност.
     *
     * @param providedId идентификатор за използване като база, или null за автоматично генериране
     * @return уникален идентификатор като низ
     *
     * Пример:
     * <pre>
     * String id1 = manager.generateId("person");     // връща "person"
     * String id2 = manager.generateId("person");     // връща "person_1"
     * String id3 = manager.generateId(null);         // връща "auto_1"
     * String id4 = manager.generateId("");           // връща "auto_2"
     * </pre>
     */
    public String generateId(String providedId) {
        if (providedId == null || providedId.trim().isEmpty()) {
            String autoId;
            do {
                autoId = "auto_" + (++autoIdCounter);
            } while (isIdUsed(autoId));
            usedIds.add(autoId);
            return autoId;
        }

        String baseId = providedId.trim();
        if (!isIdUsed(baseId)) {
            usedIds.add(baseId);
            return baseId;
        }

        int counter = 1;
        String newId;
        do {
            newId = baseId + "_" + counter++;
        } while (isIdUsed(newId));
        usedIds.add(newId);
        return newId;
    }
    /**
     * Проверява дали идентификаторът вече е използван.
     *
     * @param id идентификаторът за проверка
     * @return true ако е използван, false в противен случай
     *
     * Пример:
     * <pre>
     * boolean used = manager.isIdUsed("person");  // връща true ако "person" е използван
     * </pre>
     */
    public boolean isIdUsed(String id) {
        return usedIds.contains(id);
    }
    /**
     * Връща броя пъти, в които идентификаторът е бил използван.
     *
     * @param id идентификаторът за проверка
     * @return брой пъти, в които идентификаторът е бил използван, или 0 ако не е използван
     *
     * Пример:
     * <pre>
     * int count = manager.getIdUsageCount("person");  // връща броя пъти, в които "person" е използван
     * </pre>
     */
    public int getIdUsageCount(String id) {
        int count = 0;
        for (String usedId : usedIds) {
            if (usedId.equals(id) || usedId.startsWith(id + "_")) {
                count++;
            }
        }
        return count;
    }
}

