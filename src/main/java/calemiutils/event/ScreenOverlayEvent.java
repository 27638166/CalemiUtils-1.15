package calemiutils.event;

import calemiutils.CUConfig;
import calemiutils.item.ItemSledgehammer;
import calemiutils.item.ItemWallet;
import calemiutils.tileentity.TileEntityTradingPost;
import calemiutils.util.Location;
import calemiutils.util.helper.CurrencyHelper;
import calemiutils.util.helper.MathHelper;
import calemiutils.util.helper.ScreenHelper;
import calemiutils.util.helper.StringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class ScreenOverlayEvent {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void render (RenderGameOverlayEvent.Post event) {

        Minecraft mc = Minecraft.getInstance();
        World world = mc.world;

        int scaledWidth = mc.func_228018_at_().getScaledWidth();
        int scaledHeight = mc.func_228018_at_().getScaledHeight();

        int midX = scaledWidth / 2;
        int midY = scaledHeight / 2;

        ClientPlayerEntity player = mc.player;

        if (player != null && !player.isSpectator()) {

            ItemStack walletStack = CurrencyHelper.getCurrentWalletStack(player);
            ItemStack activeItemStack = player.getActiveItemStack();

            //Render Trading Post Info
            if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {

                Vec3d posVec = new Vec3d(player.getPositionVector().x, player.getPositionVector().y + player.getEyeHeight(), player.getPositionVector().z);

                Vec3d lookVec = player.getLookVec();
                Direction dir = Direction.getFacingFromVector(lookVec.x, lookVec.y, lookVec.z);

                BlockRayTraceResult trace = world.rayTraceBlocks(new RayTraceContext(posVec, posVec.add(lookVec.scale(5)), RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, player));
                ItemUseContext itemUseContext = new ItemUseContext(player, Hand.MAIN_HAND, trace);
                BlockItemUseContext blockUseContext = new BlockItemUseContext(itemUseContext);

                if (trace.getType() == RayTraceResult.Type.BLOCK) {

                    BlockPos pos = blockUseContext.getPos();
                    Location locationOffset = new Location(world, pos);
                    player.spawnSweepParticles();

                    BlockPos difference = locationOffset.getBlockPos().subtract(itemUseContext.getPos());
                    Direction blockSide = Direction.getFacingFromVector(difference.getX(), difference.getY(), difference.getZ());

                    Location locationReal = locationOffset.translate(blockSide.getOpposite(), 1);

                    if (locationReal.getTileEntity() != null && locationReal.getTileEntity() instanceof TileEntityTradingPost) {

                        TileEntityTradingPost post = (TileEntityTradingPost) locationReal.getTileEntity();

                        if (post.hasValidTradeOffer) {

                            ItemStack stack = post.getStackForSale();

                            String postName = post.getSecurityProfile().getOwnerName() + "'s Trading Post";

                            if (post.adminMode) {
                                postName = "Admin Post";
                            }

                            String str = (post.buyMode ? "Buying " : "Selling ") + StringHelper.printCommas(post.amountForSale) + "x " + post.getStackForSale().getDisplayName().getFormattedText() + " for " + (post.salePrice > 0 ? (StringHelper.printCurrency(post.salePrice)) : "free");

                            List<ITextComponent> lore = post.getStackForSale().getTooltip(Minecraft.getInstance().player, ITooltipFlag.TooltipFlags.NORMAL);

                            int boxWidth = mc.fontRenderer.getStringWidth(str) + 5;
                            int boxHeight = 23;

                            if (lore.size() == 2) boxHeight += 11;

                            ScreenHelper.bindGuiTextures();
                            ScreenHelper.drawCappedRect(midX - boxWidth / 2, midY + 12, 0, 138, 0, boxWidth, boxHeight, 256, 102);

                            ScreenHelper.drawCenteredString(postName, midX, midY + 15, 5, 0xFFFFFFFF);
                            ScreenHelper.drawCenteredString(str, midX, midY + 15 + 10, 5, 0xFFFFFFFF);

                            if (lore.size() == 2) ScreenHelper.drawCenteredString(lore.get(1).getFormattedText(), midX, midY + 15 + 20, 5, 0xFFFFFFFF);
                        }
                    }
                }
            }

            //Renders the Wallet info.
            if (CUConfig.wallet.walletOverlay.get() && !walletStack.isEmpty() && mc.currentScreen == null) {

                renderWalletInfo(CUConfig.WalletOverlayPosition.byName(CUConfig.wallet.walletOverlayPosition.get()), scaledWidth, scaledHeight, event, walletStack);
            }

            //Renders the Sledgehammer's charge icon.
            if (!activeItemStack.isEmpty() && activeItemStack.getItem() instanceof ItemSledgehammer && mc.currentScreen == null) {

                if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {

                    ItemSledgehammer sledgeHammerItem = (ItemSledgehammer) activeItemStack.getItem();

                    int currentChargeTime = player.getItemInUseMaxCount();
                    int chargeTime = sledgeHammerItem.chargeTime;
                    int hammerIconWidth = 13;

                    currentChargeTime = net.minecraft.util.math.MathHelper.clamp(currentChargeTime, 0, chargeTime);

                    int scale = MathHelper.scaleInt(currentChargeTime, chargeTime, hammerIconWidth);

                    ScreenHelper.bindGuiTextures();

                    if (currentChargeTime < chargeTime) {

                        ScreenHelper.drawRect(midX - 7, midY - 11, 0, 87, 0, hammerIconWidth, 4);
                        ScreenHelper.drawRect(midX - 7, midY - 11, hammerIconWidth, 87, 5, scale, 4);
                    }

                    else {

                        if (player.world.getGameTime() % 5 > 1) {
                            ScreenHelper.drawRect(midX - 7, midY - 11, hammerIconWidth * 2, 87, 10, hammerIconWidth, 4);
                        }
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void renderWalletInfo (CUConfig.WalletOverlayPosition position, int scaledWidth, int scaledHeight, RenderGameOverlayEvent.Post event, ItemStack walletStack) {

        int currency = ItemWallet.getBalance(walletStack);
        String currencyStr = StringHelper.printCurrency(currency);

        int xOffsetStr = 0;
        int xOffsetItem = 0;
        int yOffset = 0;

        if (position == CUConfig.WalletOverlayPosition.BOTTOM_LEFT) {
            yOffset = scaledHeight - 16;
        }

        else if (position == CUConfig.WalletOverlayPosition.TOP_RIGHT) {
            xOffsetStr = scaledWidth - Minecraft.getInstance().fontRenderer.getStringWidth(currencyStr) - 41;
            xOffsetItem = scaledWidth - 20;
        }

        else if (position == CUConfig.WalletOverlayPosition.BOTTOM_RIGHT) {
            yOffset = scaledHeight - 16;
            xOffsetStr = scaledWidth - Minecraft.getInstance().fontRenderer.getStringWidth(currencyStr) - 41;
            xOffsetItem = scaledWidth - 20;
        }

        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {

            Minecraft.getInstance().fontRenderer.drawString(currencyStr, 21 + xOffsetStr, 4 + yOffset, 0xFFFFFFFF);
        }

        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            ScreenHelper.drawItemStack(Minecraft.getInstance().getItemRenderer(), walletStack, 2 + xOffsetItem, yOffset);
        }
    }
}
