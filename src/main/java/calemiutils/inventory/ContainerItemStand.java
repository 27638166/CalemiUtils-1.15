package calemiutils.inventory;

import calemiutils.init.InitContainersTypes;
import calemiutils.inventory.base.ContainerBase;
import calemiutils.tileentity.TileEntityItemStand;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;

public class ContainerItemStand extends ContainerBase {

    public ContainerItemStand (final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowId, playerInventory, (TileEntityItemStand) getTileEntity(playerInventory, data));
    }

    public ContainerItemStand (final int windowId, final PlayerInventory playerInventory, final TileEntityItemStand tileEntity) {
        super(InitContainersTypes.ITEM_STAND.get(), windowId, playerInventory, tileEntity, 8, 41);
        addSlot(new Slot(tileEntity, 0, 80, 18));
    }
}
