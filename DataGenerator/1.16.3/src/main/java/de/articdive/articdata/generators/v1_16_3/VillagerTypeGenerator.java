package de.articdive.articdata.generators.v1_16_3;

import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_16_3.common.DataGenerator;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
public final class VillagerTypeGenerator extends DataGenerator<VillagerType> {
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
        List<ResourceLocation> villagerTypeRLs = Registry.VILLAGER_TYPE.keySet().stream().sorted(Comparator.comparingInt(value -> Registry.VILLAGER_TYPE.getId(Registry.VILLAGER_TYPE.get(value)))).toList();;
        JsonObject villagerTypes = new JsonObject();

        for (ResourceLocation villagerTypeRL : villagerTypeRLs) {
            VillagerType vt = Registry.VILLAGER_TYPE.get(villagerTypeRL);

            JsonObject villagerType = new JsonObject();
            villagerType.addProperty("id", Registry.VILLAGER_TYPE.getId(vt));
            villagerType.addProperty("mojangName", names.get(vt));

            villagerTypes.add(villagerTypeRL.toString(), villagerType);
        }
        return villagerTypes;
    }
}
