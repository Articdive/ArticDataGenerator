package de.articdive.articdata.generators.v1_17;

import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.DataGenerator;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.gameevent.GameEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
@GeneratorEntry(name = "Notification Radius", supported = true)
public final class GameEventGenerator extends DataGenerator<GameEvent> {
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
        List<ResourceLocation> gameEventRLs = Registry.GAME_EVENT.keySet().stream().sorted(Comparator.comparingInt(value -> Registry.GAME_EVENT.getId(Registry.GAME_EVENT.get(value)))).toList();
        JsonObject gameEvents = new JsonObject();

        for (ResourceLocation gameEventRL : gameEventRLs) {
            GameEvent ge = Registry.GAME_EVENT.get(gameEventRL);
            JsonObject gameEvent = new JsonObject();

            gameEvent.addProperty("id", Registry.GAME_EVENT.getId(ge));
            gameEvent.addProperty("mojangName", names.get(ge));
            gameEvent.addProperty("notificationRadius", ge.getNotificationRadius());
            gameEvents.add(gameEventRL.toString(), gameEvent);
        }
        return gameEvents;
    }
}
