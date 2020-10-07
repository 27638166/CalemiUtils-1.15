package calemiutils.gui;

import calemiutils.CUConfig;
import calemiutils.CalemiUtils;
import calemiutils.gui.base.ButtonRect;
import calemiutils.gui.base.ContainerScreenBase;
import calemiutils.inventory.ContainerBank;
import calemiutils.item.ItemWallet;
import calemiutils.packet.PacketBank;
import calemiutils.tileentity.TileEntityBank;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.MathHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenBank extends ContainerScreenBase<ContainerBank> {

    public ScreenBank (Container container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
    }

    @Override
    protected void init () {
        super.init();

        TileEntityBank teBank = (TileEntityBank) getTileEntity();

        addButton(new ButtonRect(getScreenX() + (getGuiSizeX() / 2 - 25) + 30, getScreenY() + 40, 50, "Withdraw", (btn) -> withdraw(teBank)));
        addButton(new ButtonRect(getScreenX() + (getGuiSizeX() / 2 - 25) - 30, getScreenY() + 40, 50, "Deposit", (btn) -> deposit(teBank)));
    }

    /**
     * Called when the withdraw button is pressed.
     * Handles withdrawals from the Bank.
     */
    private void withdraw (TileEntityBank teBank) {

        //Checks if there is a Wallet in the Wallet slot.
        if (teBank.getInventory().getStackInSlot(1).getItem() instanceof ItemWallet) {

            CompoundNBT nbt = ItemHelper.getNBT(teBank.getInventory().getStackInSlot(1));
            int walletBalance = ItemWallet.getBalance(teBank.getInventory().getStackInSlot(1));

            int amountToAdd = MathHelper.getAmountToAdd(walletBalance, teBank.storedCurrency, CUConfig.wallet.walletCurrencyCapacity.get());

            //If the Wallet can fit the currency, add it and subtract it from the Bank.
            if (amountToAdd > 0) {
                teBank.addCurrency(-amountToAdd);
                nbt.putInt("balance", walletBalance + amountToAdd);
            }

            //If the Wallet can't fit all the money, get how much is needed to fill it, then only used that much.
            else {

                int amountToFill = MathHelper.getAmountToFill(walletBalance, teBank.storedCurrency, CUConfig.wallet.walletCurrencyCapacity.get());

                if (amountToFill > 0) {
                    teBank.addCurrency(-amountToFill);
                    nbt.putInt("balance", walletBalance + amountToFill);
                }
            }

            //Syncs the Bank's currency to the server.
            CalemiUtils.network.sendToServer(new PacketBank(teBank.storedCurrency, ItemWallet.getBalance(teBank.getInventory().getStackInSlot(1)), teBank.getPos()));
        }
    }

    /**
     * Called when the deposit button is pressed.
     * Handles deposits from the Bank.
     */
    private void deposit (TileEntityBank teBank) {

        //Checks if there is a Wallet in the Wallet slot.
        if (teBank.getInventory().getStackInSlot(1).getItem() instanceof ItemWallet) {

            CompoundNBT nbt = ItemHelper.getNBT(teBank.getInventory().getStackInSlot(1));
            int currency = ItemWallet.getBalance(teBank.getInventory().getStackInSlot(1));

            int amountToAdd = MathHelper.getAmountToAdd(teBank.storedCurrency, currency, teBank.getMaxCurrency());

            //If the Bank can fit the currency, add it and subtract it from the Wallet.
            if (amountToAdd > 0) {
                teBank.addCurrency(amountToAdd);
                nbt.putInt("balance", currency - amountToAdd);
            }

            //If the Bank can't fit all the money, get how much is needed to fill it, then only used that much.
            else {

                int remainder = MathHelper.getAmountToFill(teBank.storedCurrency, currency, teBank.getMaxCurrency());

                if (remainder > 0) {
                    teBank.addCurrency(remainder);
                    nbt.putInt("balance", currency - remainder);
                }
            }

            CalemiUtils.network.sendToServer(new PacketBank(teBank.storedCurrency, ItemWallet.getBalance(teBank.getInventory().getStackInSlot(1)), teBank.getPos()));
        }
    }

    @Override
    public void drawGuiBackground (int mouseX, int mouseY) {}

    /**
     * Handles rendering a tab that shows if the Bank is inactive.
     */
    @Override
    public void drawGuiForeground (int mouseX, int mouseY) {

        if (!getTileEntity().enable) {
            addInfoIcon(1);
            addInfoHoveringText(mouseX, mouseY, "Inactive!", "Another Bank is connected in the network!");
        }
    }

    @Override
    public int getGuiSizeY () {
        return 144;
    }

    @Override
    public String getGuiTextureName () {
        return "bank";
    }
}
