package calemiutils.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class UnitChatMessage {

    private final String unitName;
    private final PlayerEntity[] players;

    public UnitChatMessage(String unitName, PlayerEntity... players) {

        this.unitName = unitName;
        this.players = players;
    }

    public void printMessage(TextFormatting format, String message) {

        for (PlayerEntity player : players) {

            StringTextComponent componentString = new StringTextComponent(getUnitName() + (format + message));
            player.sendMessage(componentString);
        }
    }

    private String getUnitName() {

        return "[" + unitName + "] ";
    }
}
