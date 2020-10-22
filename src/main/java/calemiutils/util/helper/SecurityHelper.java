package calemiutils.util.helper;

import calemiutils.config.CUConfig;
import calemiutils.security.ISecurity;
import calemiutils.tileentity.base.TileEntityBase;
import calemiutils.util.Location;
import calemiutils.util.UnitChatMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;

public class SecurityHelper {

    public static boolean openSecuredBlock (Location location, PlayerEntity player, boolean printError) {

        TileEntity tileEntity = location.getTileEntity();

        if (tileEntity instanceof ISecurity) {

            ISecurity security = (ISecurity) tileEntity;

            if (security.getSecurityProfile().isOwner(player.getName().getFormattedText()) || player.isCreative() || !CUConfig.misc.useSecurity.get()) {
                return true;
            }

            else if (printError) printErrorMessage(location, player);

            return false;
        }

        return true;
    }

    public static void printErrorMessage (Location location, PlayerEntity player) {
        UnitChatMessage message = new UnitChatMessage(location.getBlock().getNameTextComponent().getFormattedText(), player);
        if (!player.world.isRemote) message.printMessage(TextFormatting.RED, "This unit doesn't belong to you!");
    }
}
