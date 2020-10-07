package calemiutils.gui.base;

import calemiutils.init.InitItems;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.ScreenHelper;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class PencilButton extends Button {

    private final int id;
    private final ScreenRect rect;
    private final ItemRenderer itemRender;

    /**
     * A colored Pencil button. Used to set the color of a Pencil.
     * @param id Used to determine what color of Pencil to render.
     * @param pressable Called when the button is pressed.
     */
    public PencilButton (int id, int x, int y, ItemRenderer itemRender, Button.IPressable pressable) {
        super(x, y, 16, 16, "", pressable);
        this.id = id;
        rect = new ScreenRect(this.x, this.y, width, height);
        this.itemRender = itemRender;
    }

    @Override
    public void renderButton (int mouseX, int mouseY, float partialTicks) {

        if (this.visible && active) {

            isHovered = rect.contains(mouseX, mouseY);

            ItemStack stack = new ItemStack(InitItems.PENCIL.get());
            ItemHelper.getNBT(stack).putInt("color", id);

            ScreenHelper.drawItemStack(itemRender, stack, rect.x, rect.y);
            ScreenHelper.drawHoveringTextBox(mouseX, mouseY, 150, rect, DyeColor.byId(id).getName().toUpperCase());

            GL11.glColor4f(1, 1, 1, 1);
        }
    }
}
