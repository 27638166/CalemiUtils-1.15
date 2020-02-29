package calemiutils.util;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class UnitChatMessage {

    private final String unitName;
    private final Entity[] players;

    public UnitChatMessage(String unitName, Entity... players) {

        this.unitName = unitName;
        this.players = players;
    }

    public void printMessage(TextFormatting format, String message) {

        for (Entity player : players) {

            StringTextComponent componentString = new StringTextComponent(getUnitName() + (format + message));
            player.sendMessage(componentString);
        }
    }

    private String getUnitName() {

        return "[" + unitName + "] ";
    }
}
