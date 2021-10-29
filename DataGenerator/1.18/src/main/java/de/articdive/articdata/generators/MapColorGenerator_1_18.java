package de.articdive.articdata.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.common.DataGenerator_1_16_5;
import java.lang.reflect.Field;
import net.minecraft.world.level.material.MaterialColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
@GeneratorEntry(name = "Color (Decimal)", supported = true)
public final class MapColorGenerator_1_18 extends DataGenerator_1_16_5<MaterialColor> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapColorGenerator_1_18.class);

    @Override
    public void generateNames() {
        for (Field declaredField : MaterialColor.class.getDeclaredFields()) {
            if (declaredField.getName().equals("MATERIAL_COLORS") || declaredField.getType() != MaterialColor.class) {
                continue;
            }
            try {
                MaterialColor mc = (MaterialColor) declaredField.get(null);
                names.put(mc, declaredField.getName());
            } catch (IllegalAccessException e) {
                // Just stop I guess
                LOGGER.error("Failed to access map color naming system", e);
                return;
            }
        }
    }

    @Override
    public JsonArray generate() {
        JsonArray mapColors = new JsonArray();
        MaterialColor[] colors;
        try {
            Field f = MaterialColor.class.getDeclaredField("MATERIAL_COLORS");
            f.setAccessible(true);
            colors = (MaterialColor[]) f.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e ) {
            return mapColors;
        }

        for (MaterialColor mc : colors) {
            if (mc == null) {
                continue;
            }
            JsonObject mapColor = new JsonObject();

            mapColor.addProperty("id", mc.id);
            mapColor.addProperty("mojangName", names.get(mc));
            mapColor.addProperty("color", mc.col);

            mapColors.add(mapColor);
        }
        return mapColors;
    }
}
