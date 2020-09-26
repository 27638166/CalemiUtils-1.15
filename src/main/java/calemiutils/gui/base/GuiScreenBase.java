package calemiutils.gui.base;

import calemiutils.util.helper.ScreenHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;

public abstract class GuiScreenBase extends Screen {

    protected final PlayerEntity player;
    protected final Hand hand;

    protected GuiScreenBase (PlayerEntity player, Hand hand) {

        super(new StringTextComponent("Help"));
        this.player = player;
        this.hand = hand;
    }

    @Override
    public void render (int mouseX, int mouseY, float f1) {

        renderBackground();

        if (getGuiTextureName() != null) {
            ScreenHelper.bindTexture(getGuiTextureName());
            ScreenHelper.drawRect(getScreenX(), getScreenY(), 0, 0, 0, getGuiSizeX(), getGuiSizeY());
        }

        drawGuiBackground(mouseX, mouseY);
        super.render(mouseX, mouseY, f1);
        drawGuiForeground(mouseX, mouseY);
    }

    @SuppressWarnings("SameReturnValue")
    protected abstract String getGuiTextureName ();

    protected int getScreenX () {

        return (this.width - getGuiSizeX()) / 2;
    }

    protected int getScreenY () {

        return (this.height - getGuiSizeY()) / 2;
    }

    @SuppressWarnings("SameReturnValue")
    protected abstract int getGuiSizeX ();

    @SuppressWarnings("SameReturnValue")
    protected abstract int getGuiSizeY ();

    protected abstract void drawGuiBackground (int mouseX, int mouseY);

    @SuppressWarnings("EmptyMethod")
    protected abstract void drawGuiForeground (int mouseX, int mouseY);

    @Override
    public boolean keyPressed (int i1, int i2, int i3) {
        super.keyPressed(i1, i2, i3);

        if (minecraft != null) {
            if (canCloseWithInvKey() && i1 == minecraft.gameSettings.keyBindInventory.getKey().getKeyCode()) {
                player.closeScreen();
                return true;
            }
        }

        return false;
    }

    protected abstract boolean canCloseWithInvKey ();
}
