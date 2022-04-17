package de.articdive.articdata.generators.v1_16_3;

import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_16_3.common.DataGenerator;
import java.util.List;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.DimensionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Bed Works", supported = true)
@GeneratorEntry(name = "Coordinate Scale", supported = true)
@GeneratorEntry(name = "Ceiling Height", supported = true)
@GeneratorEntry(name = "Fixed Time", supported = true)
@GeneratorEntry(name = "Raids", supported = true)
@GeneratorEntry(name = "Sky Light", supported = true)
@GeneratorEntry(name = "Piglin Safe", supported = true)
@GeneratorEntry(name = "Logical Height", supported = true)
@GeneratorEntry(name = "Natural", supported = true)
@GeneratorEntry(name = "Ultra Warm", supported = true)
@GeneratorEntry(name = "Respawn Anchor Works", supported = true)
public final class DimensionTypeGenerator extends DataGenerator<DimensionType> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DimensionTypeGenerator.class);

    @Override
    public void generateNames() {
        // Not required for dimension types.
    }

    @Override
    public JsonObject generate() {
        List<ResourceLocation> dimensionTypeRLs = RegistryAccess.RegistryHolder.builtin().dimensionTypes().keySet().stream().sorted().toList();
        JsonObject dimensionTypes = new JsonObject();

        for (ResourceLocation dimensionTypeRL : dimensionTypeRLs) {
            DimensionType dt = RegistryAccess.RegistryHolder.builtin().dimensionTypes().get(dimensionTypeRL);
            if (dt == null) {
                continue;
            }
            JsonObject dimensionType = new JsonObject();

            dimensionType.addProperty("bedWorks", dt.bedWorks());
            dimensionType.addProperty("coordinateScale", dt.coordinateScale());
            dimensionType.addProperty("ceiling", dt.hasCeiling());
            dimensionType.addProperty("fixedTime", dt.hasFixedTime());
            dimensionType.addProperty("raids", dt.hasRaids());
            dimensionType.addProperty("skyLight", dt.hasSkyLight());
            dimensionType.addProperty("piglinSafe", dt.piglinSafe());
            dimensionType.addProperty("logicalHeight", dt.logicalHeight());
            dimensionType.addProperty("natural", dt.natural());
            dimensionType.addProperty("ultraWarm", dt.ultraWarm());
            dimensionType.addProperty("respawnAnchorWorks", dt.respawnAnchorWorks());

            dimensionTypes.add(dimensionTypeRL.toString(), dimensionType);
        }
        return dimensionTypes;
    }
}
