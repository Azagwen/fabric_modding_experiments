package azagwen.aza_mod_exp.item;

import azagwen.aza_mod_exp.MainInit;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class AzaItems {
    public static final Item STAIRIFIER = new StairifierItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxDamage(256));
    public static final Item SLABIFIER = new SlabifierItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxDamage(256));
    public static final Item WALLIFIER = new WallifierierItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxDamage(256));
    public static final Item TORCH_CANON = new TorchCanonItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxDamage(256));

    private static void registerItem(String name, Item item) {
        Registry.register(Registry.ITEM, MainInit.id(name), item);
    }

    public static void registerAll() {
        registerItem("stairifier", STAIRIFIER);
        registerItem("slabifier", SLABIFIER);
        registerItem("wallifier", WALLIFIER);
        registerItem("torch_canon", TORCH_CANON);
    }
}
