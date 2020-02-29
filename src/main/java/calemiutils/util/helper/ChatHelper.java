package calemiutils.util.helper;

import calemiutils.CUReference;
import calemiutils.util.UnitChatMessage;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextFormatting;

public class ChatHelper {

    public static void printModMessage(TextFormatting format, String message, Entity... players) {

        UnitChatMessage unitMessage = new UnitChatMessage(CUReference.MOD_NAME, players);
        unitMessage.printMessage(format, message);
    }
}
