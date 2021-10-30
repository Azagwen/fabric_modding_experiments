package azagwen.aza_mod_exp;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;

public class AzaTags {

    private static Tag<Item> register(String id) {
        return TagRegistry.item(MainInit.id(id));
    }

    public static class ItemTags {
        public static final Tag<Item> TORCHES = register("torches");
    }
}
