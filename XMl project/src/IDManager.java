import java.util.HashMap;
import java.util.Map;
/**
 * Управлява уникални идентификатори (ID) за XML елементи.
 * Този клас предоставя функционалност за:
 * - Генериране на уникални идентификатори за XML елементи
 * - Проследяване на вече използвани идентификатори
 * - Обработка на дублиращи се идентификатори
 * - Нулиране на проследяването на идентификатори
 *
 * Пример за използване:
 * <pre>
 * IDManager manager = new IDManager();
 * String id1 = manager.generateUniqueId("person");  // връща "person"
 * String id2 = manager.generateUniqueId("person");  // връща "person_1"
 * String id3 = manager.generateUniqueId(null);      // връща "auto_1"
 * </pre>
 */
public class IDManager {
    private Map<String, Integer> usedIds;
    private int autoIdCounter;
    /**
     * Създава нов IDManager с празен регистър на използвани идентификатори.
     */
    public IDManager() {
        usedIds = new HashMap<>();
        autoIdCounter = 1;
    }
    /**
     * Нулира проследяването на идентификатори.
     * Изчиства всички записани идентификатори и нулира брояча за автоматично генерирани ID-та.
     */
    public void reset() {
        usedIds.clear();
        autoIdCounter = 1;
    }
    /**
     * Генерира уникален идентификатор на базата на подаден такъв.
     * Ако подаденият ID е null или празен, се генерира автоматичен идентификатор.
     * Ако подаденият ID вече е използван, се добавя числов суфикс, за да стане уникален.
     *
     * @param providedId идентификатор, използван като основа, или null за автоматично генериране
     * @return уникален идентификатор като низ
     *
     * Пример:
     * <pre>
     * String id1 = manager.generateUniqueId("user");     // връща "user"
     * String id2 = manager.generateUniqueId("user");     // връща "user_1"
     * String id3 = manager.generateUniqueId(null);       // връща "auto_1"
     * String id4 = manager.generateUniqueId("");         // връща "auto_2"
     * </pre>
     */
    public String generateUniqueId(String providedId) {
        if (providedId != null && !providedId.isEmpty()) {
            if (!usedIds.containsKey(providedId)) {
                usedIds.put(providedId, 1);
                return providedId;
            } else {
                int count = usedIds.get(providedId) + 1;
                usedIds.put(providedId, count);
                return providedId + "_" + count;
            }
        } else {
            return "auto_" + autoIdCounter++;
        }
    }
    /**
     * Проверява дали даден идентификатор вече е използван.
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
        return usedIds.containsKey(id);
    }
    /**
     * Връща броя на използванията на даден идентификатор.
     *
     * @param id идентификаторът за проверка
     * @return брой пъти, в които идентификаторът е използван, или 0 ако не е използван
     *
     * Пример:
     * <pre>
     * int count = manager.getUseCount("person");  // връща броя на използванията на "person"
     * </pre>
     */
    public int getUseCount(String id) {
        return usedIds.getOrDefault(id, 0);
    }
}

