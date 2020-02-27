package calemiutils.tileentity.base;

import calemiutils.security.ISecurity;
import calemiutils.util.Location;
import calemiutils.util.UnitChatMessage;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nullable;

public abstract class TileEntityBase extends TileEntity implements ITickable {

    public boolean enable;

    protected TileEntityBase(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);

        enable = true;
    }

    public Location getLocation() {

        return new Location(world, pos);
    }

    protected UnitChatMessage getUnitName(PlayerEntity player) {
        return new UnitChatMessage(getLocation().getBlock().getNameTextComponent().getFormattedText(), player);
    }

    public void markForUpdate() {

        markDirty();
        world.markAndNotifyBlock(getPos(), world.getChunkAt(getPos()), getBlockState(), getBlockState(), 0);
        world.addBlockEvent(getPos(), getBlockState().getBlock(), 1, 1);
        world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 0);
        world.notifyNeighborsOfStateChange(getPos(), getBlockState().getBlock());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getPos(), 0, getTileData());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        read(pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {

        CompoundNBT nbt = new CompoundNBT();
        write(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {

        read(tag);
    }

    @Override
    public CompoundNBT getTileData() {

        CompoundNBT nbt = new CompoundNBT();
        write(nbt);
        return nbt;
    }

    @Override
    public void read(CompoundNBT nbt) {

        if (this instanceof ISecurity) {

            ISecurity security = (ISecurity) this;

            security.getSecurityProfile().readFromNBT(nbt);
        }

        if (this instanceof ICurrencyNetworkBank) {

            ICurrencyNetworkBank currency = (ICurrencyNetworkBank) this;

            currency.setCurrency(nbt.getInt("currency"));
        }

        enable = nbt.getBoolean("enable");
        super.read(nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {

        if (this instanceof ISecurity) {

            ISecurity security = (ISecurity) this;

            security.getSecurityProfile().writeToNBT(nbt);
        }

        if (this instanceof ICurrencyNetworkBank) {

            ICurrencyNetworkBank currency = (ICurrencyNetworkBank) this;

            nbt.putInt("currency", currency.getStoredCurrency());
        }

        nbt.putBoolean("enable", enable);
        return super.write(nbt);
    }
}
