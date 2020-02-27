package calemiutils.item;

import calemiutils.item.base.ItemBase;
import calemiutils.util.helper.StringHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCurrency extends ItemBase {

    public final int value;

    public ItemCurrency(String name, Properties properties, int value) {
        super("coin_" + name, properties);
        this.value = value;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltipList, ITooltipFlag advanced) {
        tooltipList.add(new StringTextComponent(ChatFormatting.GRAY + "Value (1): " + ChatFormatting.GOLD + StringHelper.printCurrency(value)));

        if (stack.getCount() > 1) {
            tooltipList.add(new StringTextComponent(ChatFormatting.GRAY + "Value (" + stack.getCount() + "): " + ChatFormatting.GOLD + StringHelper.printCurrency(value * stack.getCount())));
        }
    }
}
