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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WrenchEvent {

    /**
     * Called when a Block is taken by a Security Wrench.
     * Also saves the currency of the Block within the drop.
     */
    public static void onBlockWrenched (World world, Location location) {

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

        location.setBlockToAir();
    }

    /**
     * Handles transforming the Item's NBT into currency for the Block.
     */
    @SubscribeEvent
    public void onBlockPlace (BlockEvent.EntityPlaceEvent event) {

        if (event.getEntity() instanceof PlayerEntity) {

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
            }
        }
    }
}
