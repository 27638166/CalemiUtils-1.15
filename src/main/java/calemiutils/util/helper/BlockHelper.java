package calemiutils.util.helper;

import calemiutils.util.Location;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockHelper {

    public static void placeBlockInArray (World world, BlockPos pos, PlayerEntity player, Block block) {

        Location location = new Location(world, pos);

        ItemStack currentStack = player.getHeldItemMainhand();

        if (currentStack.getItem() == Item.getItemFromBlock(block)) {// || currentStack.getItem() instanceof ItemBuildersKit) {

            int face = MathHelper.floor((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

            Direction dir = null;

            if (face == 0) dir = Direction.SOUTH;
            if (face == 1) dir = Direction.WEST;
            if (face == 2) dir = Direction.NORTH;
            if (face == 3) dir = Direction.EAST;

            for (int i = 0; i < 64; i++) {

                Location nextLocation = new Location(location, dir, i);

                if (nextLocation.getBlock() != null && nextLocation.getBlock() != block && !nextLocation.isBlockValidForPlacing()) {
                    break;
                }

                if (nextLocation.isBlockValidForPlacing()) {

                    nextLocation.setBlock(block);

                    InventoryHelper.consumeItem(player.inventory, 1, true, new ItemStack(block));

                    break;
                }
            }
        }
    }

    public static void placeBlockInArray (World world, BlockPos pos, PlayerEntity player, Block block, Direction dir) {

        Location location = new Location(world, pos);

        ItemStack currentStack = player.getHeldItemMainhand();

        if (currentStack.getItem() == Item.getItemFromBlock(block)) {// || currentStack.getItem() instanceof ItemBuildersKit) {

            for (int i = 0; i < 64; i++) {

                Location nextLocation = new Location(location, dir, i);

                if (nextLocation.getBlock() != null && nextLocation.getBlock() != block && !nextLocation.isBlockValidForPlacing()) {
                    break;
                }

                if (nextLocation.isBlockValidForPlacing()) {

                    nextLocation.setBlock(block);
                    SoundHelper.playBlockPlaceSound(world, player, nextLocation.getBlockState(), nextLocation);

                    InventoryHelper.consumeItem(player.inventory, 1, true, new ItemStack(block));

                    break;
                }
            }
        }
    }
}
