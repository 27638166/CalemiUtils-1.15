package calemiutils.gui.base;

import calemiutils.util.helper.GuiHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

import java.io.IOException;

public abstract class GuiScreenBase extends Screen {

    protected final PlayerEntity player;

    protected GuiScreenBase(PlayerEntity player) {

        super(new StringTextComponent("Help"));
        this.player = player;
    }

    @SuppressWarnings("SameReturnValue")
    protected abstract String getGuiTextureName();

    @SuppressWarnings("SameReturnValue")
    protected abstract int getGuiSizeX();

    @SuppressWarnings("SameReturnValue")
    protected abstract int getGuiSizeY();

    protected abstract void drawGuiBackground(int mouseX, int mouseY);

    @SuppressWarnings("EmptyMethod")
    protected abstract void drawGuiForeground(int mouseX, int mouseY);

    protected abstract boolean canCloseWithInvKey();

    protected int getScreenX() {

        return (this.width - getGuiSizeX()) / 2;
    }

    protected int getScreenY() {

        return (this.height - getGuiSizeY()) / 2;
    }


    @Override
    public void render(int mouseX, int mouseY, float f1) {

        if (getGuiTextureName() != null) {
            GuiHelper.bindTexture(getGuiTextureName());
            GuiHelper.drawRect(getScreenX(), getScreenY(), 0, 0, 0, getGuiSizeX(), getGuiSizeY());
        }

        drawGuiBackground(mouseX, mouseY);
        super.render(mouseX, mouseY, f1);
        drawGuiForeground(mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int i1, int i2, int i3) {
        super.keyPressed(i1, i2, i3);

        if (canCloseWithInvKey() && i1 == minecraft.gameSettings.keyBindInventory.getKey().getKeyCode()) {
            minecraft.player.closeScreen();
            return true;
        }

        return false;
    }
}
