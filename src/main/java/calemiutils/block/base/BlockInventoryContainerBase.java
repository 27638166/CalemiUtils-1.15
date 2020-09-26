package calemiutils.block.base;

import calemiutils.CUConfig;
import calemiutils.security.ISecurity;
import calemiutils.util.Location;
import calemiutils.util.helper.InventoryHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * The base class for Blocks that have Inventories.
 */
public abstract class BlockInventoryContainerBase extends BlockContainerBase {

    /**
     * @param properties The specific properties for the Block. (Creative Tab, hardness, material, etc.)
     */
    public BlockInventoryContainerBase (Properties properties) {
        super(properties);
    }

    /**
     * Drops all contents when the Block is broken or replaced.
     */
    @Override
    public void onReplaced (BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {

        if (state.getBlock() != newState.getBlock()) {

            Location location = new Location(world, pos);
            TileEntity te = location.getTileEntity();

            if (te instanceof IInventory) {

                IInventory inv = (IInventory) te;
                InventoryHelper.breakInventory(world, inv, location);
            }

            super.onReplaced(state, world, pos, newState, isMoving);
        }
    }

    /**
     * This method functions the same as onBlockActivated().
     * Opens the gui of the Block.
     */
    public ActionResultType func_225533_a_ (BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {

        //Prevents client side.
        if (world.isRemote) {
            return ActionResultType.SUCCESS;
        }

        Location location = new Location(world, pos);
        TileEntity tileEntity = location.getTileEntity();

        if (player instanceof ServerPlayerEntity && tileEntity instanceof INamedContainerProvider) {

            //If it has security, then check if the player is the owner;
            if (tileEntity instanceof ISecurity) {

                ISecurity security = (ISecurity) tileEntity;

                if (security.getSecurityProfile().isOwner(player.getName().getFormattedText()) || player.isCreative() || !CUConfig.misc.useSecurity.get()) {
                    NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, pos);
                }
            }

            else NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, pos);
        }

        return ActionResultType.SUCCESS;
    }
}
