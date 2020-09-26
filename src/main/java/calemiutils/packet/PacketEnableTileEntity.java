package calemiutils.packet;

import calemiutils.tileentity.base.TileEntityBase;
import calemiutils.util.Location;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketEnableTileEntity {

    private boolean enable;
    private BlockPos pos;

    public PacketEnableTileEntity () {}

    public PacketEnableTileEntity (boolean enable, BlockPos pos) {
        this.enable = enable;
        this.pos = pos;
    }

    public PacketEnableTileEntity (PacketBuffer buf) {
        enable = buf.readBoolean();
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    public void toBytes (PacketBuffer buf) {
        buf.writeBoolean(enable);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    public void handle (Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {

            ServerPlayerEntity player = ctx.get().getSender();

            if (player != null) {

                Location location = new Location(player.world, pos);

                if (location.getTileEntity() instanceof TileEntityBase) {

                    ((TileEntityBase) location.getTileEntity()).enable = enable;
                }
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
