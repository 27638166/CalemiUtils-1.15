package calemiutils.render;

import calemiutils.block.BlockBookStand;
import calemiutils.tileentity.TileEntityBookStand;
import calemiutils.util.Location;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderBookStand extends TileEntityRenderer<TileEntityBookStand> {

    public RenderBookStand (TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void func_225616_a_ (TileEntityBookStand tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {

        if (!tileEntity.getInventory().getStackInSlot(0).isEmpty()) {

            ItemStack bookStack = tileEntity.getInventory().getStackInSlot(0);
            Location location = tileEntity.getLocation();

            Direction dir = location.getBlockState().getBlockState().get(BlockBookStand.FACING);
            int rotation = 0;

            switch (dir) {
                case EAST:
                    rotation = -90;
                    break;
                case SOUTH:
                    rotation = -180;
                    break;
                case WEST:
                    rotation = -270;
            }

            //Push
            matrixStack.func_227860_a_();

            //Translate
            matrixStack.func_227861_a_(0.5D, 0.505D, 0.5D);
            //Rotate
            matrixStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(rotation));
            //Scale
            matrixStack.func_227862_a_(2F, 2F, 2F);

            renderItem(bookStack, partialTicks, matrixStack, buffer, combinedLight);

            //Pop
            matrixStack.func_227865_b_();
        }
    }

    private void renderItem (ItemStack stack, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight) {
        Minecraft.getInstance().getItemRenderer().func_229110_a_(stack, ItemCameraTransforms.TransformType.GROUND, combinedLight, OverlayTexture.field_229196_a_, matrixStack, buffer);
    }
}
