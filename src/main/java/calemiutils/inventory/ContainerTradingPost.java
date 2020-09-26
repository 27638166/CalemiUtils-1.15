package calemiutils.inventory;

import calemiutils.init.InitContainersTypes;
import calemiutils.inventory.base.ContainerBase;
import calemiutils.tileentity.TileEntityTradingPost;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class ContainerTradingPost extends ContainerBase {

    public ContainerTradingPost (final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowId, playerInventory, (TileEntityTradingPost) getTileEntity(playerInventory, data));
    }

    public ContainerTradingPost (final int windowId, final PlayerInventory playerInventory, final TileEntityTradingPost tileEntity) {
        super(InitContainersTypes.TRADING_POST.get(), windowId, playerInventory, tileEntity, 8, 141);
        addTileEntityStorageInv(tileEntity, 0, 8, 83, 3);
    }
}