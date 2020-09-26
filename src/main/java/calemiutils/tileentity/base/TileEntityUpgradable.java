package calemiutils.tileentity.base;

import calemiutils.CUConfig;
import calemiutils.util.helper.MathHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

public abstract class TileEntityUpgradable extends TileEntityInventoryBase implements IProgress, IRange {

    public int currentProgress;
    public int currentRange;

    protected TileEntityUpgradable (TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public int getScaledRange () {
        return getScaledSlot(getRangeSlot(), getScaledRangeMin(), getScaledRangeMax());
    }

    private int getScaledSlot (int slot, int min, int max) {
        int difference = max - min;
        return min + MathHelper.scaleInt(getStackInSlot(slot).getCount(), 5, difference);
    }

    protected abstract int getRangeSlot ();

    protected abstract int getScaledRangeMin ();

    protected abstract int getScaledRangeMax ();

    protected int scaleCost (int cost) {
        return cost + (cost * CUConfig.misc.speedUpgradeCostMultiplier.get() * getStackInSlot(getSpeedSlot()).getCount());
    }

    protected abstract int getSpeedSlot ();

    protected void tickProgress () {
        currentProgress += getScaledSpeed();
    }

    private int getScaledSpeed () {
        return getScaledSlot(getSpeedSlot(), getScaledSpeedMin(), getScaledSpeedMax());
    }

    protected abstract int getScaledSpeedMin ();

    protected abstract int getScaledSpeedMax ();

    protected boolean isDoneAndReset () {

        if (currentProgress >= getMaxProgress()) {
            currentProgress = 0;
            return true;
        }

        return false;
    }

    @Override
    public int getCurrentProgress () {
        return currentProgress;
    }

    public void setCurrentProgress (int value) {
        currentProgress = value;
    }

    @Override
    public int getCurrentRange () {
        return currentRange;
    }

    @Override
    public void read (CompoundNBT nbt) {
        super.read(nbt);
        currentProgress = nbt.getInt("currentProgress");
        currentRange = nbt.getInt("currentRange");
    }

    @Override
    public CompoundNBT write (CompoundNBT nbt) {
        super.write(nbt);
        nbt.putInt("currentProgress", currentProgress);
        nbt.putInt("currentRange", currentRange);
        return nbt;
    }
}
