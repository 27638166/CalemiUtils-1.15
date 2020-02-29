package calemiutils.packet;

import calemiutils.item.ItemPencil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketPencilSetColor {

    private int colorId;
    private boolean offHand;

    public PacketPencilSetColor() {}

    public PacketPencilSetColor(int colorId, boolean offHand) {
        this.colorId = colorId;
        this.offHand = offHand;
    }

    public PacketPencilSetColor(PacketBuffer buf) {
        colorId = buf.readInt();
        offHand = buf.readBoolean();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(colorId);
        buf.writeBoolean(offHand);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {

            Hand hand = Hand.MAIN_HAND;
            if (offHand) hand = Hand.OFF_HAND;

            ServerPlayerEntity player = ctx.get().getSender();

            final ItemStack stack = player.getHeldItem(hand);

            if (!stack.isEmpty()) {

                ItemPencil pencil = (ItemPencil) player.getHeldItemMainhand().getItem();
                pencil.setColorById(player.getHeldItemMainhand(), colorId);
            }

        });

        ctx.get().setPacketHandled(true);
    }
}
