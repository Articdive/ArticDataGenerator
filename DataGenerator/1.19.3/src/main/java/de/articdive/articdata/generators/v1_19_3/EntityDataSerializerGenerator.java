package de.articdive.articdata.generators.v1_19_3;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.DataGenerator;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
public final class EntityDataSerializerGenerator extends DataGenerator<EntityDataSerializer<?>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityDataSerializerGenerator.class);

    @Override
    public void generateNames() {
        for (Field declaredField : EntityDataSerializers.class.getDeclaredFields()) {
            if (!EntityDataSerializer.class.isAssignableFrom(declaredField.getType())) {
                continue;
            }
            try {
                EntityDataSerializer<?> eds = (EntityDataSerializer<?>) declaredField.get(null);
                names.put(eds, declaredField.getName());
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to map entity data serializer system.", e);
                return;
            }
        }
    }

    @Override
    public JsonArray generate() {
        List<EntityDataSerializer<?>> serializers = new ArrayList<>();
        for (Field declaredField : EntityDataSerializers.class.getDeclaredFields()) {
            if (!EntityDataSerializer.class.isAssignableFrom(declaredField.getType())) {
                continue;
            }
            try {
                serializers.add((EntityDataSerializer<?>) declaredField.get(null));
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to get entity data serializer from the entity data serializer mapping system.", e);
            }
        }

        serializers.sort(Comparator.comparingInt(EntityDataSerializers::getSerializedId));

        JsonArray entityDataSerializers = new JsonArray();
        for (EntityDataSerializer<?> serializer : serializers) {
            JsonObject entityDataSerializer = new JsonObject();

            entityDataSerializer.addProperty("id", EntityDataSerializers.getSerializedId(serializer));
            entityDataSerializer.addProperty("type", names.get(serializer));

            entityDataSerializers.add(entityDataSerializer);
        }
        return entityDataSerializers;
    }
}
