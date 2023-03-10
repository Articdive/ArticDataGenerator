package de.articdive.articdata.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;

public final class JsonUtil {
    private JsonUtil() {

    }


    public static JsonObject sortAlphabetically(JsonObject jsonObject) {
        JsonObject sortedJsonObject = new JsonObject();
        // Now sort them alphabetically
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet().stream().sorted(Map.Entry.comparingByKey()).toList()) {
            sortedJsonObject.add(entry.getKey(), entry.getValue());
        }
        return sortedJsonObject;
    }
}
