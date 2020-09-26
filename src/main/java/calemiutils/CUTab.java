package calemiutils;

import calemiutils.init.InitItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

class CUTab extends ItemGroup {

    CUTab () {
        super(CUReference.MOD_ID + ".tabMain");
    }

    @Override
    public ItemStack createIcon () {
        return new ItemStack(InitItems.PENCIL.get());
    }
}
