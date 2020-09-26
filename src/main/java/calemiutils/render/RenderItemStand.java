package calemiutils.render;

import calemiutils.block.BlockItemStand;
import calemiutils.init.InitItems;
import calemiutils.tileentity.TileEntityItemStand;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderItemStand extends TileEntityRenderer<TileEntityItemStand> {

    private long lastTime;
    private float rot;

    public RenderItemStand (TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void func_225616_a_ (TileEntityItemStand stand, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {

        if (!stand.getInventory().getStackInSlot(0).isEmpty()) {

            ItemStack bookStack = stand.getInventory().getStackInSlot(0);

            long targetTime = 10;
            if (System.currentTimeMillis() - lastTime >= targetTime) {
                lastTime = System.currentTimeMillis();
                rot += 1F;
                rot %= 360;
            }

            //Push
            matrixStack.func_227860_a_();

            //Translate
            matrixStack.func_227861_a_(0.5D + stand.translation.getX() * 0.2D, 1.2D + stand.translation.getY() * 0.2D, 0.5D + stand.translation.getZ() * 0.2D);

            //Rotate X
            matrixStack.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(stand.spin.getX() != 0 ? rot * stand.spin.getX() : stand.rotation.getX()));
            //Rotate Y
            matrixStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(stand.spin.getY() != 0 ? rot * stand.spin.getY() : stand.rotation.getY()));
            //Rotate Z
            matrixStack.func_227863_a_(Vector3f.field_229183_f_.func_229187_a_(stand.spin.getZ() != 0 ? rot * stand.spin.getZ() : stand.rotation.getZ()));
            //Pivot
            matrixStack.func_227861_a_(stand.pivot.getX() * 0.2D, stand.pivot.getY() * 0.2D, stand.pivot.getZ() * 0.2D);

            //Scale
            matrixStack.func_227862_a_(1.5F * stand.scale.getX() + 0.5F, 1.5F * stand.scale.getY() + 0.5F, 1.5F * stand.scale.getZ() + 0.5F);

            renderItem(bookStack, partialTicks, matrixStack, buffer, combinedLight);

            //Pop
            matrixStack.func_227865_b_();
        }

        //Render a floating wrench if the clear display is chosen.
        //This prevents Item Stands from getting lost.
        if (Minecraft.getInstance().player.getHeldItemMainhand().getItem() == InitItems.SECURITY_WRENCH.get()) {

            if (stand.getBlockState().get(BlockItemStand.DISPLAY_ID) == 3) {

                matrixStack.func_227860_a_();

                matrixStack.func_227861_a_(0.5D, 0.4D, 0.5D);

                matrixStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(rot));

                matrixStack.func_227862_a_(1F, 1F, 1F);

                renderItem(new ItemStack(InitItems.SECURITY_WRENCH.get()), partialTicks, matrixStack, buffer, combinedLight);

                matrixStack.func_227865_b_();
            }
        }
    }

    private void renderItem (ItemStack stack, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight) {
        Minecraft.getInstance().getItemRenderer().func_229110_a_(stack, ItemCameraTransforms.TransformType.GROUND, combinedLight, OverlayTexture.field_229196_a_, matrixStack, buffer);
    }
}
