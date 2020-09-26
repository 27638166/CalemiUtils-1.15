package calemiutils.item.base;

import calemiutils.CalemiUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBase extends Item {

    private boolean hasEffect = false;

    public ItemBase () {
        this(new Item.Properties().group(CalemiUtils.TAB));
    }

    public ItemBase (Item.Properties properties) {
        super(properties);
    }

    public ItemBase setEffect () {
        hasEffect = true;
        return this;
    }

    @Override
    public boolean hasEffect (ItemStack stack) {

        return hasEffect;
    }
}
