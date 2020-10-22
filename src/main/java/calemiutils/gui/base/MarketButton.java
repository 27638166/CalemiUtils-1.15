package calemiutils.gui.base;

import calemiutils.config.MarketItemsFile;
import calemiutils.util.helper.StringHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class MarketButton extends ItemStackButton {

    private final List<MarketItemsFile.MarketItem> marketList;
    private final int marketListIndex;

    /**
     * A Market button. Used to select a Market offer.
     * @param marketList The type of market list.
     * @param marketListIndex The index of a market list.
     * @param pressable Called when the button is pressed.
     */
    public MarketButton(List<MarketItemsFile.MarketItem> marketList, int marketListIndex, int x, int y, ItemRenderer itemRender, IPressable pressable) {
        super(x, y, itemRender, pressable);
        this.marketList = marketList;
        this.marketListIndex = marketListIndex;
    }

    public int getMarketListIndex() {
        return marketListIndex;
    }

    @Override
    public ItemStack getRenderedStack() {
        return MarketItemsFile.getStackFromList(marketList, marketListIndex);
    }

    @Override
    public String[] getTooltip() {
        MarketItemsFile.MarketItem marketItem = marketList.get(marketListIndex);

        List<String> list = new ArrayList<>();
        List<ITextComponent> lore = getRenderedStack().getTooltip(Minecraft.getInstance().player, ITooltipFlag.TooltipFlags.NORMAL);

        list.add(marketItem.amount + "x " + getRenderedStack().getDisplayName().getFormattedText());
        list.add("Value " + TextFormatting.GOLD + StringHelper.printCurrency(marketItem.value));

        if (lore.size() > 1) {

            if (Screen.hasShiftDown()) {

                for (ITextComponent component : lore) {
                    list.add(component.getFormattedText());
                }

                list.remove(2);

                StringHelper.removeNullsFromList(list);
                StringHelper.removeCharFromList(list, "Shift", "Ctrl");
            }

            else {
                list.add(ChatFormatting.GRAY + "[" + ChatFormatting.AQUA + "Shift" + ChatFormatting.GRAY + "]" + ChatFormatting.GRAY + " Info");
            }
        }

        return StringHelper.getArrayFromList(list);
    }
}
