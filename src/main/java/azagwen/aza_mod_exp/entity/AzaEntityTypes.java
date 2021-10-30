package azagwen.aza_mod_exp.entity;

import azagwen.aza_mod_exp.MainInit;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class AzaEntityTypes {

    public static final EntityType<ThrownTorchEntity> THROWN_TORCH = Registry.register(
            Registry.ENTITY_TYPE, MainInit.id("thrown_torch"),
            FabricEntityTypeBuilder.<ThrownTorchEntity>create(SpawnGroup.MISC, ThrownTorchEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .trackRangeBlocks(4)
                    .trackedUpdateRate(10)
                    .build()
    );

}
