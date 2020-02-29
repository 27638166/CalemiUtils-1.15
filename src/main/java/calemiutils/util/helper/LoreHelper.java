package calemiutils.util.helper;

import calemiutils.config.CUConfig;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextComponentUtils;

import java.util.List;

public class LoreHelper {

    public static void addBlankLine(List<ITextComponent> tooltip) {

        tooltip.add(new StringTextComponent(""));
    }

    public static void addInformationLore(List<ITextComponent> tooltip, String lore) {

        if (CUConfig.tooltips.showInfoOnTooltips.get()) {

            if (Screen.hasShiftDown()) {
                tooltip.add(new StringTextComponent(ChatFormatting.GRAY + "" + ChatFormatting.ITALIC + lore));
            }

            else tooltip.add(new StringTextComponent(getPlateText("Shift", ChatFormatting.AQUA) + " Info"));
        }
    }

    public static void addControlsLore(List<ITextComponent> tooltip, String lore, LoreHelper.Type type) {

        addControlsLore(tooltip, lore, type, false);
    }

    public static void addControlsLore(List<ITextComponent> tooltip, String lore, LoreHelper.Type type, boolean isFirst) {

        if (CUConfig.tooltips.showControlsOnTooltips.get()) {

            if (Screen.hasControlDown()) {

                addActionLore(tooltip, lore, type);
            }

            else if (isFirst) tooltip.add(new StringTextComponent(getPlateText("Ctrl", ChatFormatting.AQUA) + ChatFormatting.GRAY + " Controls"));
        }
    }

    public static void addCurrencyLore(List<ITextComponent> tooltip, int currentCurrency) {

        addCurrencyLore(tooltip, currentCurrency, 0);
    }

    public static void addCurrencyLore(List<ITextComponent> tooltip, int currentCurrency, int maxCurrency) {

        tooltip.add(new StringTextComponent(ChatFormatting.GRAY + "Currency: " + ChatFormatting.GOLD + StringHelper.printCurrency(currentCurrency) + (maxCurrency != 0 ? (" / " + StringHelper.printCurrency(maxCurrency)) : "")));
    }

    private static void addActionLore(List<ITextComponent> tooltip, String lore, LoreHelper.Type type) {

        tooltip.add(new StringTextComponent(getPlateText(type.getName(), ChatFormatting.YELLOW) + ChatFormatting.GRAY + " " + lore));
    }

    private static String getPlateText(String text, ChatFormatting format) {

        return ChatFormatting.GRAY + "[" + format + text + ChatFormatting.GRAY + "]";
    }

    public enum Type {
        USE("Use"),
        USE_OPEN_HAND("Use-Open-Hand"),
        USE_WRENCH("Use-Wrench"),
        USE_WALLET("Use-Wallet"),
        USE_BOOK("Use-Book"),
        SNEAK_USE("Sneak-Use"),
        SNEAK_USE_BLUEPRINT("Sneak-Use-Blueprint"),
        SNEAK_BREAK_BLOCK("Sneak-Break-Block"),
        RELEASE_USE("Release Use"),
        LEFT_CLICK_BLOCK("Left-Click-Block"),
        SNEAK_LEFT_CLICK_BLOCK("Sneak-Left-Click-Block"),
        LEFT_CLICK_BLUEPRINT("Left-Click-Blueprint");

        private final String name;

        Type(String name) {

            this.name = name;
        }

        String getName() {

            return name;
        }
    }
}

