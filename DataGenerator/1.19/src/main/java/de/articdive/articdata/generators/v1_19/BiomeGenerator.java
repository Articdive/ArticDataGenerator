package de.articdive.articdata.generators.v1_19;

import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.DataGenHolder;
import de.articdive.articdata.datagen.DataGenType;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_18.common.DataGenerator;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
@GeneratorEntry(name = "Humidity", supported = true)
@GeneratorEntry(name = "Temperature", supported = true)
@GeneratorEntry(name = "Downfall", supported = true)
@GeneratorEntry(name = "Precipitation", supported = true)
@GeneratorEntry(name = "Ambient Loop", supported = true)
@GeneratorEntry(name = "Ambient Mood", supported = true)
@GeneratorEntry(name = "Ambient Particle", supported = true)
@GeneratorEntry(name = "Ambient Additions", supported = true)
@GeneratorEntry(name = "Background Music", supported = true)
@GeneratorEntry(name = "Fog Color", supported = true)
@GeneratorEntry(name = "Water Color", supported = true)
@GeneratorEntry(name = "Water Fog Color", supported = true)
@GeneratorEntry(name = "Sky Color", supported = true)
@GeneratorEntry(name = "Grass Color", supported = true)
@GeneratorEntry(name = "Grass Color Modifier", supported = true)
@GeneratorEntry(name = "Foliage Color", supported = true)
@GeneratorEntry(name = "Foliage Color Override", supported = true)
@GeneratorEntry(name = "Category", supported = false, description = "Removed (1.19).")
public final class BiomeGenerator extends DataGenerator<Biome> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BiomeGenerator.class);

    @Override
    public void generateNames() {
        for (Field declaredField : Biomes.class.getDeclaredFields()) {
            if (!Biome.class.isAssignableFrom(declaredField.getType())) {
                continue;
            }
            try {
                Biome b = (Biome) declaredField.get(null);
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
        Map<SoundEvent, String> soundNames = (Map<SoundEvent, String>) DataGenHolder.getNameMap(DataGenType.SOUNDS);
        Map<ParticleType<?>, String> particleNames = (Map<ParticleType<?>, String>) DataGenHolder.getNameMap(DataGenType.PARTICLES);

        List<ResourceLocation> biomeRLs = BuiltinRegistries.BIOME.keySet().stream().sorted().toList();
        JsonObject biomes = new JsonObject();

        for (ResourceLocation biomeRL : biomeRLs) {
            Biome b = BuiltinRegistries.BIOME.get(biomeRL);
            if (b == null) {
                continue;
            }
            JsonObject biome = new JsonObject();

            biome.addProperty("mojangName", names.get(b));
            biome.addProperty("humid", b.isHumid());
            biome.addProperty("temperature", b.getBaseTemperature());
            biome.addProperty("downfall", b.getDownfall());
            biome.addProperty("precipitation", b.getPrecipitation().name());

            // Background Music
            {
                JsonObject backgroundMusic = new JsonObject();
                Music m = b.getBackgroundMusic().orElse(null);
                if (m != null) {
                    backgroundMusic.addProperty("soundEvent", soundNames.get(m.getEvent()));
                    backgroundMusic.addProperty("minDelay", m.getMinDelay());
                    backgroundMusic.addProperty("maxDelay", m.getMaxDelay());
                    backgroundMusic.addProperty("replaceCurrentMusic", m.replaceCurrentMusic());
                }
                biome.add("backgroundMusic", backgroundMusic);
            }
            // Ambient Loop
            {
                SoundEvent aL = b.getAmbientLoop().orElse(null);
                biome.addProperty("ambientLoop", aL != null ? soundNames.get(aL) : null);
            }
            // Ambient Mood
            {
                JsonObject ambientMoodSettings = new JsonObject();
                b.getAmbientMood().ifPresent(ams -> {
                    ambientMoodSettings.addProperty("blockSearchExtent", ams.getBlockSearchExtent());
                    ambientMoodSettings.addProperty("soundPositionOffset", ams.getSoundPositionOffset());
                    ambientMoodSettings.addProperty("tickDelay", ams.getTickDelay());
                    ambientMoodSettings.addProperty("soundEvent", soundNames.get(ams.getSoundEvent()));

                });
                biome.add("ambientMoodSettings", ambientMoodSettings);
            }
            // Ambient Particle
            {
                JsonObject ambientParticleSettings = new JsonObject();
                b.getAmbientParticle().ifPresent(aps -> ambientParticleSettings.addProperty("type", particleNames.get(aps.getOptions().getType())));
                biome.add("ambientParticleSettings", ambientParticleSettings);
            }
            // Ambient Additions
            {
                JsonObject ambientAdditionsSettings = new JsonObject();
                b.getAmbientAdditions().ifPresent(aas -> {
                    ambientAdditionsSettings.addProperty("tickChance", aas.getTickChance());
                    ambientAdditionsSettings.addProperty("soundEvent", soundNames.get(aas.getSoundEvent()));
                });
                biome.add("ambientAdditionsSettings", ambientAdditionsSettings);
            }
            // Colors
            biome.addProperty("fogColor", b.getFogColor());
            biome.addProperty("waterColor", b.getWaterColor());
            biome.addProperty("waterFogColor", b.getWaterFogColor());
            biome.addProperty("skyColor", b.getSkyColor());
            biome.addProperty("foliageColor", b.getFoliageColor());
            biome.addProperty("foliageColorOverride", b.getSpecialEffects().getFoliageColorOverride().orElse(null));
            biome.addProperty("grassColorOverride", b.getSpecialEffects().getGrassColorOverride().orElse(null));
            biome.addProperty("grassColorModifier", b.getSpecialEffects().getGrassColorModifier().name());
            // TODO: Generation Settings

            biomes.add(biomeRL.toString(), biome);
        }
        return biomes;
    }
}
