package calemiutils.gui;

import calemiutils.CalemiUtils;
import calemiutils.gui.base.ButtonRect;
import calemiutils.gui.base.ContainerScreenBase;
import calemiutils.inventory.ContainerTorchPlacer;
import calemiutils.packet.PacketEnableTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenTorchPlacer extends ContainerScreenBase<ContainerTorchPlacer> {

    private ButtonRect activateBtn;

    public ScreenTorchPlacer (Container container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
    }

    @Override
    public int getGuiSizeY () {
        return 192;
    }

    @Override
    protected void drawGuiForeground (int mouseX, int mouseY) {}

    @Override
    protected String getGuiTextureName () {
        return "torch_placer";
    }

    @Override
    protected void drawGuiBackground (int mouseX, int mouseY) {}

    @Override
    protected void init () {
        super.init();

        int btnWidth = 62;
        activateBtn = addButton(new ButtonRect(getScreenX() + (getGuiSizeX() / 2) - (btnWidth / 2), getScreenY() + 24, btnWidth, getEnabledText(), (btn) -> toggleActivate()));
    }

    /**
     * @return The Tile Entities current enable state.
     */
    private String getEnabledText () {
        return getTileEntity().enable ? "Enabled" : "Disabled";
    }

    /**
     * Called when the activateBtn is pressed.
     * Handles toggling activation.
     */
    private void toggleActivate () {

        boolean value = !getTileEntity().enable;

        CalemiUtils.network.sendToServer(new PacketEnableTileEntity(value, getTileEntity().getPos()));
        getTileEntity().enable = value;

        activateBtn.setMessage(getEnabledText());
    }
}
