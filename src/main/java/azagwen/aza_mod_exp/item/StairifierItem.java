package azagwen.aza_mod_exp.item;

import azagwen.aza_mod_exp.MainInit;
import azagwen.aza_mod_exp.Utilities;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;

public class StairifierItem extends Item {

    public StairifierItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var world = context.getWorld();
        var pos = context.getBlockPos();
        var player = context.getPlayer();
        var hitSide = context.getSide();
        var hitPosVec3f = context.getHitPos();

        for (var entry : MainInit.BLOCKS_TO_STAIRS_MAP.entrySet()) {
            var stairState = entry.getValue().getDefaultState();
            var block = entry.getKey();
            var hitState = world.getBlockState(pos);

            if (hitState.getBlock() == block && hitState.getBlock() != stairState.getBlock()) {
                var facing = hitSide.getAxis() == Direction.Axis.Y ? player.getHorizontalFacing() : hitSide.getOpposite();
                var half = Utilities.getHitHalf(hitSide, hitPosVec3f, pos, player.isSneaking(), BlockHalf.BOTTOM, BlockHalf.TOP);

                world.setBlockState(pos, stairState.with(StairsBlock.FACING, facing).with(StairsBlock.HALF, half));
                world.updateListeners(pos, block.getDefaultState(), stairState, 8);
                world.playSound(player, pos, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return ActionResult.success(world.isClient);
            }
        }
        return ActionResult.PASS;
    }
}
