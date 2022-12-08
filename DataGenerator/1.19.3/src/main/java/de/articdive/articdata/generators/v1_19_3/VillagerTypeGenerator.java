package de.articdive.articdata.generators.v1_19_3;

import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_19_3.common.DataGenerator_1_19_3;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
public final class VillagerTypeGenerator extends DataGenerator_1_19_3<VillagerType> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VillagerTypeGenerator.class);

    @Override
    public void generateNames() {
        for (Field declaredField : VillagerType.class.getDeclaredFields()) {
            if (!VillagerType.class.isAssignableFrom(declaredField.getType())) {
                continue;
            }
            try {
                VillagerType vt = (VillagerType) declaredField.get(null);
                names.put(vt, declaredField.getName());
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to map villager type naming system", e);
                return;
            }
        }
    }

    @Override
    public JsonObject generate() {
        JsonObject villagerTypes = new JsonObject();

        for (ResourceLocation villagerTypeRL : VILLAGER_TYPE_REGISTRY.keySet().stream().sorted(Comparator.comparingInt(value -> VILLAGER_TYPE_REGISTRY.getId(VILLAGER_TYPE_REGISTRY.get(value)))).toList()) {
            VillagerType vt = VILLAGER_TYPE_REGISTRY.get(villagerTypeRL);

            JsonObject villagerType = new JsonObject();
            villagerType.addProperty("id", VILLAGER_TYPE_REGISTRY.getId(vt));
            villagerType.addProperty("mojangName", names.get(vt));

            villagerTypes.add(villagerTypeRL.toString(), villagerType);
        }
        return villagerTypes;
    }
}
