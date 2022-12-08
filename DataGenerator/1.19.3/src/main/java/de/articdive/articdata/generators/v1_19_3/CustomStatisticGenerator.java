package de.articdive.articdata.generators.v1_19_3;

import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_19_3.common.DataGenerator_1_19_3;
import java.lang.reflect.Field;
import java.util.Comparator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
public final class CustomStatisticGenerator extends DataGenerator_1_19_3<ResourceLocation> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomStatisticGenerator.class);

    @Override
    public void generateNames() {
        for (Field declaredField : Stats.class.getDeclaredFields()) {
            if (!ResourceLocation.class.isAssignableFrom(declaredField.getType())) {
                continue;
            }
            try {
                ResourceLocation rl = (ResourceLocation) declaredField.get(null);
                names.put(rl, declaredField.getName());
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to map custom statistics naming system", e);
                return;
            }
        }
    }

    @Override
    public JsonObject generate() {
        JsonObject customStatistics = new JsonObject();

        for (ResourceLocation customStatisticRL : CUSTOM_STAT_REGISTRY.keySet().stream().sorted(Comparator.comparingInt(value -> CUSTOM_STAT_REGISTRY.getId(CUSTOM_STAT_REGISTRY.get(value)))).toList()) {
            ResourceLocation rl = CUSTOM_STAT_REGISTRY.get(customStatisticRL);

            JsonObject customStatistic = new JsonObject();
            customStatistic.addProperty("id", CUSTOM_STAT_REGISTRY.getId(rl));
            customStatistic.addProperty("mojangName", names.get(rl));

            customStatistics.add(customStatisticRL.toString(), customStatistic);
        }
        return customStatistics;
    }
}
