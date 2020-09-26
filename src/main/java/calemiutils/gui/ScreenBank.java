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

    private void withdraw (TileEntityBank teBank) {

        if (teBank.getStackInSlot(1).getItem() instanceof ItemWallet) {

            CompoundNBT nbt = ItemHelper.getNBT(teBank.getStackInSlot(1));
            int currency = ItemWallet.getBalance(teBank.getStackInSlot(1));

            int amountToAdd = MathHelper.getAmountToAdd(currency, teBank.storedCurrency, CUConfig.wallet.walletCurrencyCapacity.get());

            if (amountToAdd > 0) {
                teBank.addCurrency(-amountToAdd);
                nbt.putInt("balance", currency + amountToAdd);
            }

            else {

                int remainder = MathHelper.getRemainder(currency, teBank.storedCurrency, CUConfig.wallet.walletCurrencyCapacity.get());

                if (remainder > 0) {
                    teBank.addCurrency(-remainder);
                    nbt.putInt("balance", currency + remainder);
                }
            }

            CalemiUtils.network.sendToServer(new PacketBank(teBank.storedCurrency, ItemWallet.getBalance(teBank.getStackInSlot(1)), teBank.getPos()));
        }
    }

    private void deposit (TileEntityBank teBank) {

        if (teBank.getStackInSlot(1).getItem() instanceof ItemWallet) {

            CompoundNBT nbt = ItemHelper.getNBT(teBank.getStackInSlot(1));
            int currency = ItemWallet.getBalance(teBank.getStackInSlot(1));

            int amountToAdd = MathHelper.getAmountToAdd(teBank.storedCurrency, currency, teBank.getMaxCurrency());

            if (amountToAdd > 0) {
                teBank.addCurrency(amountToAdd);
                nbt.putInt("balance", currency - amountToAdd);
            }

            else {

                int remainder = MathHelper.getRemainder(teBank.storedCurrency, currency, teBank.getMaxCurrency());

                if (remainder > 0) {
                    teBank.addCurrency(remainder);
                    nbt.putInt("balance", currency - remainder);
                }
            }

            CalemiUtils.network.sendToServer(new PacketBank(teBank.storedCurrency, ItemWallet.getBalance(teBank.getStackInSlot(1)), teBank.getPos()));
        }
    }

    @Override
    public int getGuiSizeY () {
        return 144;
    }

    @Override
    public void drawGuiForeground (int mouseX, int mouseY) {
        if (!getTileEntity().enable) {
            addInfoIcon(1);
            addInfoIconText(mouseX, mouseY, "Inactive!", "Another Bank is connected in the network!");
        }
    }

    @Override
    public String getGuiTextureName () {
        return "bank";
    }

    @Override
    public void drawGuiBackground (int mouseX, int mouseY) {}
}
