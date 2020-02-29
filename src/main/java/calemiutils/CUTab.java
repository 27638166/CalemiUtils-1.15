package calemiutils;

import calemiutils.init.InitItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

class CUTab extends ItemGroup {

    CUTab() {

        super(CUReference.MOD_ID + ".tabMain");
    }

    @Override
    public ItemStack createIcon() {

        ItemStack stack = new ItemStack(InitItems.PENCIL);
        return stack;
    }
}
