package de.articdive.articdata.generators.v1_16_3;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import de.articdive.articdata.generators.v1_16_3.common.DataGenerator;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Namespace ID", supported = true)
@GeneratorEntry(name = "Mojang Name", supported = true)
@GeneratorEntry(name = "Block IDs", supported = true)
public final class BlockEntityGenerator extends DataGenerator<BlockEntityType> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlockEntityGenerator.class);

    @Override
    public void generateNames() {
        for (Field declaredField : BlockEntityType.class.getDeclaredFields()) {
            if (!BlockEntityType.class.isAssignableFrom(declaredField.getType())) {
                continue;
            }
            try {
                BlockEntityType<?> bet = (BlockEntityType<?>) declaredField.get(null);
                names.put(bet, declaredField.getName());
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to map block entity naming system.", e);
                return;
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public JsonObject generate() {
        List<ResourceLocation> blockEntityRLs = Registry.BLOCK_ENTITY_TYPE.keySet().stream().sorted().toList();
        JsonObject blockEntities = new JsonObject();

        for (ResourceLocation blockEntityRL : blockEntityRLs) {
            BlockEntityType<?> bet = Registry.BLOCK_ENTITY_TYPE.get(blockEntityRL);

            JsonObject blockEntity = new JsonObject();
            blockEntity.addProperty("mojangName", names.get(bet));

            // Use reflection to get valid blocks
            {
                JsonArray beBlocks = new JsonArray();
                try {
                    Field fcField = BlockEntityType.class.getDeclaredField("validBlocks");

                    fcField.setAccessible(true);

                    Set<Block> validBlocks = (Set<Block>) fcField.get(bet);
                    for (Block validBlock : validBlocks) {
                        JsonObject beBlock = new JsonObject();
                        beBlock.addProperty("id", Registry.BLOCK.getKey(validBlock).toString());

                        beBlocks.add(beBlock);
                    }
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    LOGGER.error("Failed to get block-entity blocks, skipping block-entity with ID '" + blockEntityRL + "'.", e);
                    continue;
                }
                blockEntity.add("blocks", beBlocks);
            }
            blockEntities.add(blockEntityRL.toString(), blockEntity);
        }
        return blockEntities;
    }
}
