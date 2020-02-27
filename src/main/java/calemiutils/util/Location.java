package calemiutils.util;

import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.SoundHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.extensions.IForgeBlockState;

import java.util.List;

public class Location {

    public final World world;
    public int x, y, z;

    private BlockPos blockPos;

    public Location(World world, int x, int y, int z) {

        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;

        blockPos = new BlockPos(x, y, z);
    }

    public Location(World world, BlockPos pos) {

        this(world, pos.getX(), pos.getY(), pos.getZ());
    }

    public Location(TileEntity tileEntity) {

        this(tileEntity.getWorld(), tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ());
    }

    public Location(Entity entity) {

        this(entity.world, entity.getPosition().getX(), entity.getPosition().getY(), entity.getPosition().getZ());
    }

    public Location(Location location, Direction dir, int distance) {

        this.world = location.world;
        this.x = location.x + (dir.getXOffset() * distance);
        this.y = location.y + (dir.getYOffset() * distance);
        this.z = location.z + (dir.getZOffset() * distance);

        blockPos = new BlockPos(x, y, z);
    }

    public Location(Location location, Direction dir) {

        this(location, dir, 1);
    }

    public static Location readFromNBT(World world, CompoundNBT nbt) {

        int x = nbt.getInt("locX");
        int y = nbt.getInt("locY");
        int z = nbt.getInt("locZ");

        Location loc = new Location(world, x, y, z);

        if (!loc.isZero()) {
            return loc;
        }

        return null;
    }

    public Location translate(Direction dir, int distance) {

        this.x += (dir.getXOffset() * distance);
        this.y += (dir.getYOffset() * distance);
        this.z += (dir.getZOffset() * distance);
        blockPos = new BlockPos(x, y, z);
        return this;
    }

    public Location translate(Location location) {

        this.x += location.x;
        this.y += location.y;
        this.z += location.z;
        blockPos = new BlockPos(x, y, z);
        return this;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Location) {

            Location newLoc = (Location) obj;

            return world == newLoc.world && x == newLoc.x && y == newLoc.y && z == newLoc.z;
        }

        return super.equals(obj);
    }

    public double getDistance(Location location) {

        int dx = x - location.x;
        int dy = y - location.y;
        int dz = z - location.z;

        return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    public BlockPos getBlockPos() {

        return blockPos;
    }

    public IForgeBlockState getBlockState() {

        if (getBlockPos() == null) {
            return null;
        }

        return world.getBlockState(getBlockPos());
    }

    public Block getBlock() {

        if (getBlockState() == null) {
            return null;
        }

        return getBlockState().getBlockState().getBlock();
    }

    public void setBlock(Block block) {

        world.setBlockState(getBlockPos(), block.getDefaultState());
    }

    public void setBlock(IForgeBlockState state) {

        world.setBlockState(getBlockPos(), state.getBlockState().getBlock().getDefaultState());
        world.setBlockState(getBlockPos(), state.getBlockState());
    }

    public void breakBlock(PlayerEntity player) {
        SoundHelper.playBlockPlaceSound(world, player, getBlockState(), this);
        if (!world.isRemote && (!player.isCreative())) ItemHelper.spawnItems(world, this, getDrops());
        if (!world.isRemote) setBlockToAir();
    }

    public Material getBlockMaterial() {
        return getBlock().getMaterial(getBlockState().getBlockState());
    }

    public List<ItemStack> getDrops() {

        return getBlock().getDrops(getBlockState().getBlockState(), (ServerWorld) world, getBlockPos(), null);
    }

    public TileEntity getTileEntity() {

        return world.getTileEntity(getBlockPos());
    }

    public IInventory getIInventory() {

        if (getTileEntity() != null && getTileEntity() instanceof IInventory) {

            return (IInventory) getTileEntity();
        }

        return null;
    }

    public int getLightValue() {

        return world.getLight(getBlockPos());
    }

    public void setBlock(Block block, PlayerEntity placer) {

        setBlock(block);
        block.onBlockPlacedBy(world, getBlockPos(), block.getDefaultState(), placer, new ItemStack(block));
    }

    public void setBlock(IForgeBlockState state, PlayerEntity placer) {

        world.setBlockState(getBlockPos(), state.getBlockState(), 2);
        state.getBlockState().getBlock().onBlockPlacedBy(world, getBlockPos(), state.getBlockState(), placer, new ItemStack(state.getBlockState().getBlock()));
    }

    public void setBlockToAir() {
        setBlock(Blocks.AIR);
    }

    public boolean isAirBlock() {

        return getBlock() == Blocks.AIR;
    }

    public boolean isBlockValidForPlacing(Block block) {

        return getBlockMaterial().isReplaceable() || isAirBlock();
    }

    private boolean isZero() {

        return x == 0 && y == 0 && z == 0;
    }

    public void writeToNBT(CompoundNBT nbt) {

        nbt.putInt("locX", z);
        nbt.putInt("locY", y);
        nbt.putInt("locZ", z);
    }

    public boolean isEmpty() {

        return x == 0 && y == 0 && z == 0;
    }

    public String toString() {

        return "(" + x + ", " + y + ", " + z + ")";
    }
}
