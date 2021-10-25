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
        var hitBlock = world.getBlockState(pos).getBlock();

        if (MainInit.BLOCK_TO_STAIRS_MAP.containsKey(hitBlock)) {
            var stairState = MainInit.BLOCK_TO_STAIRS_MAP.get(hitBlock).getDefaultState();
            var facing = hitSide.getAxis() == Direction.Axis.Y ? player.getHorizontalFacing() : hitSide.getOpposite();
            var half = Utilities.getHitHalf(hitSide, hitPosVec3f, pos, player.isSneaking(), BlockHalf.BOTTOM, BlockHalf.TOP);
            var soundGroup = stairState.getSoundGroup();

            world.setBlockState(pos, stairState.with(StairsBlock.FACING, facing).with(StairsBlock.HALF, half));
            world.playSound(player, pos, soundGroup.getPlaceSound(), SoundCategory.BLOCKS, (soundGroup.getVolume() + 1.0F) / 2.0F, soundGroup.getPitch() * 0.8F);
            return ActionResult.success(world.isClient);
        }

        return ActionResult.PASS;
    }
}
