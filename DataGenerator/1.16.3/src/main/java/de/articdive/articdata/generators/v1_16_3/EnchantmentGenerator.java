package de.articdive.articdata.generators.v1_16_3;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_16_3.common.DataGenerator;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
@GeneratorEntry(name = "Translation Key", supported = true)
@GeneratorEntry(name = "Max Level", supported = true)
@GeneratorEntry(name = "Rarity", supported = true)
@GeneratorEntry(name = "Curse", supported = true)
@GeneratorEntry(name = "Discoverable", supported = true)
@GeneratorEntry(name = "Tradeable", supported = true)
@GeneratorEntry(name = "Treasure Only", supported = true)
@GeneratorEntry(name = "Category", supported = true)
@GeneratorEntry(name = "Incompatible Enchantments", supported = true)
public final class EnchantmentGenerator extends DataGenerator<Enchantment> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnchantmentGenerator.class);

    @Override
    public void generateNames() {
        for (Field declaredField : Enchantments.class.getDeclaredFields()) {
            if (!Enchantment.class.isAssignableFrom(declaredField.getType())) {
                continue;
            }
            try {
                Enchantment e = (Enchantment) declaredField.get(null);
                names.put(e, declaredField.getName());
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to map enchantment naming system", e);
                return;
            }
        }
    }

    @Override
    public JsonObject generate() {
        Set<ResourceLocation> enchantmentRLs = Registry.ENCHANTMENT.keySet();
        JsonObject enchantments = new JsonObject();

        for (ResourceLocation enchantmentRL : enchantmentRLs) {
            Enchantment e = Registry.ENCHANTMENT.get(enchantmentRL);
            if (e == null) {
                continue;
            }
            JsonObject enchantment = new JsonObject();

            enchantment.addProperty("id", Registry.ENCHANTMENT.getId(e));
            enchantment.addProperty("mojangName", names.get(e));
            enchantment.addProperty("translationKey", e.getDescriptionId());
            enchantment.addProperty("maxLevel", e.getMaxLevel());
            enchantment.addProperty("minLevel", e.getMinLevel());
            enchantment.addProperty("rarity", e.getRarity().toString());
            enchantment.addProperty("curse", e.isCurse());
            enchantment.addProperty("discoverable", e.isDiscoverable());
            enchantment.addProperty("tradeable", e.isTradeable());
            enchantment.addProperty("treasureOnly", e.isTreasureOnly());
            enchantment.addProperty("category", e.category.name());

            JsonArray incompatibleEnchaments = new JsonArray();
            // Compatabilities
            for (Enchantment e1 : Registry.ENCHANTMENT.stream().filter(e1 -> !e.isCompatibleWith(e1) && e != e1).collect(Collectors.toList())) {
                ResourceLocation e1Key = Registry.ENCHANTMENT.getKey(e1);
                if (e1Key != null) {
                    incompatibleEnchaments.add(e1Key.toString());
                }
            }
            enchantment.add("incompatibleEnchantments", incompatibleEnchaments);

            enchantments.add(enchantmentRL.toString(), enchantment);
        }
        return enchantments;
    }
}
