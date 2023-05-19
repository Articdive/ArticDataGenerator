package de.articdive.articdata.generators.v1_20;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.DataGenHolder;
import de.articdive.articdata.datagen.DataGenType;
import de.articdive.articdata.datagen.DataGenerator;
import de.articdive.articdata.datagen.ReflectionHelper;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.material.MapColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
@GeneratorEntry(name = "Color (Corresponding Map Color)", supported = true)
@GeneratorEntry(name = "Firework Color (Decimal)", supported = true)
@GeneratorEntry(name = "Text Color (Decimal)", supported = true)
@GeneratorEntry(name = "TextureDiffuseColor (Decimal)", supported = true)
@GeneratorEntry(name = "TextureDiffuseColorBGR (Decimal)", supported = true)
@GeneratorEntry(name = "TextureDiffuseColors", supported = true)
public final class DyeColorGenerator extends DataGenerator<DyeColor> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DyeColorGenerator.class);

    @Override
    public void generateNames() {
        for (DyeColor dc : DyeColor.values()) {
            names.put(dc, dc.getName());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public JsonArray generate() {
        Map<MapColor, String> mapColorNames = (Map<MapColor, String>) DataGenHolder.getNameMap(DataGenType.MAP_COLORS);

        JsonArray dyeColors = new JsonArray();
        for (DyeColor dc : Arrays.stream(DyeColor.values()).sorted(Comparator.comparingInt(DyeColor::getId)).toList()) {
            JsonObject dyeColor = new JsonObject();

            dyeColor.addProperty("id", dc.getId());
            dyeColor.addProperty("mojangName", dc.getName());
            dyeColor.addProperty("mapColor", mapColorNames.get(dc.getMapColor()));
            dyeColor.addProperty("fireworkColor", dc.getFireworkColor());
            dyeColor.addProperty("textColor", ReflectionHelper.getHiddenField(float.class, "textColor", DyeColor.class, dc));
            dyeColor.addProperty("textureDiffuseColor", ReflectionHelper.getHiddenField(float.class, "textureDiffuseColor", DyeColor.class, dc));
            dyeColor.addProperty("textureDiffuseColorBGR", ReflectionHelper.getHiddenField(float.class, "textureDiffuseColorBGR", DyeColor.class, dc));
            JsonArray array = new JsonArray();
            for (float f : dc.getTextureDiffuseColors()) {
                array.add(f);
            }
            dyeColor.add("textureDiffuseColors", array);

            dyeColors.add(dyeColor);
        }
        return dyeColors;
    }
}
