package azagwen.aza_mod_exp.item;

import azagwen.aza_mod_exp.MainInit;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;

public class WallierItem extends Item {

    public WallierItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var world = context.getWorld();
        var pos = context.getBlockPos();
        var player = context.getPlayer();

        for (var entry : MainInit.BLOCK_TO_WALL_MAP.entrySet()) {
            var wallState = entry.getValue().getDefaultState();
            var block = entry.getKey();
            var hitState = world.getBlockState(pos);

            if (hitState.getBlock() == block && hitState.getBlock() != wallState.getBlock()) {
                world.setBlockState(pos, wallState);
                world.updateListeners(pos, block.getDefaultState(), wallState, 8);
                world.playSound(player, pos, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return ActionResult.success(world.isClient);
            }
        }
        return ActionResult.PASS;
    }
}
