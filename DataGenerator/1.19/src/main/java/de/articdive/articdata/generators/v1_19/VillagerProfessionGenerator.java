package de.articdive.articdata.generators.v1_19;

import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_16_3.common.DataGenerator;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.npc.VillagerProfession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
@GeneratorEntry(name = "Work Sound", supported = true)
public final class VillagerProfessionGenerator extends DataGenerator<VillagerProfession> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VillagerProfessionGenerator.class);

    @Override
    public void generateNames() {
        for (Field declaredField : VillagerProfession.class.getDeclaredFields()) {
            if (!VillagerProfession.class.isAssignableFrom(declaredField.getType())) {
                continue;
            }
            try {
                VillagerProfession vp = (VillagerProfession) declaredField.get(null);
                names.put(vp, declaredField.getName());
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to map villager profession naming system", e);
                return;
            }
        }
    }

    @Override
    public JsonObject generate() {
        List<ResourceLocation> villagerProfessionRLs = Registry.VILLAGER_PROFESSION.keySet().stream().sorted(Comparator.comparingInt(value -> Registry.VILLAGER_PROFESSION.getId(Registry.VILLAGER_PROFESSION.get(value)))).toList();;
        JsonObject villagerProfessions = new JsonObject();

        for (ResourceLocation villagerProfessionRL : villagerProfessionRLs) {
            VillagerProfession vp = Registry.VILLAGER_PROFESSION.get(villagerProfessionRL);

            JsonObject villagerProfession = new JsonObject();

            villagerProfession.addProperty("id", Registry.VILLAGER_PROFESSION.getId(vp));
            villagerProfession.addProperty("mojangName", names.get(vp));

            SoundEvent workSound = vp.workSound();
            if (workSound != null) {
                ResourceLocation workSoundRL = Registry.SOUND_EVENT.getKey(workSound);
                if (workSoundRL != null) {
                    villagerProfession.addProperty("workSound", workSoundRL.toString());
                }
            }

            villagerProfessions.add(villagerProfessionRL.toString(), villagerProfession);
        }
        return villagerProfessions;
    }
}
