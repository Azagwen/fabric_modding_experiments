package azagwen.aza_mod_exp.item;

import azagwen.aza_mod_exp.MainInit;
import azagwen.aza_mod_exp.Utilities;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;

public class SlabifierItem extends Item {

    public SlabifierItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var world = context.getWorld();
        var pos = context.getBlockPos();
        var player = context.getPlayer();
        var hitSide = context.getSide();
        var hitPosVec3f = context.getHitPos();

        for (var entry : MainInit.BLOCK_TO_SLAB_MAP.entrySet()) {
            var slabState = entry.getValue().getDefaultState();
            var block = entry.getKey();
            var hitState = world.getBlockState(pos);

            if (hitState.getBlock() == block && hitState.getBlock() != slabState.getBlock()) {
                var type = Utilities.getHitHalf(hitSide, hitPosVec3f, pos, player.isSneaking(), SlabType.BOTTOM, SlabType.TOP);

                world.setBlockState(pos, slabState.with(SlabBlock.TYPE, type));
                world.playSound(player, pos, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.BLOCKS, 1.0F, 1.0F);
                Block.dropStack(world, pos, hitSide, new ItemStack(slabState.getBlock().asItem()));
                return ActionResult.success(world.isClient);
            }
        }
        return ActionResult.PASS;
    }
}
