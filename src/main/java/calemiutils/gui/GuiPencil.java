package calemiutils.gui;

import calemiutils.block.BlockBlueprint;
import calemiutils.gui.base.GuiBlueprintButton;
import calemiutils.gui.base.GuiScreenBase;
import calemiutils.init.InitBlocks;
import calemiutils.item.ItemPencil;
import calemiutils.util.helper.GuiHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiPencil extends GuiScreenBase {

    private final GuiBlueprintButton[] buttons = new GuiBlueprintButton[16];

    private final ItemStack stack;

    public GuiPencil(PlayerEntity player, ItemStack stack) {

        super(player);
        this.stack = stack;
    }

    @Override
    public String getGuiTextureName() {

        return null;
    }

    @Override
    public int getGuiSizeX() {

        return 0;
    }

    @Override
    public int getGuiSizeY() {

        return 0;
    }

    @Override
    protected void init() {

        super.init();

        BlockBlueprint blueprint = (BlockBlueprint) InitBlocks.BLUEPRINT;

        for (int i = 0; i < buttons.length; i++) {
            addButton(new GuiBlueprintButton(getScreenX() + (i * 20) - 158, getScreenY() - 8, itemRenderer, new ItemStack(InitBlocks.BLUEPRINT, 1), (p_213021_1_) -> {
                if (stack != null) {

                    ItemPencil pencil = (ItemPencil) stack.getItem();

                    //CalemiUtils.network.sendToServer(new ServerPacketHandler("pencil-setcolor%" + i));
                    //pencil.setColorByMeta(stack, i);

                    minecraft.player.closeScreen();
                }
            }));
        }
    }

    @Override
    public void drawGuiBackground(int mouseX, int mouseY) {

        for (int i = 0; i < DyeColor.values().length; i++) {

            int color = DyeColor.byId(i).getColorValue();
            GuiHelper.drawColoredRect(getScreenX() + (i * 20) - 160, 0, 0, 20, this.height, color, 0.4F);
        }

        GuiHelper.drawCenteredString("Choose a Color", getScreenX(), getScreenY() - 25, 0xFFFFFF);
    }

    @Override
    public void drawGuiForeground(int mouseX, int mouseY) {

    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean canCloseWithInvKey() {
        return true;
    }
}
