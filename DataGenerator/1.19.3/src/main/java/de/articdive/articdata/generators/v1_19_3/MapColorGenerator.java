package de.articdive.articdata.generators.v1_19_3;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.DataGenerator;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import net.minecraft.world.level.material.MaterialColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
@GeneratorEntry(name = "Color (Decimal)", supported = true)
public final class MapColorGenerator extends DataGenerator<MaterialColor> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapColorGenerator.class);

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

        for (MaterialColor mc : Arrays.stream(colors).filter(Objects::nonNull).sorted(Comparator.comparingInt(value -> value.id)).toList()) {
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
