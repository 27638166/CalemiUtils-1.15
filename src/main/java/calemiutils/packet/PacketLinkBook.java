package calemiutils.packet;

import calemiutils.item.ItemLinkBookLocation;
import calemiutils.util.Location;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketLinkBook {

    private String command;
    private boolean offHand;
    private String name;
    private BlockPos pos;
    private int dim;

    public PacketLinkBook () {}

    /**
     * Use this constructor to name the Link Book
     */
    public PacketLinkBook (String command, Hand hand, String name) {
        this(command, hand, name, new BlockPos(0, 0, 0), 0);
    }

    public PacketLinkBook (String command, Hand hand, String name, BlockPos pos, int dim) {
        this.command = command;
        this.offHand = (hand != Hand.MAIN_HAND);
        this.name = name;
        this.pos = pos;
        this.dim = dim;
    }

    /**
     * Use this constructor to bind a Location to the Link Book
     */
    public PacketLinkBook (String command, Hand hand, BlockPos pos) {
        this(command, hand, "", pos, 0);
    }

    /**
     * Use this constructor to reset the Lick Book's data.
     */
    public PacketLinkBook (String command, Hand hand) {
        this(command, hand, "", new BlockPos(0, 0, 0), 0);
    }

    /**
     * Use this constructor to teleport to the Link Book's linked location.
     */
    public PacketLinkBook (String command, Hand hand, BlockPos pos, int dim) {
        this(command, hand, "", pos, dim);
    }

    public PacketLinkBook (PacketBuffer buf) {
        command = buf.readString(8).trim();
        offHand = buf.readBoolean();
        name = buf.readString(32);
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        dim = buf.readInt();
    }

    public void toBytes (PacketBuffer buf) {
        buf.writeString(command, 8);
        buf.writeBoolean(offHand);
        buf.writeString(name, 32);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeInt(dim);
    }

    public void handle (Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {

            Hand hand = Hand.MAIN_HAND;
            if (offHand) hand = Hand.OFF_HAND;

            ServerPlayerEntity player = ctx.get().getSender();

            if (player != null) {

                final ItemStack stack = player.getHeldItem(hand);
                Location location = new Location(player.world, pos);

                if (command.equalsIgnoreCase("teleport")) {
                    ItemLinkBookLocation.teleport(player.world, player, location, dim);
                }

                else if (stack.getItem() instanceof ItemLinkBookLocation) {

                    if (command.equalsIgnoreCase("name")) {
                        ItemLinkBookLocation.bindName(stack, name);
                    }

                    else if (command.equalsIgnoreCase("reset")) {
                        ItemLinkBookLocation.resetLocation(stack, player);
                    }

                    else if (command.equalsIgnoreCase("bind")) {
                        ItemLinkBookLocation.bindLocation(stack, player, location, true);
                    }
                }
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
