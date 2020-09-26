package calemiutils.render;

import calemiutils.tileentity.TileEntityTradingPost;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderTradingPost extends TileEntityRenderer<TileEntityTradingPost> {

    private long lastTime;
    private float rot;

    public RenderTradingPost (TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void func_225616_a_ (TileEntityTradingPost te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {

        if (te.getStackForSale() != null) {

            ItemStack stackForSale = te.getStackForSale();

            long targetTime = 10;
            if (System.currentTimeMillis() - lastTime >= targetTime) {
                lastTime = System.currentTimeMillis();
                rot += 1F;
                rot %= 360;
            }

            //Push
            matrixStack.func_227860_a_();

            float offset = 0;
            float scale = 1;

            if (te.getStackForSale().getItem() instanceof BlockItem) {
                offset = -0.125F;
                scale = 1.5F;
            }

            //Translate
            matrixStack.func_227861_a_(0.5D, 0.5D + offset, 0.5D);
            //Rotate
            matrixStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(rot));
            //Scale
            matrixStack.func_227862_a_(scale, scale, scale);

            //Render Item
            renderItem(stackForSale, partialTicks, matrixStack, buffer, combinedLight);

            //Pop
            matrixStack.func_227865_b_();


        }
    }

    private void renderItem (ItemStack stack, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight) {
        Minecraft.getInstance().getItemRenderer().func_229110_a_(stack, ItemCameraTransforms.TransformType.GROUND, combinedLight, OverlayTexture.field_229196_a_, matrixStack, buffer);
    }
}
