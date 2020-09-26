package calemiutils.inventory;

import calemiutils.CUConfig;
import calemiutils.init.InitContainersTypes;
import calemiutils.init.InitItems;
import calemiutils.inventory.base.ContainerBase;
import calemiutils.inventory.base.SlotFilter;
import calemiutils.inventory.base.SlotIInventoryFilter;
import calemiutils.item.ItemCurrency;
import calemiutils.util.helper.CurrencyHelper;
import calemiutils.util.helper.ItemHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerWallet extends ContainerBase {

    public final int selectedSlot;
    private final IInventory stackInv;

    public ContainerWallet (final int windowID, final PlayerInventory playerInventory, IInventory stackInv, int selectedSlot) {
        super(InitContainersTypes.WALLET.get(), windowID, playerInventory);

        isItemContainer = true;
        size = 1;
        this.stackInv = stackInv;

        this.selectedSlot = selectedSlot;

        addPlayerInv(8, 94);
        addPlayerHotbar(8, 152);

        //New Inventory
        addSlot(new SlotIInventoryFilter(stackInv, 0, 17, 42, InitItems.COIN_PENNY.get(), InitItems.COIN_NICKEL.get(), InitItems.COIN_QUARTER.get(), InitItems.COIN_DOLLAR.get()));
    }

    public static ContainerWallet createClientWallet (final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        final int selectedSlot = data.readVarInt();
        return new ContainerWallet(windowId, playerInventory, new Inventory(1), selectedSlot);
    }

    @Override
    public ItemStack slotClick (int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {

        ItemStack returnStack = super.slotClick(slotId, dragType, clickTypeIn, player);
        ItemStack stack = stackInv.getStackInSlot(0);

        if (stack != null && !stack.isEmpty() && stack.getItem() instanceof ItemCurrency) {

            ItemCurrency currency = ((ItemCurrency) stack.getItem());

            int balance = getNBT().getInt("balance");

            int amountToAdd = 0;
            int stacksToRemove = 0;

            for (int i = 0; i < stack.getCount(); i++) {

                if (balance + currency.value <= CUConfig.wallet.walletCurrencyCapacity.get()) {

                    balance += currency.value;

                    amountToAdd += currency.value;
                    stacksToRemove++;
                }
            }

            getNBT().putInt("balance", getNBT().getInt("balance") + amountToAdd);
            stackInv.decrStackSize(0, stacksToRemove);
        }

        return returnStack;
    }

    private CompoundNBT getNBT () {
        return ItemHelper.getNBT(getCurrentWalletStack());
    }

    private ItemStack getCurrentWalletStack () {
        return CurrencyHelper.getCurrentWalletStack(playerInventory.player);
    }

    @Override
    public void onContainerClosed (PlayerEntity player) {
        super.onContainerClosed(player);
    }

    @OnlyIn(Dist.CLIENT)
    public int getSize () {
        return size;
    }
}
