package calemiutils.gui.base;

import calemiutils.util.helper.GuiHelper;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class GuiBlueprintButton extends Button {

    private final GuiRect rect;
    private final ItemStack stack;
    private final ItemRenderer itemRender;

    public GuiBlueprintButton(int x, int y, ItemRenderer itemRender, ItemStack stack, Button.IPressable pressable) {

        super(x, y, 16, 16, "", pressable);
        rect = new GuiRect(this.x, this.y, width, height);
        this.stack = stack;
        this.itemRender = itemRender;
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks) {

        if (this.visible && active) {

            isHovered = rect.contains(mouseX, mouseY);

            ItemStack icon = stack;

            GuiHelper.drawItemStack(itemRender, icon, rect.x, rect.y);
            GuiHelper.drawHoveringTextBox(mouseX, mouseY, 150, rect, icon.getDisplayName().getFormattedText());

            setMessage(icon.getDisplayName().getFormattedText());

            GL11.glColor4f(1, 1, 1, 1);
        }

        super.renderButton(mouseX, mouseY, partialTicks);
    }
}
