package calemiutils.packet;

import calemiutils.tileentity.TileEntityTradingPost;
import calemiutils.util.Location;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.LogHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketTradingPost {

    private String command;
    private BlockPos pos;
    private int stackStrSize, nbtStrSize;
    private String stack, nbt;
    private boolean buyMode;
    private int amount;
    private int price;

    public PacketTradingPost () {}

    /**
     * Use this constructor to sync the current mode.
     */
    public PacketTradingPost (String command, BlockPos pos, boolean buyMode) {
        this(command, pos, 0, 0, "", "", buyMode, 0, 0);
    }

    public PacketTradingPost (String command, BlockPos pos, int stackStrSize, int nbtStrSize, String stack, String nbt, boolean buyMode, int amount, int price) {
        this.command = command;
        this.pos = pos;
        this.stackStrSize = stackStrSize;
        this.nbtStrSize = nbtStrSize;
        this.stack = stack;
        this.nbt = nbt;
        this.buyMode = buyMode;
        this.amount = amount;
        this.price = price;
    }

    /**
     * Use this constructor to sync the stack for sale.
     */
    public PacketTradingPost (String command, BlockPos pos, String stack, String nbt) {
        this(command, pos, stack.length(), nbt.length(), stack, nbt, false, 0, 0);
    }

    /**
     * Use this constructor to sync the options
     */
    public PacketTradingPost (String command, BlockPos pos, int amount, int price) {
        this(command, pos, 0, 0, "", "", false, amount, price);
    }

    public PacketTradingPost (PacketBuffer buf) {
        this.command = buf.readString(11).trim();
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.stackStrSize = buf.readInt();
        this.nbtStrSize = buf.readInt();
        this.stack = buf.readString(stackStrSize);
        this.nbt = buf.readString(nbtStrSize);
        this.buyMode = buf.readBoolean();
        this.amount = buf.readInt();
        this.price = buf.readInt();
    }

    public void toBytes (PacketBuffer buf) {
        buf.writeString(command, 11);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeInt(stackStrSize);
        buf.writeInt(nbtStrSize);
        buf.writeString(stack, stackStrSize);
        buf.writeString(nbt, nbtStrSize);
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

                        ItemStack stackForSale = ItemHelper.getStackFromString(stack);

                        if (!nbt.isEmpty()) {
                            ItemHelper.attachNBTFromString(stackForSale, nbt);
                        }

                        tPost.setStackForSale(stackForSale);
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
