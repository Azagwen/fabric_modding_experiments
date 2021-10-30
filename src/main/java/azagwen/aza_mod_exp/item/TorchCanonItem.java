package azagwen.aza_mod_exp.item;

import azagwen.aza_mod_exp.AzaTags;
import azagwen.aza_mod_exp.entity.ThrownTorchEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class TorchCanonItem extends RangedWeaponItem {

    public TorchCanonItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var heldStack = user.getStackInHand(hand);
        var isCreative = user.getAbilities().creativeMode;

        var projectile = this.getTorchType(heldStack, user);
        if (!projectile.isEmpty() || isCreative) {
            if (projectile.isEmpty()) {
                projectile = new ItemStack(Items.TORCH);
            }

            if (!world.isClient) {
                var torch = (projectile.isIn(AzaTags.ItemTags.TORCHES) ? projectile.getItem() : Items.TORCH);
                var thrownTorchEntity = new ThrownTorchEntity(world, torch, user);
                thrownTorchEntity.setItem(projectile);
                thrownTorchEntity.setProperties(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
                world.spawnEntity(thrownTorchEntity);
            }

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F));
            if (!isCreative) {
                projectile.decrement(1);
                if (projectile.isEmpty()) {
                    user.getInventory().removeOne(projectile);
                }
            }

            heldStack.damage(1, (LivingEntity) user, (player) -> {
                player.sendToolBreakStatus(player.getActiveHand());
            });

            user.incrementStat(Stats.USED.getOrCreateStat(this));

            return TypedActionResult.success(heldStack, world.isClient());
        }
        return TypedActionResult.fail(heldStack);
    }

    public ItemStack getTorchType(ItemStack stack, PlayerEntity player) {
        if (!(stack.getItem() instanceof RangedWeaponItem)) {
            return ItemStack.EMPTY;
        } else {
            var predicate = ((RangedWeaponItem)stack.getItem()).getHeldProjectiles();
            var itemStack = RangedWeaponItem.getHeldProjectile(player, predicate);
            if (!itemStack.isEmpty()) {
                return itemStack;
            } else {
                predicate = ((RangedWeaponItem)stack.getItem()).getProjectiles();

                for(int i = 0; i < player.getInventory().size(); ++i) {
                    ItemStack itemStack2 = player.getInventory().getStack(i);
                    if (predicate.test(itemStack2)) {
                        return itemStack2;
                    }
                }

                return player.getAbilities().creativeMode ? new ItemStack(Items.TORCH) : ItemStack.EMPTY;
            }
        }
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return (stack) -> stack.isIn(AzaTags.ItemTags.TORCHES);
    }

    @Override
    public int getRange() {
        return 30;
    }
}
