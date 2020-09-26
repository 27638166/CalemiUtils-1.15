package calemiutils.packet;

import calemiutils.tileentity.TileEntityTradingPost;
import calemiutils.util.Location;
import calemiutils.util.helper.ItemHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketTradingPost {

    private String command;
    private BlockPos pos;
    private String stack;
    private boolean buyMode;
    private int amount;
    private int price;

    public PacketTradingPost () {}

    /**
     * Use this constructor to sync the current mode.
     */
    public PacketTradingPost (String command, BlockPos pos, boolean buyMode) {
        this(command, pos, "", buyMode, 0, 0);
    }

    public PacketTradingPost (String command, BlockPos pos, String stack, boolean buyMode, int amount, int price) {
        this.command = command;
        this.pos = pos;
        this.stack = stack;
        this.buyMode = buyMode;
        this.amount = amount;
        this.price = price;
    }

    /**
     * Use this constructor to sync the stack for sale.
     */
    public PacketTradingPost (String command, BlockPos pos, String stack) {
        this(command, pos, stack, false, 0, 0);
    }

    /**
     * Use this constructor to sync the options
     */
    public PacketTradingPost (String command, BlockPos pos, int amount, int price) {
        this(command, pos, "", false, amount, price);
    }

    public PacketTradingPost (PacketBuffer buf) {
        this.command = buf.readString(11).trim();
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.stack = buf.readString();
        this.buyMode = buf.readBoolean();
        this.amount = buf.readInt();
        this.price = buf.readInt();
    }

    public void toBytes (PacketBuffer buf) {
        buf.writeString(command, 11);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeString(stack);
        buf.writeBoolean(buyMode);
        buf.writeInt(amount);
        buf.writeInt(price);
    }

    public void handle (Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {

            ServerPlayerEntity player = ctx.get().getSender();

            if (player != null) {

                Location location = new Location(player.world, pos);

                if (location.getTileEntity() instanceof TileEntityTradingPost) {

                    TileEntityTradingPost tPost = (TileEntityTradingPost) location.getTileEntity();

                    if (command.equalsIgnoreCase("syncmode")) {
                        tPost.buyMode = this.buyMode;
                    }

                    else if (command.equalsIgnoreCase("syncstack")) {
                        tPost.setStackForSale(ItemHelper.getStackFromString(stack));
                    }

                    else if (command.equalsIgnoreCase("syncoptions")) {
                        tPost.amountForSale = this.amount;
                        tPost.salePrice = this.price;
                    }
                }
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
