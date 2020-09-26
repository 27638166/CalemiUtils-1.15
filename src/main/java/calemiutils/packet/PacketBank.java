package calemiutils.packet;

import calemiutils.tileentity.TileEntityBank;
import calemiutils.util.Location;
import calemiutils.util.helper.ItemHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketBank {

    private int bankCurrency;
    private int walletCurrency;
    private BlockPos pos;

    public PacketBank () {}

    public PacketBank (int bankCurrency, int walletCurrency, BlockPos pos) {
        this.bankCurrency = bankCurrency;
        this.walletCurrency = walletCurrency;
        this.pos = pos;
    }

    public PacketBank (PacketBuffer buf) {
        this.bankCurrency = buf.readInt();
        this.walletCurrency = buf.readInt();
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    public void toBytes (PacketBuffer buf) {
        buf.writeInt(bankCurrency);
        buf.writeInt(walletCurrency);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    public void handle (Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {

            ServerPlayerEntity player = ctx.get().getSender();

            if (player != null) {

                Location location = new Location(player.world, pos);

                if (location.getTileEntity() instanceof TileEntityBank) {

                    TileEntityBank teBank = (TileEntityBank) location.getTileEntity();
                    CompoundNBT walletNBT = ItemHelper.getNBT(teBank.getStackInSlot(1));

                    teBank.storedCurrency = bankCurrency;
                    walletNBT.putInt("balance", walletCurrency);
                }
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
