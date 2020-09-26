package calemiutils.event;

import calemiutils.CalemiUtils;
import calemiutils.init.InitKeyBindings;
import calemiutils.packet.PacketOpenWallet;
import calemiutils.util.helper.CurrencyHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyEvent {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onKeyInput (InputEvent.KeyInputEvent event) {

        if (InitKeyBindings.openWalletButton.isPressed()) {

            PlayerEntity player = Minecraft.getInstance().player;

            ItemStack walletStack = CurrencyHelper.getCurrentWalletStack(player);

            if (!walletStack.isEmpty()) {
                CalemiUtils.network.sendToServer(new PacketOpenWallet());
            }
        }
    }
}