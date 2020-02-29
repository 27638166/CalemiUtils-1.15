package calemiutils.gui;

import calemiutils.CalemiUtils;
import calemiutils.block.BlockBlueprint;
import calemiutils.gui.base.GuiBlueprintButton;
import calemiutils.gui.base.GuiScreenBase;
import calemiutils.init.InitBlocks;
import calemiutils.init.InitItems;
import calemiutils.item.ItemPencil;
import calemiutils.packet.PacketPencilSetColor;
import calemiutils.util.helper.GuiHelper;
import calemiutils.util.helper.ItemHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiPencil extends GuiScreenBase {

    private final GuiBlueprintButton[] buttons = new GuiBlueprintButton[16];

    private final ItemStack stack;
    private final Hand hand;

    public GuiPencil(PlayerEntity player, ItemStack stack, Hand hand) {

        super(player);
        this.stack = stack;
        this.hand = hand;
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

        for (int i = 0; i < buttons.length; i++) {

            int id = i;

            ItemStack stack = new ItemStack(InitItems.PENCIL);

            ItemHelper.getNBT(stack).putInt("color", id);

            addButton(new GuiBlueprintButton(id,getScreenX() + (i * 20) - 158, getScreenY() - 8, itemRenderer, stack, (p_213021_1_) -> {
                if (stack != null) {

                    ItemPencil pencil = (ItemPencil) stack.getItem();

                    CalemiUtils.network.sendToServer(new PacketPencilSetColor(id, hand != Hand.MAIN_HAND));
                    pencil.setColorById(stack, id);

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
