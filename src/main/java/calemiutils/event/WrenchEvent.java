package calemiutils.event;

import calemiutils.tileentity.base.ICurrencyNetworkBank;
import calemiutils.tileentity.base.TileEntityBase;
import calemiutils.util.Location;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.LoreHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WrenchEvent {

    public static void onBlockWrenched(World world, Location location) {

        TileEntity tileEntity = location.getTileEntity();

        ItemStack stack = new ItemStack(location.getBlock().asItem(), 1);

        if (!world.isRemote) {
            ItemHelper.spawnItem(world, location, stack);
        }

        //Currency
        if (tileEntity instanceof ICurrencyNetworkBank) {

            ICurrencyNetworkBank currencyNetwork = (ICurrencyNetworkBank) tileEntity;

            if (currencyNetwork.getStoredCurrency() > 0) {
                ItemHelper.getNBT(stack).putInt("currency", currencyNetwork.getStoredCurrency());
            }
        }

        //Building Unit
        /*if (tileEntity instanceof TileEntityBuildingUnit) {

            TileEntityBuildingUnit teBuildingUnit = (TileEntityBuildingUnit) tileEntity;

            if (!teBuildingUnit.isEmpty()) {
                ItemStackHelper.saveAllItems(ItemHelper.getNBT(stack), teBuildingUnit.slots);
            }
        }*/

        location.setBlockToAir();
    }

    @SubscribeEvent
    public void onBlockPlace(BlockEvent.EntityPlaceEvent event) {

        if (event.getEntity() instanceof  PlayerEntity) {

            PlayerEntity player = (PlayerEntity) event.getEntity();

            TileEntity tileEntity = event.getWorld().getTileEntity(event.getPos());
            ItemStack stack = player.getHeldItem(player.getActiveHand());

            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {

                //Currency
                if (tileEntity instanceof ICurrencyNetworkBank) {

                    ICurrencyNetworkBank currencyNetwork = (ICurrencyNetworkBank) tileEntity;

                    if (ItemHelper.getNBT(stack).getInt("currency") != 0) {
                        currencyNetwork.setCurrency(ItemHelper.getNBT(stack).getInt("currency"));
                        ((TileEntityBase) tileEntity).markForUpdate();
                    }
                }

                //Building Unit
                /*if (tileEntity instanceof TileEntityBuildingUnit) {

                    TileEntityBuildingUnit teBuildingUnit = (TileEntityBuildingUnit) tileEntity;

                    ItemStackHelper.loadAllItems(ItemHelper.getNBT(stack), teBuildingUnit.slots);
                }*/
            }
        }
    }

    @SubscribeEvent
    public void onBlockDestroy(BlockEvent.BreakEvent event) {

        TileEntity tileEntity = event.getWorld().getTileEntity(event.getPos());

        //Building Unit
        /*if (tileEntity instanceof TileEntityBuildingUnit) {
            onBlockWrenched(event.getWorld(), new Location(tileEntity));
        }*/
    }

    @SubscribeEvent
    public void onLoreEvent(ItemTooltipEvent event) {

        if (event.getItemStack().getTag() != null) {

            int currency = ItemHelper.getNBT(event.getItemStack()).getInt("currency");

            if (currency != 0) {
                event.getToolTip().add(new StringTextComponent(""));
                LoreHelper.addCurrencyLore(event.getToolTip(), currency);
            }
        }
    }
}
