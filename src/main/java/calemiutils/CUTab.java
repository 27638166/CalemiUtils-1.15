package calemiutils;

import calemiutils.init.InitItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CUTab extends ItemGroup {

    public CUTab () {
        super(CUReference.MOD_ID + ".tabMain");
    }

    @Override
    public ItemStack createIcon () {
        return new ItemStack(InitItems.PENCIL.get());
    }
}
