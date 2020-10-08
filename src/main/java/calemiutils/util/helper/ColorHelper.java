package calemiutils.util.helper;

import net.minecraft.item.DyeColor;

public class ColorHelper {

    public static DyeColor getColorFromString(String name) {

        if (name != null) {

            for (DyeColor color : DyeColor.values()) {

                if (name.equalsIgnoreCase(color.getName())) {
                    return color;
                }
            }
        }

        return DyeColor.BLUE;
    }
}