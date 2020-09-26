package calemiutils.gui;

import calemiutils.CalemiUtils;
import calemiutils.gui.base.ButtonRect;
import calemiutils.gui.base.ContainerScreenBase;
import calemiutils.gui.base.FakeSlot;
import calemiutils.inventory.ContainerTradingPost;
import calemiutils.packet.PacketTradingPost;
import calemiutils.tileentity.TileEntityTradingPost;
import calemiutils.tileentity.base.ICurrencyNetworkBank;
import calemiutils.util.helper.*;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class ScreenTradingPost extends ContainerScreenBase<ContainerTradingPost> {

    private final TileEntityTradingPost tePost;
    private final int upY = 40;
    private final int downY = 59;
    private ButtonRect sellModeButton;
    private FakeSlot fakeSlot;

    public ScreenTradingPost (Container container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, new StringTextComponent("Wallet"));
        tePost = (TileEntityTradingPost) getTileEntity();
    }

    @Override
    public int getGuiSizeY () {
        return 223;
    }

    @Override
    protected void drawGuiForeground (int mouseX, int mouseY) {

        if (tePost.getBank() != null) {
            GL11.glColor3f(1, 1, 1);
            addCurrencyInfo(mouseX, mouseY, ((ICurrencyNetworkBank) tePost.getBank()).getStoredCurrency(), ((ICurrencyNetworkBank) tePost.getBank()).getMaxCurrency());
        }

        GL11.glDisable(GL11.GL_LIGHTING);
        fakeSlot.renderButton(mouseX, mouseY, 150);

        addInfoIcon(0);
        addInfoIconText(mouseX, mouseY, "Button Click Info", "Shift: 10, Ctrl: 100, Shift + Ctrl: 1,000");
    }

    @Override
    public String getGuiTextureName () {
        return "trading_post";
    }

    @Override
    protected void drawGuiBackground (int mouseX, int mouseY) {

        GL11.glPushMatrix();
        GL11.glTranslatef(0, 0, 50);

        // Titles
        minecraft.fontRenderer.drawString("Amount", getScreenX() + 10, getScreenY() + upY + 4, TEXT_COLOR);
        minecraft.fontRenderer.drawString("Price", getScreenX() + 10, getScreenY() + downY + 4, TEXT_COLOR);

        ScreenHelper.drawCenteredString(StringHelper.printCommas(tePost.amountForSale), getScreenX() + getGuiSizeX() / 2, getScreenY() + upY + 4, 0, TEXT_COLOR);
        ScreenHelper.drawCenteredString(StringHelper.printCommas(tePost.salePrice), getScreenX() + getGuiSizeX() / 2, getScreenY() + downY + 4, 0, TEXT_COLOR);

        GL11.glPopMatrix();

        sellModeButton.setMessage(tePost.buyMode ? "Buying" : "Selling");
    }

    @Override
    protected void init () {
        super.init();

        //Subtract Amount
        addButton(new ButtonRect(getScreenX() + 49, getScreenY() + upY, 16, "-", (btn) -> {
            int i = ShiftHelper.getShiftCtrlInt(1, 10, 100, 1000);
            changeAmount(-i);
        }));

        //Add Amount
        addButton(new ButtonRect(getScreenX() + 111, getScreenY() + upY, 16, "+", (btn) -> {
            int i = ShiftHelper.getShiftCtrlInt(1, 10, 100, 1000);
            changeAmount(i);
        }));

        //Subtract Price
        addButton(new ButtonRect(getScreenX() + 49, getScreenY() + downY, 16, "-", (btn) -> {
            int i = ShiftHelper.getShiftCtrlInt(1, 10, 100, 1000);
            changePrice(-i);
        }));

        //Add Price
        addButton(new ButtonRect(getScreenX() + 111, getScreenY() + downY, 16, "+", (btn) -> {
            int i = ShiftHelper.getShiftCtrlInt(1, 10, 100, 1000);
            changePrice(i);
        }));

        //Reset Amount
        addButton(new ButtonRect(getScreenX() + 130, getScreenY() + upY, 16, "R", (btn) -> resetAmount()));

        //Reset Price
        addButton(new ButtonRect(getScreenX() + 130, getScreenY() + downY, 16, "R", (btn) -> resetPrice()));

        sellModeButton = addButton(new ButtonRect(getScreenX() + 21, getScreenY() + 19, 39, tePost.buyMode ? "Buying" : "Selling", (btn) -> toggleMode()));

        fakeSlot = addButton(new FakeSlot(getScreenX() + 80, getScreenY() + 19, itemRenderer, (btn) -> setFakeSlot()));
        fakeSlot.setItemStack(tePost.getStackForSale());
    }

    private void changeAmount (int change) {
        int value = MathHelper.clamp(tePost.amountForSale + change, 1, 64);
        tePost.amountForSale = value;
        CalemiUtils.network.sendToServer(new PacketTradingPost("syncoptions", tePost.getPos(), value, tePost.salePrice));
    }

    private void changePrice (int change) {
        int value = MathHelper.clamp(tePost.salePrice + change, 0, 9999);
        tePost.salePrice = value;
        CalemiUtils.network.sendToServer(new PacketTradingPost("syncoptions", tePost.getPos(), tePost.amountForSale, value));
    }

    private void resetAmount () {
        tePost.amountForSale = 0;
        CalemiUtils.network.sendToServer(new PacketTradingPost("syncoptions", tePost.getPos(), 0, tePost.salePrice));
    }

    private void resetPrice () {
        tePost.salePrice = 0;
        CalemiUtils.network.sendToServer(new PacketTradingPost("syncoptions", tePost.getPos(), tePost.amountForSale, 0));
    }

    private void toggleMode () {
        boolean mode = !tePost.buyMode;
        CalemiUtils.network.sendToServer(new PacketTradingPost("syncmode", tePost.getPos(), mode));
        tePost.buyMode = mode;
    }

    private void setFakeSlot () {

        ItemStack stack = new ItemStack(playerInventory.getItemStack().getItem(), 1);
        if (playerInventory.getItemStack().hasTag()) stack.setTag(playerInventory.getItemStack().getTag());

        CalemiUtils.network.sendToServer(new PacketTradingPost("syncstack", tePost.getPos(), ItemHelper.getStringFromStack(stack), stack.hasTag() ? stack.getTag().toString() : ""));
        tePost.setStackForSale(stack);
        fakeSlot.setItemStack(stack);
    }
}
