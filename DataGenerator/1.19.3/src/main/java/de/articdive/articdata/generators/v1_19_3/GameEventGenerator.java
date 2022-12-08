package de.articdive.articdata.generators.v1_19_3;

import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_19_3.common.DataGenerator_1_19_3;
import java.lang.reflect.Field;
import java.util.Comparator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.gameevent.GameEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
@GeneratorEntry(name = "Notification Radius", supported = true)
public final class GameEventGenerator extends DataGenerator_1_19_3<GameEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameEventGenerator.class);

    @Override
    public void generateNames() {
        for (Field declaredField : GameEvent.class.getDeclaredFields()) {
            if (!GameEvent.class.isAssignableFrom(declaredField.getType())) {
                continue;
            }
            try {
                GameEvent ge = (GameEvent) declaredField.get(null);
                names.put(ge, declaredField.getName());
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to map game event naming system", e);
                return;
            }
        }
    }

    @Override
    public JsonObject generate() {
        JsonObject gameEvents = new JsonObject();

        for (ResourceLocation gameEventRL : GAME_EVENT_REGISTRY.keySet().stream().sorted(Comparator.comparingInt(value -> GAME_EVENT_REGISTRY.getId(GAME_EVENT_REGISTRY.get(value)))).toList()) {
            GameEvent ge = GAME_EVENT_REGISTRY.get(gameEventRL);
            JsonObject gameEvent = new JsonObject();

            gameEvent.addProperty("id", GAME_EVENT_REGISTRY.getId(ge));
            gameEvent.addProperty("mojangName", names.get(ge));
            gameEvent.addProperty("notificationRadius", ge.getNotificationRadius());
            gameEvents.add(gameEventRL.toString(), gameEvent);
        }
        return gameEvents;
    }
}
