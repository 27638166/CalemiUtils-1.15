package calemiutils.util.helper;

import calemiutils.CUReference;
import calemiutils.gui.base.ScreenRect;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ScreenHelper {

    private static final int TEXTURE_SIZE = 256;
    private static final Minecraft mc = Minecraft.getInstance();

    public static void bindTexture (String name) {

        mc.getTextureManager().bindTexture(new ResourceLocation(CUReference.MOD_ID + ":textures/gui/" + name + ".png"));
    }

    public static void drawHoveringTextBox (int mouseX, int mouseY, int zLevel, ScreenRect rect, String... text) {

        if (rect.contains(mouseX, mouseY)) {

            drawTextBox(mouseX, mouseY, zLevel, text);
        }
    }

    public static void drawTextBox (int x, int y, int zLevel, String... text) {

        GL11.glPushMatrix();
        GL11.glTranslatef(0, 0, zLevel);

        int maxLength = mc.fontRenderer.getStringWidth(text[0]);

        for (String str : text) {

            if (mc.fontRenderer.getStringWidth(str) > maxLength) {
                maxLength = mc.fontRenderer.getStringWidth(str);
            }
        }

        bindGuiTextures();
        drawCappedRect(x + 7, y - 13, 0, 138, 0, maxLength + 5, 13 + ((text.length - 1) * 9), 256, 102);

        for (int i = 0; i < text.length; i++) {

            String str = text[i];

            GL11.glTranslatef(0, 0, 100);
            mc.fontRenderer.drawString(ChatFormatting.WHITE + str, (x + 10), (y - 10) + (i * 9), 0xFFFFFF);
        }

        GL11.glTranslatef(0, 0, 0);
        GL11.glPopMatrix();
        GL11.glColor4f(1, 1, 1, 1);
    }

    /*public static void drawOneLineHoveringTextBox(String text, int mouseX, int mouseY, GuiRect rect) {

        if (rect.contains(mouseX, mouseY)) {

            GL11.glPushMatrix();
            GL11.glTranslatef(0, 0, 325);

            bindGuiTextures();
            drawCappedRect(mouseX + 2, mouseY - 13, 0, 168, 0, mc.fontRenderer.getStringWidth(text) + 5, 13, 256, 72);
            mc.fontRenderer.drawString(text, mouseX + 5, mouseY - 10, 0xFFFFFF);

            GL11.glPopMatrix();

            GL11.glColor4f(1, 1, 1, 1);
        }
    }*/

    public static void bindGuiTextures () {

        mc.getTextureManager().bindTexture(CUReference.GUI_TEXTURES);
    }

    public static void drawCappedRect (int x, int y, int u, int v, int zLevel, int width, int height, int maxWidth, int maxHeight) {

        drawRect(x, y, u, v, zLevel, width - 2, height - 2);
        drawRect(x + width - 2, y, u + maxWidth - 2, v, zLevel, 2, height - 2);
        drawRect(x, y + height - 2, u, v + maxHeight - 2, zLevel, width - 2, 2);
        drawRect(x + width - 2, y + height - 2, u + maxWidth - 2, v + maxHeight - 2, zLevel, 2, 2);
    }

    public static void drawRect (int x, int y, int u, int v, int zLevel, int width, int height) {

        GL11.glPushMatrix();
        GL11.glTranslatef(0, 0, zLevel);

        int maxX = x + width;
        int maxY = y + height;

        int maxU = u + width;
        int maxV = v + height;

        float pixel = 1F / TEXTURE_SIZE;

        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder buffer = tessellator.getBuffer();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.func_225582_a_((float) x, (float) maxY, 50);
        buffer.func_225583_a_(u * pixel, maxV * pixel).endVertex();
        buffer.func_225582_a_((float) maxX, (float) maxY, 50);
        buffer.func_225583_a_(maxU * pixel, maxV * pixel).endVertex();
        buffer.func_225582_a_((float) maxX, (float) y, 50);
        buffer.func_225583_a_(maxU * pixel, v * pixel).endVertex();
        buffer.func_225582_a_((float) x, (float) y, 50);
        buffer.func_225583_a_(u * pixel, v * pixel).endVertex();
        tessellator.draw();

        RenderSystem.disableBlend();

        GL11.glPopMatrix();
    }

    /*
    public static void drawLimitedCenteredString(String text, int x, int y, int maxWidth, int color) {

        int xPos = (x - 3);
        int yPos = y;

        int stringWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);

        if (stringWidth > maxWidth) {

            ArrayList arrayList = new ArrayList();
            Iterator iterator = mc.fontRenderer.listFormattedStringToWidth(text, maxWidth).iterator();

            while (iterator.hasNext()) {
                String s1 = (String) iterator.next();
                arrayList.add(s1);
            }

            String[] string = (String[]) arrayList.toArray(new String[0]);
            int yOffset = yPos + 5;

            for (int i = 0; i < string.length; ++i) {

                String s = string[i];
                drawCenteredString(s, xPos + 3, yOffset, color);
                yOffset += mc.fontRenderer.FONT_HEIGHT + 1;
            }
        }

        else {
            mc.fontRenderer.drawString(text, (xPos - (stringWidth / 2)) + 3, yPos + 5, color);
        }
    }*/

    public static void drawLimitedString (String text, int x, int y, int textLimit, int color) {

        String temp = text;

        if (temp.length() > textLimit) {

            temp = temp.substring(0, textLimit - 1);
            temp += "...";
        }

        mc.fontRenderer.drawString(temp, x, y, color);
        GL11.glColor4f(1, 1, 1, 1);
    }

    public static void drawCenteredString (String text, int x, int y, int zLevel, int color) {

        GL11.glPushMatrix();
        GL11.glTranslatef(0, 0, 50 + zLevel);
        mc.fontRenderer.drawString(text, x - (float) (mc.fontRenderer.getStringWidth(text) / 2), y, color);
        GL11.glPopMatrix();
    }

    public static void drawColoredRect (int x, int y, int zLevel, int width, int height, int hex, float alpha) {

        float r = (hex >> 16) & 0xFF;
        float g = (hex >> 8) & 0xFF;
        float b = (hex) & 0xFF;

        float red = ((((r * 100) / 255) / 100));
        float green = ((((g * 100) / 255) / 100));
        float blue = ((((b * 100) / 255) / 100));

        int maxX = x + width;
        int maxY = y + height;

        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        //RenderSystem.defaultBlendFunc();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.func_225582_a_(x, maxY, zLevel).func_227885_a_(red, green, blue, alpha).endVertex();
        bufferbuilder.func_225582_a_(maxX, maxY, zLevel).func_227885_a_(red, green, blue, alpha).endVertex();
        bufferbuilder.func_225582_a_(maxX, y, zLevel).func_227885_a_(red, green, blue, alpha).endVertex();
        bufferbuilder.func_225582_a_(x, y, zLevel).func_227885_a_(red, green, blue, alpha).endVertex();
        tessellator.draw();

        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
    }

    public static void drawItemStack (ItemRenderer itemRender, ItemStack stack, int x, int y) {

        RenderHelper.func_227780_a_();
        GL11.glTranslatef(0.0F, 0.0F, 0.0F);
        itemRender.zLevel = -100;
        itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        itemRender.zLevel = 0F;
    }
}
