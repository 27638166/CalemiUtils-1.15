package calemiutils.event;

import calemiutils.tileentity.TileEntityTradingPost;
import calemiutils.util.Location;
import calemiutils.util.helper.RayTraceHelper;
import calemiutils.util.helper.ScreenHelper;
import calemiutils.util.helper.StringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class TradingPostOverlayEvent {

    /**
     * Handles the Trading Post overlay when the cursor is over it.
     */
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void render (RenderGameOverlayEvent.Post event) {

        //Checks if the current render is on the "HOTBAR" layer, so we can use transparency.
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {

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
                        List<ITextComponent> stackForSaleLore = post.getStackForSale().getTooltip(Minecraft.getInstance().player, ITooltipFlag.TooltipFlags.NORMAL);

                        String postName = post.adminMode ? ("Admin Post") : (post.getSecurityProfile().getOwnerName() + "'s Trading Post");
                        String sellingStr = (post.buyMode ? "Buying " : "Selling ") + StringHelper.printCommas(post.amountForSale) + "x " + post.getStackForSale().getDisplayName().getFormattedText() + " for " + (post.salePrice > 0 ? (StringHelper.printCurrency(post.salePrice)) : "free");

                        int boxWidth = mc.fontRenderer.getStringWidth(sellingStr) + 5;
                        int boxHeight = 23;

                        //If the Stack for sale has lore, increase the box size.
                        if (stackForSaleLore.size() == 2) boxHeight += 11;

                        ScreenHelper.bindGuiTextures();
                        ScreenHelper.drawCappedRect(midX - boxWidth / 2, midY + 12, 0, 138, 0, boxWidth, boxHeight, 256, 102);

                        ScreenHelper.drawCenteredString(postName, midX, midY + 15, 5, 0xFFFFFFFF);
                        ScreenHelper.drawCenteredString(sellingStr, midX, midY + 15 + 10, 5, 0xFFFFFFFF);

                        //If the Stack for sale has lore, draw the string.
                        if (stackForSaleLore.size() == 2) ScreenHelper.drawCenteredString(stackForSaleLore.get(1).getFormattedText(), midX, midY + 15 + 20, 5, 0xFFFFFFFF);
                    }
                }
            }
        }
    }
}
