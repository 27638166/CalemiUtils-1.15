package calemiutils.util.helper;

import calemiutils.CUConfig;
import calemiutils.item.ItemWallet;
import calemiutils.tileentity.base.ICurrencyNetworkBank;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CurrencyHelper {

    public static ItemStack getCurrentWalletStack (PlayerEntity player) {

        if (player.getHeldItemMainhand().getItem() instanceof ItemWallet) {
            return player.getHeldItemMainhand();
        }

        if (player.getHeldItemOffhand().getItem() instanceof ItemWallet) {
            return player.getHeldItemOffhand();
        }

        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {

            ItemStack stack = player.inventory.getStackInSlot(i);

            if (stack.getItem() instanceof ItemWallet) {
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }

    private static void addWallet (List<ItemStack> walletList, ItemStack stackToAdd) {

        for (ItemStack stackInList : walletList) {

            if (!ItemStack.areItemStacksEqual(stackToAdd, stackToAdd)) {
                walletList.add(stackToAdd);
            }
        }
    }

    public static boolean canFitAddedCurrencyToNetwork (ICurrencyNetworkBank network, int addAmount) {

        return network.getStoredCurrency() + addAmount <= network.getMaxCurrency();
    }

    @SuppressWarnings("SameReturnValue")
    public static boolean canFitAddedCurrencyToWallet (ItemStack walletStack, int addAmount) {

        if (walletStack.getItem() instanceof ItemWallet) {
            return ItemWallet.getBalance(walletStack) + addAmount <= CUConfig.wallet.walletCurrencyCapacity.get();
        }

        return false;
    }
}
