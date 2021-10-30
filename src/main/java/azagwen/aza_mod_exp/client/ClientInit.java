package azagwen.aza_mod_exp.client;

import azagwen.aza_mod_exp.MainInit;
import azagwen.aza_mod_exp.client.render.ThrownTorchEntityRenderer;
import azagwen.aza_mod_exp.entity.AzaEntityTypes;
import azagwen.aza_mod_exp.item.AzaItems;
import azagwen.aza_mod_exp.item.TorchCanonItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.block.RedstoneBlock;
import net.minecraft.client.item.UnclampedModelPredicateProvider;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.jetbrains.annotations.Nullable;


@Environment(EnvType.CLIENT)
public class ClientInit implements ClientModInitializer {

    public static class SimpleAnimUpdater {
        public double value;
        private long lastUpdateTime;

        SimpleAnimUpdater() {
        }

        public boolean shouldUpdate(long time) {
            return this.lastUpdateTime != time;
        }

        public void update(long time, double value) {
            this.lastUpdateTime = time;
            this.value = value;
        }
    }

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(AzaEntityTypes.THROWN_TORCH, ThrownTorchEntityRenderer::new);

        FabricModelPredicateProviderRegistry.register(AzaItems.TORCH_CANON, MainInit.id("torch_type"), new UnclampedModelPredicateProvider() {
            private final SimpleAnimUpdater animUpdater = new SimpleAnimUpdater();

            @Override
            public float unclampedCall(ItemStack itemStack, @Nullable ClientWorld clientWorld, @Nullable LivingEntity livingEntity, int i) {
                if (livingEntity == null) {
                    return 0.0F;
                } else {
                    if (clientWorld == null && livingEntity.world instanceof ClientWorld world) {
                        clientWorld = world;
                    }

                    if (livingEntity instanceof PlayerEntity player) {
                        var time = clientWorld.getTime();
                        var item = itemStack.getItem() instanceof TorchCanonItem canonItem ? canonItem : null;
                        var isHoldingItem = player.getMainHandStack() == itemStack || player.getOffHandStack() == itemStack;

                        if (this.animUpdater.shouldUpdate(time) && item != null && isHoldingItem) {
                            var type = 0.0F;
                            var torch = item.getTorchType(itemStack, player).getItem();

                            if (torch == Items.TORCH) {
                                type = 0.001F;
                            } else if (torch == Items.SOUL_TORCH) {
                                type = 0.002F;
                            } else if (torch == Items.REDSTONE_TORCH) {
                                type = 0.003F;
                            }

                            this.animUpdater.update(time, type);
                        } else if (this.animUpdater.shouldUpdate(time) && !isHoldingItem) {
                            this.animUpdater.update(time, 0.0F);
                        }

                        return (float) this.animUpdater.value;
                    } else {
                        return 0.0F;
                    }
                }
            }
        });
    }
}
