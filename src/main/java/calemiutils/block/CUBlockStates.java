package calemiutils.block;

import net.minecraft.item.DyeColor;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.properties.RedstoneSide;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Direction.Plane;

public class CUBlockStates {
    public static final EnumProperty<DyeColor> COLOR;

    static {
        COLOR = EnumProperty.create("color", DyeColor.class);
    }
}
