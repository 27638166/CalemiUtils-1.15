package calemiutils.event;

import calemiutils.item.ItemSledgehammer;
import calemiutils.util.helper.CurrencyHelper;
import calemiutils.util.helper.GuiHelper;
import calemiutils.util.helper.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OverlayEvent {

    @SubscribeEvent
    public void render(RenderGameOverlayEvent.Post event) {

        Minecraft mc = Minecraft.getInstance();

        int midX = mc.func_228018_at_().getScaledWidth() / 2;
        int midY = mc.func_228018_at_().getScaledHeight() / 2;

        ClientPlayerEntity player = mc.player;

        if (player != null) {

            ItemStack walletStack = CurrencyHelper.getCurrentWalletStack(player);

            ItemStack activeItemStack = player.getActiveItemStack();

            //Wallet Currency
            /*if (CUConfig.wallet.walletOverlay.get() && !walletStack.isEmpty() && mc.currentScreen == null) {

            if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {

                int currency = ItemWallet.getBalance(walletStack);

                Minecraft.getMinecraft().fontRenderer.drawString(StringHelper.printCurrency(currency), 21, 4, 0xFFFFFFFF, true);
            }

            if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
                GuiHelper.drawItemStack(mc.getRenderItem(), walletStack, 2, 0);
            }
            }*/

            //Sledgehammer Charge Bar
            if (!activeItemStack.isEmpty() && activeItemStack.getItem() instanceof ItemSledgehammer && mc.currentScreen == null) {

                if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {

                    ItemSledgehammer sledgeHammerItem = (ItemSledgehammer) activeItemStack.getItem();

                    int currentChargeTime = player.getItemInUseMaxCount();
                    int chargeTime = sledgeHammerItem.chargeTime;
                    int hammerIconWidth = 13;

                    currentChargeTime = net.minecraft.util.math.MathHelper.clamp(currentChargeTime, 0, chargeTime);

                    int scale = MathHelper.scaleInt(currentChargeTime, chargeTime, hammerIconWidth);

                    GuiHelper.bindGuiTextures();

                    if (currentChargeTime < chargeTime) {

                        GuiHelper.drawRect(midX - 6, midY - 11, 0, 87, 0, hammerIconWidth, 4);
                        GuiHelper.drawRect(midX - 6, midY - 11, hammerIconWidth, 87, 5, scale, 4);
                    }

                    else {

                        if (player.world.getGameTime() % 5 > 1) {
                            GuiHelper.drawRect(midX - 6, midY - 11, hammerIconWidth * 2, 87, 10, hammerIconWidth, 4);
                        }
                    }
                }
            }
        }
    }
}
