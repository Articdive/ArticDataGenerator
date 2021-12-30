package de.articdive.articdata.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.DataGenHolder;
import de.articdive.articdata.datagen.DataGenType;
import de.articdive.articdata.datagen.ReflectionHelper;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.common.DataGenerator_1_16_5;
import java.util.Map;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.material.MaterialColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
@GeneratorEntry(name = "Color (Material Color)", supported = true)
@GeneratorEntry(name = "Firework Color (Decimal)", supported = true)
@GeneratorEntry(name = "Text Color (Decimal)", supported = true)
@GeneratorEntry(name = "TextureDiffuseColor (Decimal)", supported = true)
@GeneratorEntry(name = "TextureDiffuseColorBGR (Decimal)", supported = true)
@GeneratorEntry(name = "TextureDiffuseColors", supported = true)
public final class DyeColorGenerator_1_16_5 extends DataGenerator_1_16_5<DyeColor> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DyeColorGenerator_1_16_5.class);

    @Override
    public void generateNames() {
        for (DyeColor dc : DyeColor.values()) {
            names.put(dc, dc.getName());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public JsonArray generate() {
        Map<MaterialColor, String> bsPropertyNames = (Map<MaterialColor, String>) DataGenHolder.getNameMap(DataGenType.MAP_COLORS);

        JsonArray dyeColors = new JsonArray();

        for (DyeColor dc : DyeColor.values()) {
            JsonObject dyeColor = new JsonObject();

            dyeColor.addProperty("id", dc.getId());
            dyeColor.addProperty("mojangName", dc.getName());
            dyeColor.addProperty("color", bsPropertyNames.get(dc.getMaterialColor()));
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
