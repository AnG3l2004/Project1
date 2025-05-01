import java.util.HashMap;
import java.util.Map;

public class IDManager {
    private Map<String, Integer> usedIds;
    private int autoIdCounter;

    public IDManager() {
        usedIds = new HashMap<>();
        autoIdCounter = 1;
    }

    public void reset() {
        usedIds.clear();
        autoIdCounter = 1;
    }

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

    public boolean isIdUsed(String id) {
        return usedIds.containsKey(id);
    }

    public int getUseCount(String id) {
        return usedIds.getOrDefault(id, 0);
    }
}

