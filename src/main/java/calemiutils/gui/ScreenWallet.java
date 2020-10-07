package calemiutils.gui;

import calemiutils.CUConfig;
import calemiutils.CalemiUtils;
import calemiutils.gui.base.ButtonRect;
import calemiutils.gui.base.ContainerScreenBase;
import calemiutils.init.InitItems;
import calemiutils.inventory.ContainerWallet;
import calemiutils.item.ItemCurrency;
import calemiutils.item.ItemWallet;
import calemiutils.packet.PacketWallet;
import calemiutils.util.helper.*;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class ScreenWallet extends ContainerScreenBase<ContainerWallet> {

    public ScreenWallet (Container container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, new StringTextComponent("Wallet"));
    }

    /**
     * Gets the current Wallet Stack, returns empty if missing and closes the screen.
     */
    private ItemStack getCurrentWalletStack () {

        ItemStack walletStack = CurrencyHelper.getCurrentWalletStack(player);

        if (!walletStack.isEmpty()) {
            return walletStack;
        }

        else {
            player.closeScreen();
            return ItemStack.EMPTY;
        }
    }

    @Override
    protected void init () {
        super.init();

        for (int index = 0; index < 4; index++) {

            int id = index;

            addButton(new ButtonRect(getScreenX() + 146, getScreenY() + 15 + (index * 18), 16, "+", (btn) -> addMoney(id)));
        }
    }

    /**
     * Called when a "+" button is pressed.
     * Adds money to the Player from the Wallet.
     */
    private void addMoney (int id) {

        ItemStack walletStack = getCurrentWalletStack();

        //Checks if there is a current Wallet.
        if (!walletStack.isEmpty()) {

            ItemWallet walletItem = (ItemWallet) walletStack.getItem();

            int price = ((ItemCurrency) InitItems.COIN_PENNY.get()).value;
            if (id == 1) price = ((ItemCurrency) InitItems.COIN_NICKEL.get()).value;
            else if (id == 2) price = ((ItemCurrency) InitItems.COIN_QUARTER.get()).value;
            else if (id == 3) price = ((ItemCurrency) InitItems.COIN_DOLLAR.get()).value;

            int multiplier = ShiftHelper.getShiftCtrlInt(1, 16, 64, 9 * 64);
            price *= multiplier;

            //If the Wallet's balance can afford the requested amount, give it to the player and sync the current balance.
            if (ItemWallet.getBalance(walletStack) >= price) {

                CalemiUtils.network.sendToServer(new PacketWallet(id, multiplier));
                CompoundNBT nbt = ItemHelper.getNBT(walletStack);
                nbt.putInt("balance", nbt.getInt("balance") - price);
            }
        }
    }

    @Override
    public void drawGuiForeground (int mouseX, int mouseY) {

        GL11.glDisable(GL11.GL_LIGHTING);
        addInfoIcon(0);
        addInfoHoveringText(mouseX, mouseY, "Button Click Info", "Shift: 16, Ctrl: 64, Shift + Ctrl: 64 * 9");
    }

    @Override
    public void drawGuiBackground (int mouseX, int mouseY) {

        ScreenHelper.drawItemStack(itemRenderer, new ItemStack(InitItems.COIN_PENNY.get()), getScreenX() + 127, getScreenY() + 15);
        ScreenHelper.drawItemStack(itemRenderer, new ItemStack(InitItems.COIN_NICKEL.get()), getScreenX() + 127, getScreenY() + 33);
        ScreenHelper.drawItemStack(itemRenderer, new ItemStack(InitItems.COIN_QUARTER.get()), getScreenX() + 127, getScreenY() + 51);
        ScreenHelper.drawItemStack(itemRenderer, new ItemStack(InitItems.COIN_DOLLAR.get()), getScreenX() + 127, getScreenY() + 69);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor4f(1, 1, 1, 1);

        ItemStack stack = getCurrentWalletStack();

        if (!stack.isEmpty()) {
            ScreenHelper.drawCenteredString(StringHelper.printCommas(ItemHelper.getNBT(stack).getInt("balance")), getScreenX() + getGuiSizeX() / 2 - 16, getScreenY() + 42, 0, TEXT_COLOR_GRAY);
            ScreenHelper.drawCenteredString(CUConfig.economy.currencyName.get(), getScreenX() + getGuiSizeX() / 2 - 16, getScreenY() + 51, 0, TEXT_COLOR_GRAY);
        }
    }

    @Override
    public String getGuiTextureName () {
        return "wallet";
    }
}
