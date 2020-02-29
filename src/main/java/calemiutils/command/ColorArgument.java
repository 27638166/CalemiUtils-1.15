package calemiutils.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.DyeColor;

import java.awt.*;

public class ColorArgument implements ArgumentType<DyeColor> {

    @Override
    public DyeColor parse(StringReader reader) throws CommandSyntaxException {

        return DyeColor.valueOf(reader.getString());
    }
}
