package calemiutils.gui.base;

import calemiutils.util.helper.ScreenHelper;
import calemiutils.util.helper.StringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class FakeSlot extends Button {

    private final ScreenRect rect;
    private final ItemRenderer itemRender;
    private ItemStack stack = new ItemStack(Items.AIR);

    public FakeSlot (int x, int y, ItemRenderer itemRender, Button.IPressable pressable) {
        super(x, y, 16, 16, "", pressable);
        rect = new ScreenRect(this.x, this.y, width, height);
        this.itemRender = itemRender;
    }

    @Override
    public void renderButton (int mouseX, int mouseY, float partialTicks) {

        if (this.visible) {

            List<String> list = new ArrayList<>();
            List<ITextComponent> lore = stack.getTooltip(Minecraft.getInstance().player, ITooltipFlag.TooltipFlags.NORMAL);

            for (ITextComponent component : lore) {
                list.add(component.getFormattedText());
            }

            StringHelper.removeNullsFromList(list);
            StringHelper.removeCharFromList(list, "Shift", "Ctrl");

            ScreenHelper.drawItemStack(itemRender, getItemStack(), rect.x, rect.y);
            if (!stack.isEmpty()) ScreenHelper.drawHoveringTextBox(mouseX, mouseY, 300, rect, StringHelper.getArrayFromList(list));

            GL11.glColor3f(1, 1, 1);
        }
    }

    private ItemStack getItemStack () {
        return stack;
    }

    public void setItemStack (ItemStack stack) {
        this.stack = stack;
    }
}
