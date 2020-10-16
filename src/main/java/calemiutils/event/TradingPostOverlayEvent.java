package calemiutils.event;

import calemiutils.tileentity.TileEntityTradingPost;
import calemiutils.util.Location;
import calemiutils.util.helper.LoreHelper;
import calemiutils.util.helper.RayTraceHelper;
import calemiutils.util.helper.ScreenHelper;
import calemiutils.util.helper.StringHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class TradingPostOverlayEvent {

    /**
     * Handles the Trading Post overlay when the cursor is over it.
     */
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void render (RenderGameOverlayEvent.Post event) {

        //Checks if the current render is on the "HOTBAR" layer, so we can use transparency.
        if (event.getType() == RenderGameOverlayEvent.ElementType.CHAT) {

            Minecraft mc = Minecraft.getInstance();
            World world = mc.world;
            ClientPlayerEntity player = mc.player;

            int scaledWidth = mc.func_228018_at_().getScaledWidth();
            int scaledHeight = mc.func_228018_at_().getScaledHeight();
            int midX = scaledWidth / 2;
            int midY = scaledHeight / 2;

            RayTraceHelper.BlockTrace blockTrace = RayTraceHelper.RayTraceBlock(world, player, Hand.MAIN_HAND);

            //Checks if the trace hit a block.
            if (blockTrace != null) {

                Location hit = blockTrace.getHit();

                //Check if the hit was a Trading Post
                if (hit.getTileEntity() instanceof TileEntityTradingPost) {

                    TileEntityTradingPost post = (TileEntityTradingPost) hit.getTileEntity();

                    //Checks if the Trading Post has a valid trade.
                    if (post.hasValidTradeOffer) {

                        ItemStack stackForSale = post.getStackForSale();

                        List<String> list = new ArrayList<>();
                        List<ITextComponent> lore = post.getStackForSale().getTooltip(Minecraft.getInstance().player, ITooltipFlag.TooltipFlags.NORMAL);

                        String postName = post.adminMode ? ("Admin Post") : (post.getSecurityProfile().getOwnerName() + "'s Trading Post");
                        String sellingStr = (post.buyMode ? "Buying " : "Selling ") + StringHelper.printCommas(post.amountForSale) + "x " + post.getStackForSale().getDisplayName().getFormattedText() + " for " + (post.salePrice > 0 ? (StringHelper.printCurrency(post.salePrice)) : "free");

                        list.add(postName);
                        list.add(sellingStr);

                        if (lore.size() > 2) {

                            if (player.isCrouching()) {

                                for (ITextComponent component : lore) {
                                    list.add(component.getFormattedText());
                                }

                                list.remove(2);

                                StringHelper.removeNullsFromList(list);
                                StringHelper.removeCharFromList(list, "Shift", "Ctrl");
                            }

                            else {
                                list.add(ChatFormatting.GRAY + "[" + ChatFormatting.AQUA + "Shift" + ChatFormatting.GRAY + "]" + ChatFormatting.GRAY + " Info");
                            }
                        }

                        ScreenHelper.bindGuiTextures();
                        ScreenHelper.drawTextBox(midX - 3, midY + 12, 0, true, StringHelper.getArrayFromList(list));
                    }
                }
            }
        }
    }
}
