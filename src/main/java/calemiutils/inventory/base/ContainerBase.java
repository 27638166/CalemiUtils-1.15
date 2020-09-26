package calemiutils.inventory.base;

import calemiutils.tileentity.base.TileEntityInventoryBase;
import calemiutils.tileentity.base.TileEntityUpgradable;
import calemiutils.util.FunctionalIntReferenceHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;

import java.util.Objects;

public class ContainerBase extends Container {

    protected final PlayerInventory playerInventory;
    public TileEntityInventoryBase tileEntity;
    public FunctionalIntReferenceHolder currentProgress;
    protected int size;

    protected boolean isItemContainer;

    protected ContainerBase (ContainerType<?> type, int windowId, PlayerInventory playerInventory, TileEntityInventoryBase tileEntity, int x, int y) {
        this(type, windowId, playerInventory, tileEntity);
        addPlayerInv(x, y);
        addPlayerHotbar(x, y + 58);

        if (tileEntity instanceof TileEntityUpgradable) {

            TileEntityUpgradable tileEntityProgress = (TileEntityUpgradable) tileEntity;
            trackInt(currentProgress = new FunctionalIntReferenceHolder(tileEntityProgress::getCurrentProgress, tileEntityProgress::setCurrentProgress));
        }
    }

    protected ContainerBase (ContainerType<?> type, int windowId, PlayerInventory playerInventory, TileEntityInventoryBase tileEntity) {
        super(type, windowId);
        this.playerInventory = playerInventory;
        this.tileEntity = tileEntity;
    }

    protected void addPlayerInv (int x, int y) {
        addStorageInv(playerInventory, 9, x, y, 3);
    }

    protected void addPlayerHotbar (int x, int y) {
        addStorageInv(playerInventory, 0, x, y, 1);
    }

    private void addStorageInv (IInventory inv, int idOffset, int x, int y, int height) {

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inv, j + i * 9 + idOffset, x + (j * 18), y + (i * 18)));
            }
        }
    }

    protected ContainerBase (ContainerType<?> type, int windowId, PlayerInventory playerInventory) {
        super(type, windowId);
        this.playerInventory = playerInventory;
    }

    protected static TileEntityInventoryBase getTileEntity (final PlayerInventory playerInventory, final PacketBuffer data) {

        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");

        final TileEntity tileAtPos = playerInventory.player.world.getTileEntity(data.readBlockPos());

        if (tileAtPos instanceof TileEntityInventoryBase) {
            return (TileEntityInventoryBase) tileAtPos;
        }

        throw new IllegalStateException("Tile entity is not correct!" + tileAtPos);
    }

    protected void addTileEntityStorageInv (IInventory inv, int idOffset, int x, int y, int height) {

        int id = idOffset;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inv, id, x + (j * 18), y + (i * 18)));
                id++;
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot (PlayerEntity playerIn, int index) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {

            ItemStack itemStack1 = slot.getStack();
            itemstack = itemStack1.copy();

            //Does tile entity have slots
            if (getTileEntitySlotAmount() > 0) {

                //inventory -> tile entity
                if (index <= 35) {

                    if (mergeIfPossible(slot, itemStack1, itemstack, 36, 36 + getTileEntitySlotAmount())) {

                        if (mergeInvHotbarIfPossible(slot, itemStack1, itemstack, index)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }

                //tile entity -> inventory
                else {

                    if (mergeIfPossible(slot, itemStack1, itemstack, 0, 35)) {
                        return ItemStack.EMPTY;
                    }

                    slot.onSlotChange(itemStack1, itemstack);
                }
            }

            else {

                if (mergeInvHotbarIfPossible(slot, itemStack1, itemstack, index)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemStack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            }

            else {
                slot.onSlotChanged();
            }

            if (itemStack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerInventory.player, itemStack1);
        }

        return itemstack;
    }

    private int getTileEntitySlotAmount () {
        return isItemContainer ? size : tileEntity.getSizeInventory();
    }

    private boolean mergeIfPossible (Slot slot, ItemStack is, ItemStack is2, int id, int maxId) {

        if (!this.mergeItemStack(is, id, maxId, false)) {
            return true;
        }

        slot.onSlotChange(is, is2);
        return false;
    }

    private boolean mergeInvHotbarIfPossible (Slot slot, ItemStack is, ItemStack is2, int id) {

        if (id < 27) {

            if (mergeIfPossible(slot, is, is2, 27, 35)) {
                return true;
            }
        }

        else {

            if (mergeIfPossible(slot, is, is2, 0, 26)) {
                return true;
            }
        }

        slot.onSlotChange(is, is2);
        return false;
    }

    @Override
    public boolean canInteractWith (PlayerEntity playerIn) {
        return true;
    }
}

