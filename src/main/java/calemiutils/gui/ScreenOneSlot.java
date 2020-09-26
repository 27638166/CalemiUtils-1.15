package calemiutils.gui;

import calemiutils.gui.base.ContainerScreenBase;
import calemiutils.inventory.base.ContainerBase;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenOneSlot extends ContainerScreenBase<ContainerBase> {

    public ScreenOneSlot (Container container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
    }

    @Override
    public int getGuiSizeY () {
        return 123;
    }

    @Override
    public void drawGuiForeground (int mouseX, int mouseY) {}

    @Override
    public String getGuiTextureName () {
        return "one_slot";
    }

    @Override
    public void drawGuiBackground (int mouseX, int mouseY) {}
}
