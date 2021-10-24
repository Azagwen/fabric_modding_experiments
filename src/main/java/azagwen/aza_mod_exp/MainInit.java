package azagwen.aza_mod_exp;

import azagwen.aza_mod_exp.item.Items;
import com.google.common.collect.Maps;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;
import java.util.function.Function;

public class MainInit implements ModInitializer {
    public static final Map<Block, StairsBlock> BLOCKS_TO_STAIRS_MAP = Maps.newHashMap();
    public static final Map<Block, SlabBlock> BLOCK_TO_SLAB_MAP = Maps.newHashMap();
    public static final Map<Block, WallBlock> BLOCK_TO_WALL_MAP = Maps.newHashMap();

    public static Identifier id(String path) {
        return new Identifier("aza_mod_exp", path);
    }

    public <B> void populateBlockToBlock(RecipeManager recipeManager, Map<Block, B> btbMap, Function<BlockItem, Boolean> condition) {
        for (var key : recipeManager.keys().toList()) {
            var recipe = recipeManager.get(key).get();
            if (recipe.getType() == RecipeType.CRAFTING) {
                if (recipe.getOutput().getItem() instanceof BlockItem resultBlockItem && condition.apply(resultBlockItem)) {
                    for (var ingredient : recipe.getIngredients()) {
                        for (var stack : ingredient.getMatchingStacks()) {
                            var block = Registry.BLOCK.get(Registry.ITEM.getId(stack.getItem()));
                            btbMap.put(block, (B) resultBlockItem.getBlock());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            this.populateBlockToBlock(server.getRecipeManager(), BLOCK_TO_SLAB_MAP, (blockItem) -> blockItem.getBlock() instanceof SlabBlock);
            this.populateBlockToBlock(server.getRecipeManager(), BLOCK_TO_WALL_MAP, (blockItem) -> blockItem.getBlock() instanceof WallBlock);
        });
        Items.registerAll();
    }
}
