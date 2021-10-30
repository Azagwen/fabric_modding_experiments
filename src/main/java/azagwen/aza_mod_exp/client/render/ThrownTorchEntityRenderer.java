package azagwen.aza_mod_exp.client.render;

import azagwen.aza_mod_exp.entity.ThrownTorchEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class ThrownTorchEntityRenderer <T extends Entity & FlyingItemEntity> extends EntityRenderer<T> {
    private final BlockRenderManager manager;
    private final float scale;
    private final boolean lit;

    public ThrownTorchEntityRenderer(EntityRendererFactory.Context context, float f, boolean bl) {
        super(context);
        this.manager = MinecraftClient.getInstance().getBlockRenderManager();
        this.scale = f;
        this.lit = bl;
    }

    public ThrownTorchEntityRenderer(EntityRendererFactory.Context context) {
        this(context, 1.0F, false);
    }

    protected int getBlockLight(T entity, BlockPos pos) {
        return this.lit ? 15 : super.getBlockLight(entity, pos);
    }

    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (entity.age >= 2){
            if (entity instanceof ThrownTorchEntity torchEntity) {
                var pos = torchEntity.getBlockPos();
                var world = torchEntity.getEntityWorld();

                if (torchEntity.getTorchType() instanceof WallStandingBlockItem torchItem) {
                    var state = torchItem.getBlock().getDefaultState();
                    var renderLayer = RenderLayers.getEntityBlockLayer(state, true);
                    var vertexConsumer = vertexConsumers.getBuffer(renderLayer);

                    matrices.push();
                    this.manager.getModelRenderer().render(world, this.manager.getModel(state), state, pos, matrices, vertexConsumer, false, new Random(), state.getRenderingSeed(pos), OverlayTexture.DEFAULT_UV);
                    matrices.pop();

                    super.render(entity, yaw, tickDelta, matrices, vertexConsumers, LightmapTextureManager.MAX_LIGHT_COORDINATE);
                }
            }
        }
    }

    public Identifier getTexture(Entity entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}