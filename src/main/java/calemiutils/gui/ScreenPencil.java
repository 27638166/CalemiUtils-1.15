package calemiutils.gui;

import calemiutils.CalemiUtils;
import calemiutils.gui.base.BlueprintButton;
import calemiutils.gui.base.GuiScreenBase;
import calemiutils.init.InitItems;
import calemiutils.item.ItemPencil;
import calemiutils.packet.PacketPencilSetColor;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.ScreenHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenPencil extends GuiScreenBase {

    private final BlueprintButton[] buttons = new BlueprintButton[16];

    public ScreenPencil (PlayerEntity player, Hand hand) {
        super(player, hand);
    }

    @Override
    protected void init () {

        super.init();

        for (int i = 0; i < buttons.length; i++) {
            int id = i;

            ItemStack stack = new ItemStack(InitItems.PENCIL.get());
            ItemHelper.getNBT(stack).putInt("color", id);

            addButton(new BlueprintButton(id, getScreenX() + (i * 20) - 158, getScreenY() - 8, itemRenderer, stack, (btn) -> {
                ItemPencil pencil = (ItemPencil) stack.getItem();
                CalemiUtils.network.sendToServer(new PacketPencilSetColor(id, hand));
                pencil.setColorById(stack, id);
                player.closeScreen();
            }));
        }
    }

    @Override
    public boolean isPauseScreen () {
        return false;
    }

    @Override
    public String getGuiTextureName () {
        return null;
    }

    @Override
    public int getGuiSizeX () {
        return 0;
    }

    @Override
    public int getGuiSizeY () {
        return 0;
    }

    @Override
    public void drawGuiBackground (int mouseX, int mouseY) {

        for (int i = 0; i < DyeColor.values().length; i++) {
            int color = DyeColor.byId(i).getColorValue();
            ScreenHelper.drawColoredRect(getScreenX() + (i * 20) - 160, 0, 0, 20, this.height, color, 0.4F);
        }

        ScreenHelper.drawCenteredString("Choose a Color", getScreenX(), getScreenY() - 25, 0, 0xFFFFFF);
    }

    @Override
    public void drawGuiForeground (int mouseX, int mouseY) {}

    @Override
    public boolean canCloseWithInvKey () {
        return true;
    }
}
