package calemiutils.util.helper;

import calemiutils.util.Location;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.util.Direction;

public class TorchHelper {

    public static boolean canPlaceTorchAt (Location location) {

        if (!location.isBlockValidForPlacing()) {
            return false;
        }

        if (location.getLightValue() > 7) {
            return false;
        }

        if (location.getBlock() instanceof FlowingFluidBlock) {
            return false;
        }

        Location locationDown = new Location(location, Direction.DOWN);

        return locationDown.isFullCube();
    }
}
