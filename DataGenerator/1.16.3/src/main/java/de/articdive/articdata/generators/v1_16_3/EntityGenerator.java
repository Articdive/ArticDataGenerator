package de.articdive.articdata.generators.v1_16_3;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.DataGenHolder;
import de.articdive.articdata.datagen.DataGenType;
import de.articdive.articdata.datagen.DataGenerator;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
@GeneratorEntry(name = "Translation Key", supported = true)
@GeneratorEntry(name = "Loot Table Key", supported = true)
@GeneratorEntry(name = "Entity Data Packet Info", supported = true)
@GeneratorEntry(name = "Packet Type", supported = true)
@GeneratorEntry(name = "Fire Immunity", supported = true)
@GeneratorEntry(name = "Height", supported = true)
@GeneratorEntry(name = "Width", supported = true)
@GeneratorEntry(name = "Client Tracking Range", supported = true)
public final class EntityGenerator extends DataGenerator<EntityType<?>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityGenerator.class);
    private static final Map<EntityType<?>, Class<?>> entityClasses = new LinkedHashMap<>();

    @Override
    public void generateNames() {
        for (Field declaredField : EntityType.class.getDeclaredFields()) {
            if (!EntityType.class.isAssignableFrom(declaredField.getType())) {
                continue;
            }
            try {
                EntityType<?> et = (EntityType<?>) declaredField.get(null);
                // FIeld name
                names.put(et, declaredField.getName());
                entityClasses.put(et, (Class<?>) ((ParameterizedType) declaredField.getGenericType()).getActualTypeArguments()[0]);
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to map entity naming system.", e);
                return;
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public JsonObject generate() {
        Map<EntityDataSerializer<?>, String> edsNames = (Map<EntityDataSerializer<?>, String>) DataGenHolder.getNameMap(DataGenType.ENTITY_DATA_SERIALIZERS);

        List<ResourceLocation> entityRLs = Registry.ENTITY_TYPE.keySet().stream().sorted(Comparator.comparingInt(value -> Registry.ENTITY_TYPE.getId(Registry.ENTITY_TYPE.get(value)))).toList();
        JsonObject entities = new JsonObject();

        for (ResourceLocation entityRL : entityRLs) {
            EntityType<?> et = Registry.ENTITY_TYPE.get(entityRL);

            // Complicated but we need to get the Entity class of EntityType.
            // E.g. EntityType<T> we need to get T and check what classes T implements.

            Class<?> entityClass = entityClasses.get(et);
            String packetType;
            if (Player.class.isAssignableFrom(entityClass)) {
                packetType = "PLAYER";
            } else if (LivingEntity.class.isAssignableFrom(entityClass)) {
                packetType = "LIVING";
            } else if (Painting.class.isAssignableFrom(entityClass)) {
                packetType = "PAINTING";
            } else if (ExperienceOrb.class.isAssignableFrom(entityClass)) {
                packetType = "EXPERIENCE_ORB";
            } else {
                packetType = "BASE";
            }

            JsonObject entity = new JsonObject();

            entity.addProperty("id", Registry.ENTITY_TYPE.getId(et));
            entity.addProperty("mojangName", names.get(et));
            entity.addProperty("translationKey", et.getDescriptionId());
            // entity.addProperty("category", et.getCategory().toString()); basically useless
            entity.addProperty("packetType", packetType);
            entity.addProperty("fireImmune", et.fireImmune());
            entity.addProperty("height", et.getHeight());
            entity.addProperty("width", et.getWidth());
            entity.addProperty("clientTrackingRange", et.clientTrackingRange());
            entity.addProperty("lootTableLocation", et.getDefaultLootTable().toString());
            // entity.addProperty("fixed", et.getDimensions().fixed); also basically useless

            // Use some reflection to find some metadata properties we need
            {
                JsonArray metadata = new JsonArray();
                Class<?> currentClass = entityClass;
                do {
                    for (Field declaredField : currentClass.getDeclaredFields()) {
                        JsonObject entityMetadata = new JsonObject();
                        if (!EntityDataAccessor.class.isAssignableFrom(declaredField.getType())) {
                            continue;
                        }
                        try {
                            declaredField.setAccessible(true);
                            EntityDataAccessor<?> eda = (EntityDataAccessor<?>) declaredField.get(null);
                            eda.getSerializer();

                            entityMetadata.addProperty("mojangName", declaredField.getName().toLowerCase());
                            entityMetadata.addProperty("id", eda.getId());
                            entityMetadata.addProperty("serializer", edsNames.get(eda.getSerializer()));

                            metadata.add(entityMetadata);
                        } catch (IllegalAccessException e) {
                            LOGGER.error("Failed to access entity metadata for '" + entityRL.toString() + "'.", e);
                        }

                    }
                } while ((currentClass = currentClass.getSuperclass()) != null);
                entity.add("metadata", metadata);
            }

            entities.add(entityRL.toString(), entity);
        }
        return entities;
    }
}
