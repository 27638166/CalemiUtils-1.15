package calemiutils.tileentity.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.INameable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileEntityInventoryBase extends TileEntityBase implements ISidedInventory, ITileEntityGuiHandler, INamedContainerProvider, INameable {

    public final Slot[] containerSlots = new Slot[getSizeInventory()];
    private final IItemHandlerModifiable items = createHandler();
    public NonNullList<ItemStack> slots = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
    protected int numPlayersUsing;
    private LazyOptional<IItemHandlerModifiable> itemHandler = LazyOptional.of(() -> items);

    private int[] inputSlots, sideInputSlots, extractSlots;

    public TileEntityInventoryBase (TileEntityType<?> tileEntityType) {
        super(tileEntityType);

        for (int i = 0; i < slots.size(); i++) {
            containerSlots[i] = new Slot(this, i, 0, 0);
        }
    }

    public abstract ITextComponent getDisplayName ();

    protected void setExtractSlots (int... extractSlots) {
        this.extractSlots = extractSlots;
    }

    protected void setInputSlots (int... inputSlots) {
        this.inputSlots = inputSlots;
    }

    protected void setSideInputSlots (int... sideInputSlots) {
        this.sideInputSlots = sideInputSlots;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability (@Nonnull Capability<T> cap, @Nullable Direction side) {

        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    private IItemHandlerModifiable createHandler () {
        return new InvWrapper(this);
    }

    @Nullable
    @Override
    public Container createMenu (int id, PlayerInventory playerInv, PlayerEntity player) {
        return getTileContainer(id, playerInv);
    }

    @Override
    public int[] getSlotsForFace (Direction side) {

        int[] slots = new int[getSizeInventory()];

        for (int i = 0; i < slots.length; i++) {
            slots[i] = i;
        }

        return slots;
    }

    @Override
    public boolean canInsertItem (int index, ItemStack itemStackIn, @Nullable Direction direction) {

        if (direction != null) {

            int dir = direction.getIndex();

            if (direction == Direction.UP && inputSlots != null) {
                for (int id : inputSlots) {
                    if (id == index) {
                        return true;
                    }
                }
            }

            if (dir > 1 && sideInputSlots != null) {
                for (int id : sideInputSlots) {
                    if (id == index) return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean canExtractItem (int slot, ItemStack itemStackOut, @Nullable Direction direction) {

        if (direction == Direction.DOWN && extractSlots != null) {
            for (int id : extractSlots) {
                if (id == slot) return true;
            }
        }

        return false;
    }

    @Override
    public int getSizeInventory () {
        return 0;
    }

    @Override
    public boolean isEmpty () {

        for (ItemStack stack : slots) {
            if (!stack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getStackInSlot (int index) {
        return slots.get(index);
    }

    @Override
    public ItemStack decrStackSize (int index, int count) {

        ItemStack itemstack;

        if (this.slots.get(index).getCount() <= count) {
            itemstack = this.slots.get(index);
            this.slots.set(index, ItemStack.EMPTY);
        }

        else {
            itemstack = this.slots.get(index).split(count);
            if (slots.get(index) == ItemStack.EMPTY) {
                this.slots.set(index, ItemStack.EMPTY);
            }
        }

        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot (int index) {
        ItemStack copy = getStackInSlot(index).copy();
        setInventorySlotContents(index, ItemStack.EMPTY);
        return copy;
    }

    @Override
    public void setInventorySlotContents (int index, ItemStack stack) {
        this.slots.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {

            decrStackSize(index, stack.getCount() - this.getInventoryStackLimit());
        }
    }

    @Override
    public int getInventoryStackLimit () {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer (PlayerEntity player) {
        return true;
    }

    @Override
    public void openInventory (PlayerEntity player) {

        if (!player.isSpectator()) {

            if (numPlayersUsing < 0) {
                numPlayersUsing = 0;
            }

            ++numPlayersUsing;

            markForUpdate();
        }
    }

    @Override
    public void closeInventory (PlayerEntity player) {

        if (!player.isSpectator()) {
            --numPlayersUsing;
            markForUpdate();
        }
    }

    @Override
    public boolean isItemValidForSlot (int index, ItemStack stack) {
        return containerSlots[index] != null && containerSlots[index].isItemValid(stack);
    }

    @Override
    public void remove () {
        super.remove();

        if (itemHandler != null) {
            itemHandler.invalidate();
        }
    }

    @Override
    public boolean receiveClientEvent (int id, int type) {

        if (id == 1) {
            numPlayersUsing = type;
            return true;
        }

        return super.receiveClientEvent(id, type);
    }

    @Override
    public void updateContainingBlockInfo () {
        super.updateContainingBlockInfo();

        if (itemHandler != null) {
            itemHandler.invalidate();
            itemHandler = null;
        }
    }

    @Override
    public void clear () {
        for (int i = 0; i < getSizeInventory(); i++) {
            slots.set(i, ItemStack.EMPTY);
        }
    }

    @Override
    public void read (CompoundNBT nbt) {
        super.read(nbt);
        this.slots = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, slots);
    }

    @Override
    public CompoundNBT write (CompoundNBT nbt) {
        ItemStackHelper.saveAllItems(nbt, slots);
        return super.write(nbt);
    }
}
