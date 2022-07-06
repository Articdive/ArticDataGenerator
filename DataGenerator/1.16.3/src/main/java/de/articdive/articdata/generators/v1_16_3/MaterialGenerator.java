package de.articdive.articdata.generators.v1_16_3;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import de.articdive.articdata.datagen.DataGenerator;
import de.articdive.articdata.datagen.ReflectionHelper;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Protocol ID", supported = true)
@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
@GeneratorEntry(name = "Translation Key", supported = true)
@GeneratorEntry(name = "Depletes", supported = true)
@GeneratorEntry(name = "Max Stack Size", supported = true)
@GeneratorEntry(name = "Max Damage (Durability)", supported = true)
@GeneratorEntry(name = "Edible", supported = true)
@GeneratorEntry(name = "Fire Resistant", supported = true)
@GeneratorEntry(name = "Corresponding Block", supported = true)
@GeneratorEntry(name = "Eating & Drinking Sound", supported = true)
@GeneratorEntry(name = "Food Properties", supported = true)
@GeneratorEntry(name = "Armor Properties", supported = true)
@GeneratorEntry(name = "Spawn Egg Properties", supported = true)
@GeneratorEntry(name = "Tool Properties", supported = true)
@GeneratorEntry(name = "Bucket Properties", supported = true)
@GeneratorEntry(name = "Dye Properties", supported = true)
public final class MaterialGenerator extends DataGenerator<Item> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MaterialGenerator.class);

    @Override
    public void generateNames() {
        for (Field declaredField : Items.class.getDeclaredFields()) {
            if (!Item.class.isAssignableFrom(declaredField.getType())) {
                continue;
            }
            try {
                Item i = (Item) declaredField.get(null);
                names.put(i, declaredField.getName());
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to map item naming system.", e);
                return;
            }
        }
    }

    @Override
    public JsonObject generate() {
        List<ResourceLocation> itemRLs = Registry.ITEM.keySet().stream().sorted(Comparator.comparingInt(value -> Registry.ITEM.getId(Registry.ITEM.get(value)))).toList();
        JsonObject items = new JsonObject();

        for (ResourceLocation itemRL : itemRLs) {
            Item i = Registry.ITEM.get(itemRL);

            JsonObject item = new JsonObject();
            item.addProperty("id", Registry.ITEM.getId(i));
            item.addProperty("mojangName", names.get(i));
            item.addProperty("rarity", i.getRarity(ItemStack.EMPTY).name()); // We want the rarity property here
            item.addProperty("translationKey", i.getDescriptionId());
            item.addProperty("depletes", i.canBeDepleted());
            item.addProperty("maxStackSize", i.getMaxStackSize());
            item.addProperty("maxDamage", i.getMaxDamage());
            // item.addProperty("complex", i.isComplex()); basically useless
            item.addProperty("edible", i.isEdible());
            item.addProperty("fireResistant", i.isFireResistant());
            item.addProperty("blockId", Registry.BLOCK.getKey(Block.byItem(i)).toString());
            ResourceLocation eatingSound = Registry.SOUND_EVENT.getKey(i.getEatingSound());
            if (eatingSound != null) {
                item.addProperty("eatingSound", eatingSound.toString());
            }
            ResourceLocation drinkingSound = Registry.SOUND_EVENT.getKey(i.getDrinkingSound());
            if (drinkingSound != null) {
                item.addProperty("drinkingSound", drinkingSound.toString());
            }
            // Food Properties
            if (i.isEdible() && i.getFoodProperties() != null) {
                FoodProperties fp = i.getFoodProperties();

                JsonObject foodProperties = new JsonObject();
                foodProperties.addProperty("alwaysEdible", fp.canAlwaysEat());
                foodProperties.addProperty("isFastFood", fp.isFastFood());
                foodProperties.addProperty("nutrition", fp.getNutrition());
                foodProperties.addProperty("saturationModifier", fp.getSaturationModifier());

                {
                    // Food effects
                    JsonArray effects = new JsonArray();
                    for (Pair<MobEffectInstance, Float> effect : fp.getEffects()) {
                        ResourceLocation rl = Registry.MOB_EFFECT.getKey(effect.getFirst().getEffect());

                        if (rl == null) {
                            continue;
                        }
                        JsonObject foodEffect = new JsonObject();
                        foodEffect.addProperty("id", rl.toString());
                        foodEffect.addProperty("amplifier", effect.getFirst().getAmplifier());
                        foodEffect.addProperty("duration", effect.getFirst().getDuration());
                        foodEffect.addProperty("chance", effect.getSecond());
                        effects.add(foodEffect);
                    }
                    foodProperties.add("effects", effects);
                }
                item.add("foodProperties", foodProperties);
            }
            // Custom Properties for the different types of ItemStack
            {
                JsonObject specificItemData = new JsonObject();
                // Armor properties
                if (i instanceof ArmorItem ai) {

                    JsonObject armorProperties = new JsonObject();
                    armorProperties.addProperty("defense", ai.getDefense());
                    armorProperties.addProperty("toughness", ai.getToughness());
                    armorProperties.addProperty("slot", ai.getSlot().getName());

                    specificItemData.add("armorProperties", armorProperties);
                }
                // SpawnEgg properties
                if (i instanceof SpawnEggItem sei) {

                    JsonObject spawnEggProperties = new JsonObject();
                    spawnEggProperties.addProperty("entityType", Registry.ENTITY_TYPE.getKey(sei.getType(null)).toString());

                    specificItemData.add("spawnEggProperties", spawnEggProperties);
                }
                // TieredItem Properties
                if (i instanceof TieredItem) {

                    JsonObject tieredItemProperties = new JsonObject();
                    tieredItemProperties.addProperty("tier", ((Tiers) ((TieredItem) i).getTier()).name());

                    specificItemData.add("tieredItemProperties", tieredItemProperties);
                }
                // Bucket Properties
                if (i instanceof BucketItem) {

                    JsonObject bucketItemProperties = new JsonObject();
                    Fluid f = ReflectionHelper.getHiddenField(Fluid.class, "content", BucketItem.class, (BucketItem) i);
                    bucketItemProperties.addProperty(
                            "fluid",
                            Registry.FLUID.getKey(f).toString()
                    );

                    specificItemData.add("bucketItemProperties", bucketItemProperties);
                }
                // Dye Properties
                if (i instanceof DyeItem) {

                    JsonObject bucketItemProperties = new JsonObject();
                    bucketItemProperties.addProperty(
                            "dyeColor",
                            ((DyeItem) i).getDyeColor().getName()
                    );

                    specificItemData.add("dyeItemProperties", bucketItemProperties);
                }
                item.add("specificItemData", specificItemData);
            }

            items.add(itemRL.toString(), item);
        }
        return items;
    }
}
