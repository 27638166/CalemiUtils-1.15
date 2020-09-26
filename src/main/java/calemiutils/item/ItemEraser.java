package calemiutils.item;

import calemiutils.CalemiUtils;
import calemiutils.block.BlockBlueprint;
import calemiutils.item.base.ItemBase;
import calemiutils.util.Location;
import calemiutils.util.VeinScan;
import calemiutils.util.helper.LoreHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemEraser extends ItemBase {

    public ItemEraser () {
        super(new Item.Properties().group(CalemiUtils.TAB).maxStackSize(1));
    }

    @Override
    public void addInformation (ItemStack stack, @Nullable World world, List<ITextComponent> tooltipList, ITooltipFlag advanced) {
        LoreHelper.addInformationLore(tooltipList, "Destroys Blueprint", true);
        LoreHelper.addControlsLore(tooltipList, "Erases one Blueprint", LoreHelper.Type.USE, true);
        LoreHelper.addControlsLore(tooltipList, "Erases all connected Blueprint", LoreHelper.Type.SNEAK_USE);
    }

    @Override
    public ActionResultType onItemUse (ItemUseContext context) {

        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        BlockPos pos = context.getPos();

        Location location = new Location(world, pos);

        if (player != null) {

            if (location.getBlock() instanceof BlockBlueprint) {

                if (!player.isCrouching()) {
                    location.setBlockToAir();
                }

                else {

                    VeinScan scan = new VeinScan(location, location.getBlockState());
                    scan.startScan();

                    for (Location nextLocation : scan.buffer) {

                        nextLocation.setBlockToAir();
                    }
                }

                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.FAIL;
    }

    @Override
    public float getDestroySpeed (ItemStack stack, BlockState state) {

        if (state.getBlock() instanceof BlockBlueprint) {
            return 9F;
        }

        return super.getDestroySpeed(stack, state);
    }
}
