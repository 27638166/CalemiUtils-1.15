package calemiutils.command;

import calemiutils.CUReference;
import calemiutils.util.helper.ChatHelper;
import com.google.common.base.Predicates;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockStateArgument;
import net.minecraft.command.impl.TeleportCommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import org.codehaus.plexus.util.cli.Commandline;

import java.awt.*;
import java.util.Collection;
import java.util.EnumSet;

public class CUCommandBase {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {

        dispatcher.register(LiteralArgumentBuilder.<CommandSource>literal("cu")
                .requires(Predicates.alwaysTrue())
                .then(help())
                .then(reload())
                .then(cube())
                .then(circle()));
    }

    private static ArgumentBuilder<CommandSource, ?> help() {

        return Commands.literal("reload").executes(ctx -> {

            String holdBrush = "[Hold Brush]";

            PlayerEntity player = ctx.getSource().asPlayer();

            ChatHelper.printModMessage(TextFormatting.GREEN, "----- Help for " + CUReference.MOD_NAME + " -----", player);
            ChatHelper.printModMessage(TextFormatting.GREEN, "() are optional arguments.", player);
            ChatHelper.printModMessage(TextFormatting.GREEN, " /cu reload - Reloads the MarketItems and MiningUnitCosts files.", player);
            ChatHelper.printModMessage(TextFormatting.GREEN, holdBrush + " /cu cube <color> (block) - Creates a cube of blueprint. <color> Color. (block) the block it replaces.", player);
            ChatHelper.printModMessage(TextFormatting.GREEN, holdBrush + " /cu circle <color> (block) - Creates a circle of blueprint. <color> Color. (block) the block it replaces.", player);
            ChatHelper.printModMessage(TextFormatting.GREEN, holdBrush + " /cu move - Moves the cube selection (Not implemented)", player);
            return 0;
        });
    }

    private static ArgumentBuilder<CommandSource, ?> reload() {

        return Commands.literal("reload").executes(ctx -> {
            return 0;
        });
    }

    private static ArgumentBuilder<CommandSource, ?> cube() {

        return Commands.literal("cube").executes(ctx -> {
            return 0;
        }).then(Commands.argument("color", new ColorArgument())).then(Commands.argument("block", BlockStateArgument.blockState()));
    }

    private static ArgumentBuilder<CommandSource, ?> circle() {

        return Commands.literal("circle").executes(ctx -> {
            return 0;
        }).then(Commands.argument("color", new ColorArgument()));
    }
}
