package calemiutils.util.helper;

import calemiutils.tileentity.base.CUItemHandler;
import calemiutils.tileentity.base.TileEntityInventoryBase;
import calemiutils.util.Location;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.Objects;

public class InventoryHelper {

    public static boolean canInsertItem (ItemStack stack, IInventory inventory) {

        for (int i = 0; i < inventory.getSizeInventory(); i++) {

            ItemStack slot = inventory.getStackInSlot(i);

            boolean equalAndNotFull = (ItemStack.areItemsEqual(slot, stack) && slot.getCount() + stack.getCount() < stack.getMaxStackSize());

            if (inventory.isItemValidForSlot(i, stack) && (slot.isEmpty() || equalAndNotFull)) {
                return true;
            }
        }

        return false;
    }

    public static boolean canInsertItem (ItemStack stack, TileEntityInventoryBase tileEntity) {

        for (int i = 0; i < tileEntity.getInventory().getSlots(); i++) {

            ItemStack slot = tileEntity.getInventory().getStackInSlot(i);

            boolean equalAndNotFull = (ItemStack.areItemsEqual(slot, stack) && slot.getCount() + stack.getCount() < stack.getMaxStackSize());

            if (tileEntity.isItemValidForSlot(i, stack) && (slot.isEmpty() || equalAndNotFull)) {
                return true;
            }
        }

        return false;
    }

    public static void insertItem (ItemStack stack, CUItemHandler inventory) {
        insertItem(stack, inventory, 0);
    }

    public static void insertItem (ItemStack stack, IInventory inventory) {
        insertItem(stack, inventory, 0);
    }

    public static void insertItem (ItemStack stack, CUItemHandler inventory, int slotOffset) {

        for (int i = slotOffset; i < inventory.getSlots(); i++) {

            ItemStack slot = inventory.getStackInSlot(i);

            if (ItemStack.areItemsEqual(slot, stack) && (slot.getCount() + stack.getCount() <= stack.getMaxStackSize())) {

                inventory.setStackInSlot(i, new ItemStack(stack.getItem(), slot.getCount() + stack.getCount()));
                return;
            }

            else if (slot.isEmpty()) {
                inventory.setStackInSlot(i, stack);
                return;
            }
        }
    }

    public static void insertItem (ItemStack stack, IInventory inventory, int slotOffset) {

        for (int i = slotOffset; i < inventory.getSizeInventory(); i++) {

            ItemStack slot = inventory.getStackInSlot(i);

            if (ItemStack.areItemsEqual(slot, stack) && (slot.getCount() + stack.getCount() <= stack.getMaxStackSize())) {

                inventory.setInventorySlotContents(i, new ItemStack(stack.getItem(), slot.getCount() + stack.getCount()));
                return;
            }

            else if (slot.isEmpty()) {
                inventory.setInventorySlotContents(i, stack);
                return;
            }
        }
    }

    public static boolean insertHeldItemIntoSlot (PlayerEntity player, Hand hand, Location location, CUItemHandler inventory, int slot, boolean removeStack) {

        ItemStack stack = player.getHeldItem(hand);
        TileEntity te = location.getTileEntity();

        if (inventory.getSlots() > slot) {

            if (!stack.isEmpty() && inventory.getStackInSlot(slot).isEmpty()) {

                inventory.setStackInSlot(slot, stack.copy());

                if (removeStack) {
                    player.setHeldItem(hand, ItemStack.EMPTY);
                }

                return true;
            }
        }

        return false;
    }

    public static void breakInventory (World world, CUItemHandler inventory, Location location) {

        for (int i = 0; i < inventory.getSlots(); i++) {

            ItemStack stack = inventory.getStackInSlot(i);

            if (!stack.isEmpty()) {

                ItemEntity dropEntity = ItemHelper.spawnItem(world, location, stack);

                if (stack.hasTag()) {
                    dropEntity.getItem().setTag(stack.getTag());
                }
            }
        }
    }

    public static void consumeItem (IInventory inventory, int amount, ItemStack... itemStack) {
        consumeItem(0, inventory, amount, false, itemStack);
    }

    public static void consumeItem (CUItemHandler inventory, int amount, ItemStack... itemStack) {
        consumeItem(0, inventory, amount, false, itemStack);
    }

    public static void consumeItem (int slotOffset, CUItemHandler inventory, int amount, boolean useNBT, ItemStack... itemStacks) {

        int amountLeft = amount;

        if (countItems(inventory, useNBT, itemStacks) >= amount) {

            for (int i = slotOffset; i < inventory.getSlots(); i++) {

                if (amountLeft <= 0) {
                    break;
                }

                ItemStack stack = inventory.getStackInSlot(i);

                if (!stack.isEmpty()) {

                    for (ItemStack itemStack : itemStacks) {

                        if (stack.isItemEqual(itemStack)) {

                            if (useNBT && itemStack.hasTag()) {

                                if (!stack.hasTag() || !Objects.equals(stack.getTag(), itemStack.getTag())) {
                                    continue;
                                }
                            }

                            if (amountLeft >= stack.getCount()) {

                                amountLeft -= stack.getCount();
                                inventory.setStackInSlot(i, ItemStack.EMPTY);
                            }

                            else {

                                ItemStack copy = stack.copy();

                                inventory.decrStackSize(i, amountLeft);
                                amountLeft -= copy.getCount();
                            }
                        }
                    }
                }
            }
        }
    }

    public static void consumeItem (int slotOffset, IInventory inventory, int amount, boolean useNBT, ItemStack... itemStacks) {

        int amountLeft = amount;

        if (countItems(inventory, useNBT, itemStacks) >= amount) {

            for (int i = slotOffset; i < inventory.getSizeInventory(); i++) {

                if (amountLeft <= 0) {
                    break;
                }

                ItemStack stack = inventory.getStackInSlot(i);

                if (!stack.isEmpty()) {

                    for (ItemStack itemStack : itemStacks) {

                        if (stack.isItemEqual(itemStack)) {

                            if (useNBT && itemStack.hasTag()) {

                                if (!stack.hasTag() || !Objects.equals(stack.getTag(), itemStack.getTag())) {
                                    continue;
                                }
                            }

                            if (amountLeft >= stack.getCount()) {

                                amountLeft -= stack.getCount();
                                inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                            }

                            else {

                                ItemStack copy = stack.copy();

                                inventory.decrStackSize(i, amountLeft);
                                amountLeft -= copy.getCount();
                            }
                        }
                    }
                }
            }
        }
    }

    public static int countItems (IInventory inventory, boolean useNBT, ItemStack... itemStacks) {

        int count = 0;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {

            ItemStack stack = inventory.getStackInSlot(i);

            for (ItemStack itemStack : itemStacks) {

                if (stack.isItemEqual(itemStack)) {

                    if (useNBT && itemStack.hasTag()) {

                        if (stack.hasTag() && Objects.equals(stack.getTag(), itemStack.getTag())) {
                            count += stack.getCount();
                        }
                    }

                    count += stack.getCount();
                }
            }
        }

        return count;
    }

    public static int countItems (CUItemHandler inventory, boolean useNBT, ItemStack... itemStacks) {

        int count = 0;

        for (int i = 0; i < inventory.getSlots(); i++) {

            ItemStack stack = inventory.getStackInSlot(i);

            for (ItemStack itemStack : itemStacks) {

                if (stack.isItemEqual(itemStack)) {

                    if (useNBT && itemStack.hasTag()) {

                        if (stack.hasTag() && Objects.equals(stack.getTag(), itemStack.getTag())) {
                            count += stack.getCount();
                        }
                    }

                    count += stack.getCount();
                }
            }
        }

        return count;
    }
}
