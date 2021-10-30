package azagwen.aza_mod_exp.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class ThrownTorchEntity extends ThrownItemEntity {
    private Item torchType;

    public ThrownTorchEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }



    public ThrownTorchEntity(World world, Item torchType, LivingEntity player) {
        super(AzaEntityTypes.THROWN_TORCH, player, world);
        this.torchType = torchType;
    }

    @Override
    protected Item getDefaultItem() {
        return Items.TORCH;
    }

    private void setTorchType(Item torchType) {
        this.torchType = torchType;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (!this.world.isClient) {
            this.world.sendEntityStatus(this, (byte)3);

            var torch = (BlockItem) this.torchType;
            if (this.getOwner() instanceof ServerPlayerEntity player) {
                var ctx = new ItemPlacementContext(player, player.getActiveHand(), new ItemStack(this.torchType), blockHitResult);

                if (torch.place(ctx) == ActionResult.FAIL) {
                    this.dropItem(torch);
                }
            }

            this.discard();
        }
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        var torch = new ItemStack(this.torchType);
        if (!torch.isEmpty()) {
            nbt.put("TorchType", torch.writeNbt(new NbtCompound()));
        }
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        var torchType = ItemStack.fromNbt(nbt.getCompound("TorchType"));
        this.setTorchType(torchType.getItem());
    }
}
