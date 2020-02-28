package calemiutils.item;

import calemiutils.CalemiUtils;
import calemiutils.config.CUConfig;
import calemiutils.event.WrenchEvent;
import calemiutils.item.base.ItemBase;
import calemiutils.security.ISecurity;
import calemiutils.tileentity.base.TileEntityBase;
import calemiutils.util.Location;
import calemiutils.util.helper.LoreHelper;
import calemiutils.util.helper.SecurityHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSecurityWrench extends ItemBase {

    public ItemSecurityWrench() {

        super("security_wrench", new Item.Properties().group(CalemiUtils.TAB).maxStackSize(1));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltipList, ITooltipFlag advanced) {
        LoreHelper.addInformationLore(tooltipList, "Used to access secured blocks!");
        LoreHelper.addControlsLore(tooltipList, "Interact with secured blocks", LoreHelper.Type.USE, true);
        LoreHelper.addControlsLore(tooltipList, "Pick up secured blocks", LoreHelper.Type.SNEAK_USE);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {

        PlayerEntity player = context.getPlayer();

        Location location = new Location(context.getWorld(), context.getPos());

        if (player.isCrouching() && location.getTileEntity() != null && location.getTileEntity() instanceof TileEntityBase) {

            if (location.getTileEntity() instanceof ISecurity) {

                ISecurity security = (ISecurity) location.getTileEntity();

                if (security.getSecurityProfile().isOwner(player.getName().getFormattedText()) || player.isCreative() || !CUConfig.misc.useSecurity.get()) {
                    WrenchEvent.onBlockWrenched(context.getWorld(), location);
                    return ActionResultType.SUCCESS;

                }

                else SecurityHelper.printErrorMessage(location, player);
            }

            else {
                WrenchEvent.onBlockWrenched(context.getWorld(), location);
            }
        }

        return super.onItemUseFirst(stack, context);
    }
}
