package azagwen.aza_mod_exp;

import azagwen.aza_mod_exp.item.AzaItems;
import com.google.common.collect.Maps;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.function.Function;

public class MainInit implements ModInitializer {
    private static final Logger LOGGER = LogManager.getLogger("Aza Main");
    public static final Map<Block, StairsBlock> BLOCK_TO_STAIRS_MAP = Maps.newHashMap();
    public static final Map<Block, SlabBlock> BLOCK_TO_SLAB_MAP = Maps.newHashMap();
    public static final Map<Block, WallBlock> BLOCK_TO_WALL_MAP = Maps.newHashMap();
    public static final Map<Block, WallBlock> BLOCK_TO_FENCE_MAP = Maps.newHashMap();

    public static Identifier id(String path) {
        return new Identifier("aza_mod_exp", path);
    }

    @SuppressWarnings("unchecked")
    public <B> void populateConversionMap(RecipeManager recipeManager, String type, Map<Block, B> conversionMap, Function<BlockItem, Boolean> condition) {
        for (var key : recipeManager.keys().toList()) { //Loop through every single loaded recipe
            var recipe = recipeManager.get(key).get();

            //Check if:
            // -the recipe is a crafting grid recipe
            // -the output of it is a block-item
            // -the condition Fuction applies
            if ((recipe.getType() == RecipeType.CRAFTING) && (recipe.getOutput().getItem() instanceof BlockItem resultBlockItem) && condition.apply(resultBlockItem)) {
                for (var ingredient : recipe.getIngredients()) { //Loop through all ingredients in the recipe found above
                    for (var stack : ingredient.getMatchingStacks()) {  //Loop through all the stacks in each ingredient
                        if (stack.getItem() instanceof BlockItem blockItem) { //Check if the current stack is a block-item
                            var block = Registry.BLOCK.get(Registry.ITEM.getId(blockItem));
                            conversionMap.put(block, (B) resultBlockItem.getBlock()); //All the previous conditions have been confirmed, add the blocks to their respective map
                        }
                    }
                }
            }
        }

        LOGGER.info("Successfully populated " + conversionMap.size() + " Block-to-" + type + " operations");
    }

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            this.populateConversionMap(server.getRecipeManager(), "Stairs", BLOCK_TO_STAIRS_MAP, (blockItem) -> blockItem.getBlock() instanceof StairsBlock);
            this.populateConversionMap(server.getRecipeManager(), "Slab", BLOCK_TO_SLAB_MAP, (blockItem) -> blockItem.getBlock() instanceof SlabBlock);
            this.populateConversionMap(server.getRecipeManager(), "Wall", BLOCK_TO_WALL_MAP, (blockItem) -> blockItem.getBlock() instanceof WallBlock);
            this.populateConversionMap(server.getRecipeManager(), "Fence", BLOCK_TO_FENCE_MAP, (blockItem) -> blockItem.getBlock() instanceof FenceBlock);
        });
        AzaItems.registerAll();
    }
}
