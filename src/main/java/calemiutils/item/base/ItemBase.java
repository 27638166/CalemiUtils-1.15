package calemiutils.item.base;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBase extends Item {

    private boolean hasEffect = false;

    public ItemBase(String name, Item.Properties properties) {

        super(properties);
        setRegistryName(name);
    }

    public ItemBase setEffect() {
        hasEffect = true;
        return this;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {

        return hasEffect;
    }
}
