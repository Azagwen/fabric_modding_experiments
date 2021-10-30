package azagwen.aza_mod_exp.item;

import azagwen.aza_mod_exp.MainInit;
import azagwen.aza_mod_exp.Utilities;
import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public class SlabifierItem extends Item {
    private final Map<Block, SlabBlock> blockToSlabMap = MainInit.BLOCK_TO_SLAB_MAP; //Too many references to the main map, added this as a quality of life field.

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
        var hitBlock = world.getBlockState(pos).getBlock();
        var stack = player.getStackInHand(context.getHand());

        if (blockToSlabMap.containsKey(hitBlock)) {
            var slabState = blockToSlabMap.get(hitBlock).getDefaultState();
            var type = Utilities.getHitHalf(hitSide, hitPosVec3f, pos, player.isSneaking(), SlabType.BOTTOM, SlabType.TOP);

            stack.damage(1, (LivingEntity) player, (playerx) -> {
                playerx.sendToolBreakStatus(playerx.getActiveHand());
            });

            world.setBlockState(pos, slabState.with(SlabBlock.TYPE, type));
            this.playConversionSounds(world, player, pos, slabState);
            Block.dropStack(world, pos, hitSide, new ItemStack(slabState.getBlock().asItem()));
            return ActionResult.success(world.isClient);
        } else {
            for (var entry : MainInit.BLOCK_TO_STAIRS_MAP.entrySet()) {
                if (hitBlock == entry.getValue()) {
                    var slabState = blockToSlabMap.get(entry.getKey()).getDefaultState();
                    var type = world.getBlockState(pos).get(StairsBlock.HALF) == BlockHalf.BOTTOM ? SlabType.BOTTOM : SlabType.TOP;

                    stack.damage(1, (LivingEntity) player, (playerx) -> {
                        playerx.sendToolBreakStatus(playerx.getActiveHand());
                    });

                    world.setBlockState(pos, slabState.with(SlabBlock.TYPE, type));
                    this.playConversionSounds(world, player, pos, slabState);
                    return ActionResult.success(world.isClient());
                }
            }
        }

        return ActionResult.PASS;
    }

    private void playConversionSounds(World world, PlayerEntity player, BlockPos pos, BlockState state) {
        var soundGroup = state.getSoundGroup();
        world.playSound(player, pos, soundGroup.getPlaceSound(), SoundCategory.BLOCKS, (soundGroup.getVolume() + 1.0F) / 2.0F, soundGroup.getPitch() * 0.8F);
    }
}
