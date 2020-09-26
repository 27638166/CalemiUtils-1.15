package calemiutils.item.base;

import calemiutils.init.InitItems;
import calemiutils.item.ItemPencil;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemPencilColored implements IItemColor {

    @Override
    public int getColor (ItemStack stack, int tintIndex) {

        ItemPencil pencil = (ItemPencil) InitItems.PENCIL.get();

        if (tintIndex == 1) {
            int colorMeta = pencil.getColorId(stack);
            DyeColor dye = DyeColor.byId(colorMeta);

            return dye.getColorValue();
        }

        return 0xFFFFFF;
    }
}
