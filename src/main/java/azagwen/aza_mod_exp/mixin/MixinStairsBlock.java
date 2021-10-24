package azagwen.aza_mod_exp.mixin;

import azagwen.aza_mod_exp.MainInit;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StairsBlock.class)
public class MixinStairsBlock {
    private final StairsBlock self = (StairsBlock) (Object) this;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(BlockState blockState, AbstractBlock.Settings settings, CallbackInfo ci) {
        MainInit.BLOCKS_TO_STAIRS_MAP.put(blockState.getBlock(), self);
    }
}
