package de.articdive.articdata.generators.v1_16_3;

import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.DataGenerator;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.Biomes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
@GeneratorEntry(name = "Humidity", supported = true)
@GeneratorEntry(name = "Scale", supported = true)
@GeneratorEntry(name = "Depth", supported = true)
@GeneratorEntry(name = "Temperature", supported = true)
@GeneratorEntry(name = "Percipitation", supported = true)
@GeneratorEntry(name = "Downfall", supported = true)
@GeneratorEntry(name = "Fog Color", supported = true)
@GeneratorEntry(name = "Water Color", supported = true)
@GeneratorEntry(name = "Water Fog Color", supported = true)
@GeneratorEntry(name = "Sky Color", supported = true)
@GeneratorEntry(name = "Grass Color", supported = true)
@GeneratorEntry(name = "Grass Color Modifier", supported = true)
@GeneratorEntry(name = "Foliage Color", supported = true)
@GeneratorEntry(name = "Foliage Color Override", supported = true)
@GeneratorEntry(name = "Category", supported = true)
public final class BiomeGenerator extends DataGenerator<ResourceKey<Biome>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BiomeGenerator.class);

    @SuppressWarnings("unchecked")
    @Override
    public void generateNames() {
        for (Field declaredField : Biomes.class.getDeclaredFields()) {
            if (!ResourceKey.class.isAssignableFrom(declaredField.getType())) {
                continue;
            }
            try {
                ResourceKey<Biome> b = (ResourceKey<Biome>) declaredField.get(null);
                names.put(b, declaredField.getName());
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to map fluid naming system.", e);
                return;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public JsonObject generate() {
        List<ResourceLocation> biomeRLs = BuiltinRegistries.BIOME.keySet().stream().sorted().toList();
        JsonObject biomes = new JsonObject();

        for (ResourceLocation biomeRL : biomeRLs) {
            Biome b = BuiltinRegistries.BIOME.get(biomeRL);
            if (b == null) {
                continue;
            }
            JsonObject biome = new JsonObject();

            biome.addProperty("mojangName", names.get(BuiltinRegistries.BIOME.getResourceKey(b).get()));
            biome.addProperty("humid", b.isHumid());
            biome.addProperty("scale", b.getScale());
            biome.addProperty("depth", b.getDepth());
            biome.addProperty("temperature", b.getBaseTemperature());
            biome.addProperty("downfall", b.getDownfall());
            biome.addProperty("precipitation", b.getPrecipitation().name());
            biome.addProperty("category", b.getBiomeCategory().name());

            {
                // Use reflection to access SpecialBiomeEffects (why the hell is this private anyway?)
                try {
                    BiomeSpecialEffects bse = b.getSpecialEffects();
                    Field fcField = BiomeSpecialEffects.class.getDeclaredField("fogColor");
                    Field wcField = BiomeSpecialEffects.class.getDeclaredField("waterColor");
                    Field wfcField = BiomeSpecialEffects.class.getDeclaredField("waterFogColor");
                    Field scField = BiomeSpecialEffects.class.getDeclaredField("skyColor");
                    Field fcoField = BiomeSpecialEffects.class.getDeclaredField("foliageColorOverride");
                    Field gcoField = BiomeSpecialEffects.class.getDeclaredField("grassColorOverride");
                    Field gcmField = BiomeSpecialEffects.class.getDeclaredField("grassColorModifier");

                    fcField.setAccessible(true);
                    wcField.setAccessible(true);
                    wfcField.setAccessible(true);
                    scField.setAccessible(true);
                    fcoField.setAccessible(true);
                    gcoField.setAccessible(true);
                    gcmField.setAccessible(true);

                    int fogColor = fcField.getInt(bse);
                    int waterColor = wcField.getInt(bse);
                    int waterFogColor = wfcField.getInt(bse);
                    int skyColor = scField.getInt(bse);
                    Optional<Integer> foliageColorOverride = (Optional<Integer>) fcoField.get(bse);
                    Optional<Integer> grassColorOverride = (Optional<Integer>) gcoField.get(bse);
                    BiomeSpecialEffects.GrassColorModifier grassColorModifier = (BiomeSpecialEffects.GrassColorModifier) gcmField.get(bse);

                    biome.addProperty("fogColor", fogColor);
                    biome.addProperty("waterColor", waterColor);
                    biome.addProperty("waterFogColor", waterFogColor);
                    biome.addProperty("skyColor", skyColor);
                    biome.addProperty("foliageColorOverride", foliageColorOverride.orElse(null));
                    biome.addProperty("grassColorOverride", grassColorOverride.orElse(null));
                    biome.addProperty("grassColorModifier", grassColorModifier.name());
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    LOGGER.error("Failed to get biome effects, skipping biome with ID '" + biomeRL + "'.", e);
                    continue;
                }
            }
            biomes.add(biomeRL.toString(), biome);
        }
        return biomes;
    }
}
