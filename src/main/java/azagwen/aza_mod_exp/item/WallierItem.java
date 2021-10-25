package azagwen.aza_mod_exp.item;

import azagwen.aza_mod_exp.MainInit;
import net.minecraft.block.*;
import net.minecraft.block.enums.WallShape;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Map;

public class WallierItem extends Item {

    public WallierItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var world = context.getWorld();
        var pos = context.getBlockPos();
        var player = context.getPlayer();
        var hitBlock = world.getBlockState(pos).getBlock();

        if (MainInit.BLOCK_TO_WALL_MAP.containsKey(hitBlock)) {
            this.convert(world, pos, hitBlock, player, context, MainInit.BLOCK_TO_WALL_MAP);
            return ActionResult.success(world.isClient);
        }
        else if (MainInit.BLOCK_TO_FENCE_MAP.containsKey(hitBlock)) {
            this.convert(world, pos, hitBlock, player, context, MainInit.BLOCK_TO_FENCE_MAP);
            return ActionResult.success(world.isClient);
        }

        return ActionResult.PASS;
    }

    private void convert(World world, BlockPos pos, Block hitBlock, PlayerEntity player, ItemUsageContext context, Map<Block, ? extends Block> map) {
        var wallState = map.get(hitBlock).getDefaultState();
        var wallBlockItem = (BlockItem) wallState.getBlock().asItem();

        world.removeBlock(pos, false);
        wallBlockItem.place(new ItemPlacementContext(player, context.getHand(), context.getStack(), new BlockHitResult(context.getHitPos(), context.getSide(), pos, false)));
    }
}
