package calemiutils.util.helper;

import calemiutils.CUReference;
import calemiutils.gui.base.ScreenRect;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class ScreenHelper {

    private static final int TEXTURE_SIZE = 256;
    private static final Minecraft mc = Minecraft.getInstance();

    public static void bindGuiTextures () {
        mc.getTextureManager().bindTexture(CUReference.GUI_TEXTURES);
    }

    public static void bindTexture (String name) {
        mc.getTextureManager().bindTexture(new ResourceLocation(CUReference.MOD_ID + ":textures/gui/" + name + ".png"));
    }

    public static void drawHoveringTextBox (int mouseX, int mouseY, int zLevel, ScreenRect rect, String... text) {

        if (rect.contains(mouseX, mouseY)) {
            drawTextBox(mouseX + 8, mouseY - 10, zLevel, false, text);
        }
    }

    public static void drawTextBox (int x, int y, int zLevel, boolean centeredString, String... text) {

        GL11.glPushMatrix();
        GL11.glTranslatef(0, 0, zLevel);

        List<String> textToRender = new ArrayList<>();

        int maxLength = mc.fontRenderer.getStringWidth(text[0]);

        for (String str : text) {

            if (str.length() >= 80) {

                textToRender.add(str.substring(0, str.length() / 2));
                textToRender.add(StringHelper.addAllFormats(StringHelper.getFormatsFromString(str)) + str.substring(str.length() / 2));
            }

            else textToRender.add(str);
        }

        for (String str : textToRender) {

            if (mc.fontRenderer.getStringWidth(str) > maxLength) {
                maxLength = mc.fontRenderer.getStringWidth(str);
            }
        }

        bindTexture("tooltip");
        drawCappedRect(x + (centeredString ? - maxLength / 2 : 0), y, 0, 0, 0, maxLength + 5, 13 + ((textToRender.size() - 1) * 9), 512, 512);

        GL11.glTranslatef(0, 0, 100);
        for (int i = 0; i < textToRender.size(); i++) {

            String str = textToRender.get(i);
            mc.fontRenderer.drawString(ChatFormatting.WHITE + str, x + 3 + (centeredString ? -(int)(mc.fontRenderer.getStringWidth(str) / 2) : 0), y + 3 + (i * 9), 0xFFFFFF);
        }

        GL11.glTranslatef(0, 0, 0);
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glPopMatrix();
    }

    public static void drawCappedRect (int x, int y, int u, int v, int zLevel, int width, int height, int maxWidth, int maxHeight) {

        //TOP LEFT
        drawRect(x, y, u, v, zLevel, Math.min(width - 2, maxWidth), Math.min(height - 2, maxHeight));

        //RIGHT
        if (width <= maxWidth) drawRect(x + width - 2, y, u + maxWidth - 2, v, zLevel, 2, Math.min(height - 2, maxHeight));

        //BOTTOM
        if (height <= maxHeight) drawRect(x, y + height - 2, u, v + maxHeight - 2, zLevel, Math.min(width - 2, maxWidth), 2);

        //BOTTOM RIGHT
        if (width <= maxWidth && height <= maxHeight) drawRect(x + width - 2, y + height - 2, u + maxWidth - 2, v + maxHeight - 2, zLevel, 2, 2);
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

        //RenderHelper.func_227780_a_();
        GL11.glTranslatef(0.0F, 0.0F, 0.0F);
        itemRender.zLevel = -100;
        itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        itemRender.zLevel = 0F;
    }
}
