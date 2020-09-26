package calemiutils.util.helper;

import calemiutils.CUReference;
import calemiutils.util.UnitChatMessage;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextFormatting;

public class ChatHelper {

    public static void printModMessage (TextFormatting format, String message, Entity... players) {

        UnitChatMessage unitMessage = new UnitChatMessage(CUReference.MOD_NAME, players);
        unitMessage.printMessage(format, message);
    }
}
