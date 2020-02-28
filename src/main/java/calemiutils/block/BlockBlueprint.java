package calemiutils.block;

import calemiutils.block.base.BlockColoredBase;
import calemiutils.config.CUConfig;
import calemiutils.init.InitBlocks;
import calemiutils.util.Location;
import calemiutils.util.UnitChatMessage;
import calemiutils.util.VeinScan;
import calemiutils.util.helper.InventoryHelper;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.SoundHelper;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeBlockState;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;

public class BlockBlueprint extends BlockColoredBase {

    public BlockBlueprint() {

        super("blueprint", Block.Properties.create(Material.GLASS).sound(SoundType.STONE).hardnessAndResistance(0.1F).harvestLevel(0).func_226896_b_().variableOpacity());
    }

    public IForgeBlockState getStateByPrefix(String prefix) {

        DyeColor dye = DyeColor.BLUE;

        for (DyeColor dyes : DyeColor.values()) {
            if (dyes.getName().startsWith(prefix)) {
                dye = dyes;
            }
        }

        return getDefaultState().with(COLOR, dye);
    }

    @Override
    public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player) {

        Location location = new Location(world, pos);
        ItemStack currentStack = player.getHeldItemMainhand();
        UnitChatMessage message = new UnitChatMessage("Blueprint", player);

        VeinScan scan = new VeinScan(location, location.getBlockState());
        scan.startScan();

        if (!currentStack.isEmpty() && currentStack.getItem() != Item.getItemFromBlock(this)) {

            if (currentStack.getItem() instanceof BlockItem) {
                replaceAllBlocks(world, player, state, location, currentStack, scan, message);
            }

            /*else if (currentStack.getItem() instanceof ItemBuildersKit) {
                replaceAllBlocks(world, player, location, ((ItemBuildersKit) currentStack.getItem()).getBlockType(currentStack), scan, message);
            }*/
        }

        else if (!world.isRemote && player.isCrouching() && currentStack.isEmpty()) {

            if (scan.buffer.size() >= CUConfig.blockScans.veinScanMaxSize.get()) {
                message.printMessage(TextFormatting.GREEN, "There are " + CUConfig.blockScans.veinScanMaxSize + "+ connected Blueprints");
            }

            else message.printMessage(TextFormatting.GREEN, "There are " + ItemHelper.countByStacks(scan.buffer.size()) + " connected Blueprints");
        }
    }

    private void replaceAllBlocks(World world, PlayerEntity player, BlockState blueprintState, Location location, ItemStack currentStack, VeinScan scan, UnitChatMessage message) {

        IForgeBlockState state = Block.getBlockFromItem(currentStack.getItem()).getDefaultState();

        if (canPlaceBlockInBlueprint(state)) {

            if (player.isCrouching()) {
                replaceBlock(location, player, state);
                InventoryHelper.consumeItem(player.inventory, 1, true, currentStack);
                SoundHelper.playBlockPlaceSound(world, player, Block.getBlockFromItem(currentStack.getItem()).getDefaultState(), location);
            }

            else {

                int itemCount = InventoryHelper.countItems(player.inventory, true, false, currentStack);

                if (itemCount >= scan.buffer.size()) {

                    int amountToConsume = 0;

                    for (Location nextLocation : scan.buffer) {

                        amountToConsume++;
                        replaceBlock(nextLocation, player, state);
                    }

                    if (amountToConsume > 0) {

                        SoundHelper.playDing(player.world, player);
                        SoundHelper.playBlockPlaceSound(world, player, Block.getBlockFromItem(currentStack.getItem()).getDefaultState(), location);

                        if (!world.isRemote) message.printMessage(TextFormatting.GREEN, "Placed " + ItemHelper.countByStacks(amountToConsume));
                        InventoryHelper.consumeItem(player.inventory, amountToConsume, true, currentStack);
                    }
                }

                else if (!world.isRemote) {

                    message.printMessage(TextFormatting.RED, "You don't have enough blocks of that type!");
                    message.printMessage(TextFormatting.RED, "You're missing: " + ItemHelper.countByStacks((scan.buffer.size() - itemCount)));
                }
            }
        }
    }

    private void replaceBlock(Location location, PlayerEntity player, IForgeBlockState state) {

        if (!player.world.isRemote) {

            location.setBlock(state, player);
            ForgeEventFactory.onBlockPlace(player, BlockSnapshot.getBlockSnapshot(player.world, location.getBlockPos()), Direction.UP);
        }
    }

    private boolean canPlaceBlockInBlueprint(IForgeBlockState forgeState) {

        BlockState state = forgeState.getBlockState();

        if (state.getBlock() instanceof ChestBlock) return false;

        return state.getMaterial() != Material.PLANTS && state.getMaterial() != Material.AIR && state.getMaterial() != Material.ANVIL && state.getMaterial() != Material.CACTUS && state.getMaterial() != Material.CAKE && state.getMaterial() != Material.CARPET && state.getMaterial() != Material.PORTAL;
    }

    public int getIdFromState(IForgeBlockState state) {

        if (state.getBlockState().getBlock() instanceof BlockBlueprint) {
            return (state.getBlockState().get(COLOR)).getId();
        }

        return 0;
    }

    public IForgeBlockState getStateFromId(int id) {

        return getDefaultState().with(COLOR, DyeColor.byId(id));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float func_220080_a(BlockState p_220080_1_, IBlockReader p_220080_2_, BlockPos p_220080_3_) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    @Override
    public boolean func_229869_c_(BlockState p_229869_1_, IBlockReader p_229869_2_, BlockPos p_229869_3_) {
        return false;
    }

    @Override
    public boolean isNormalCube(BlockState p_220081_1_, IBlockReader p_220081_2_, BlockPos p_220081_3_) {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isSideInvisible(BlockState centerBlockState, BlockState otherStateBlock, Direction dir) {
        return (otherStateBlock.getBlock() == this && centerBlockState.get(COLOR).getId() == otherStateBlock.get(COLOR).getId()) || super.isSideInvisible(centerBlockState, otherStateBlock, dir);
    }

    @Override
    public boolean canEntitySpawn(BlockState p_220067_1_, IBlockReader p_220067_2_, BlockPos p_220067_3_, EntityType<?> p_220067_4_) {
        return false;
    }
}
