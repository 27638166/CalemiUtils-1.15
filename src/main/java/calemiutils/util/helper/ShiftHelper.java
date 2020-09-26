package calemiutils.util.helper;

import net.minecraft.client.gui.screen.Screen;

public class ShiftHelper {

    public static int getShiftCtrlInt (int defaultInt, int shiftInt, int ctrlInt, int bothInt) {

        int i = defaultInt;

        boolean s = Screen.hasShiftDown();
        boolean c = Screen.hasControlDown();

        if (s) i = shiftInt;
        if (c) i = ctrlInt;
        if (s && c) i = bothInt;

        return i;
    }
}
