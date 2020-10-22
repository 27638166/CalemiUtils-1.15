package calemiutils.util.helper;

import calemiutils.CUReference;
import calemiutils.util.UnitChatMessage;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ChatHelper {

    /**
     * Used to print the main mod's messages.
     * @param format The color or style of the message.
     * @param message The message.
     * @param players The Players that will receive the message.
     */
    public static void printModMessage (TextFormatting format, String message, Entity... players) {
        UnitChatMessage unitMessage = new UnitChatMessage(CUReference.MOD_NAME, players);
        unitMessage.printMessage(format, message);
    }

    /**
     * Used to send a message to everyone.
     * @param message The message.
     */
    public static void broadcastMessage (World world, String message) {
        world.getServer().getPlayerList().sendMessage(new StringTextComponent(message));
    }
}
