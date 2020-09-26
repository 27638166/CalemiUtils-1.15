package calemiutils.gui.base;

import calemiutils.inventory.base.ContainerBase;
import calemiutils.security.ISecurity;
import calemiutils.tileentity.base.ICurrencyNetworkBank;
import calemiutils.tileentity.base.IProgress;
import calemiutils.tileentity.base.TileEntityInventoryBase;
import calemiutils.tileentity.base.TileEntityUpgradable;
import calemiutils.util.helper.MathHelper;
import calemiutils.util.helper.ScreenHelper;
import calemiutils.util.helper.StringHelper;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public abstract class ContainerScreenBase<T extends ContainerBase> extends ContainerScreen<Container> {

    protected static final int TEXT_COLOR = 0x555555;
    protected final PlayerInventory playerInventory;
    protected final PlayerEntity player;
    private final Container container;
    public int leftTabOffset;
    private int rightTabOffset;

    private int currentProgress;

    protected ContainerScreenBase (Container container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = getGuiSizeX();
        this.ySize = getGuiSizeY();
        this.container = container;
        this.playerInventory = playerInventory;
        this.player = playerInventory.player;

        leftTabOffset = 4;
        rightTabOffset = 4;
    }

    protected int getGuiSizeX () {
        return 176;
    }

    protected int getGuiSizeY () {
        return 176;
    }

    protected abstract void drawGuiForeground (int mouseX, int mouseY);

    private void addUpgradeSlot (int index) {

        ScreenHelper.bindGuiTextures();
        addRightInfoText("", 15, 22);

        ScreenHelper.bindGuiTextures();
        ScreenHelper.drawRect(getScreenX() + getGuiSizeX() + 1, getScreenY() + 6 + (index * 24), (index * 18), 19, 0, 18, 18);
    }

    private void addProgressBar (int progress, int maxProgress) {

        ScreenHelper.bindGuiTextures();
        ScreenRect rect = new ScreenRect(getScreenX() - 13, getScreenY() + leftTabOffset, 13, 35);

        int scale = MathHelper.scaleInt(progress, maxProgress, 26);

        ScreenHelper.drawRect(rect.x, rect.y, 0, 37, 0, rect.width, rect.height);
        ScreenHelper.drawRect(getScreenX() - 8, getScreenY() + 30 + leftTabOffset - scale, 13, 62 - scale, 0, 5, scale);
    }

    private void addProgressBarText (int mouseX, int mouseY, int progress, int maxProgress) {

        ScreenRect rect = new ScreenRect(getScreenX() - 13, getScreenY() + leftTabOffset, 13, 35);
        ScreenHelper.drawHoveringTextBox(mouseX, mouseY, 170, rect, "Progress: " + MathHelper.scaleInt(progress, maxProgress, 100) + "%");
    }

    @SuppressWarnings("SameParameterValue")
    protected void addInfoIcon (int index) {

        GL11.glDisable(GL11.GL_LIGHTING);
        ScreenHelper.bindGuiTextures();
        ScreenHelper.drawRect(getScreenX() - 13, getScreenY() + leftTabOffset, (index * 13), 72, 2, 13, 15);
    }

    protected int getScreenX () {
        return (this.width - getGuiSizeX()) / 2;
    }

    protected int getScreenY () {
        return (this.height - getGuiSizeY()) / 2;
    }

    protected void addInfoIconText (int mouseX, int mouseY, String... text) {

        ScreenRect rect = new ScreenRect(getScreenX() - 13, getScreenY() + leftTabOffset, 13, 15);
        ScreenHelper.drawHoveringTextBox(mouseX, mouseY, 170, rect, text);
    }

    protected void addCurrencyInfo (int mouseX, int mouseY, int currency, int maxCurrency) {

        if (minecraft != null) {

            String fullName = StringHelper.printCommas(currency) + " / " + StringHelper.printCurrency(maxCurrency);

            int fullWidth = minecraft.fontRenderer.getStringWidth(fullName) + 6;

            ScreenRect rect = new ScreenRect(getScreenX() - fullWidth, getScreenY() + leftTabOffset, fullWidth, 15);
            String text = StringHelper.printCurrency(currency);

            if (rect.contains(mouseX, mouseY)) {
                text = fullName;
            }

            addLeftInfoText(text, 15);
        }
    }

    @SuppressWarnings("SameParameterValue")
    protected void addRightInfoText (String text, int sizeAdd, int sizeY) {

        if (minecraft != null) {

            int width = minecraft.fontRenderer.getStringWidth(text) + sizeAdd + 7;

            ScreenHelper.bindGuiTextures();
            ScreenHelper.drawCappedRect(getScreenX() + getGuiSizeX() - 1, getScreenY() + rightTabOffset, 0, 116, -40, width, sizeY, 256, 22);

            if (!text.isEmpty()) {
                GL11.glPushMatrix();
                GL11.glTranslatef(0, 0, 5);
                GL11.glColor3f(0.35F, 0.35F, 0.35F);
                minecraft.fontRenderer.drawString(text, getScreenX() + getGuiSizeX() + 4, getScreenY() + (float) (sizeY / 2) - 3 + rightTabOffset, TEXT_COLOR);
                GL11.glColor3f(1, 1, 1);
                GL11.glPopMatrix();
            }

            rightTabOffset += (sizeY + 2);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void addLeftInfoText (String text, int sizeY) {

        if (minecraft != null) {

            int width = minecraft.fontRenderer.getStringWidth(text) + 6;

            ScreenHelper.bindGuiTextures();
            ScreenHelper.drawCappedRect(getScreenX() - width, getScreenY() + leftTabOffset, 0, 116, 10, width, sizeY, 255, 22);

            if (!text.isEmpty()) {
                GL11.glPushMatrix();
                GL11.glColor3f(0.35F, 0.35F, 0.35F);
                GL11.glTranslatef(0, 0, 5);
                minecraft.fontRenderer.drawString(text, getScreenX() - width + 4, getScreenY() + (float) (sizeY / 2) - 3 + leftTabOffset, TEXT_COLOR);
                GL11.glColor3f(1, 1, 1);
                GL11.glPopMatrix();
            }

            leftTabOffset += (sizeY + 2);
        }
    }

    private void addGraphicsBeforeRendering () {

        ScreenHelper.bindGuiTextures();

        if (getTileEntity() != null) {

            TileEntityInventoryBase tileEntity = getTileEntity();

            if (tileEntity instanceof TileEntityUpgradable) {

                TileEntityUpgradable tileEntityUpgradable = (TileEntityUpgradable) tileEntity;

                addUpgradeSlot(0);
                addUpgradeSlot(1);

                addProgressBar(currentProgress, tileEntityUpgradable.getMaxProgress());
            }
        }
    }

    private void addGraphicsAfterRendering (int mouseX, int mouseY) {

        GL11.glDisable(GL11.GL_LIGHTING);
        ScreenHelper.bindGuiTextures();

        if (minecraft != null && getTileEntity() != null) {

            TileEntityInventoryBase tileEntity = getTileEntity();

            if (tileEntity instanceof ISecurity) {

                ISecurity tileEntitySecurity = (ISecurity) tileEntity;

                String name = tileEntitySecurity.getSecurityProfile().getOwnerName();
                int width = minecraft.fontRenderer.getStringWidth(name) + 7;
                ScreenHelper.drawCappedRect(getScreenX() + (getGuiSizeX() / 2) - (width / 2), getScreenY() + getGuiSizeY() - 1, 0, 116, 0, width, 13, 256, 22);
                ScreenHelper.drawCenteredString(name, getScreenX() + (getGuiSizeX() / 2) + 1, getScreenY() + getGuiSizeY() + 2, 50, TEXT_COLOR);
                GL11.glColor3f(1, 1, 1);
            }

            if (tileEntity instanceof ICurrencyNetworkBank) {

                ICurrencyNetworkBank tileEntityBank = (ICurrencyNetworkBank) tileEntity;

                GL11.glColor3f(1, 1, 1);
                addCurrencyInfo(mouseX, mouseY, tileEntityBank.getStoredCurrency(), tileEntityBank.getMaxCurrency());
            }

            if (tileEntity instanceof IProgress) {

                IProgress tileEntityProgress = (IProgress) tileEntity;
                ContainerBase containerBase = (ContainerBase) container;

                GL11.glColor3f(1, 1, 1);
                addProgressBarText(mouseX, mouseY, currentProgress, tileEntityProgress.getMaxProgress());
            }
        }
    }

    @Override
    public void render (int mouseX, int mouseY, float f) {

        renderBackground();
        addGraphicsBeforeRendering();

        super.render(mouseX, mouseY, f);

        addGraphicsAfterRendering(mouseX, mouseY);
        drawGuiForeground(mouseX, mouseY);

        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer (float f, int mouseX, int mouseY) {

        leftTabOffset = 4;
        rightTabOffset = 4;

        ScreenHelper.bindTexture(getGuiTextureName());
        ScreenHelper.drawRect(getScreenX(), getScreenY(), 0, 0, 0, getGuiSizeX(), getGuiSizeY());

        drawGuiBackground(mouseX, mouseY);

        String name = getTitle().getFormattedText();

        if (getTileEntity() != null) {
            name = getTileEntity().getDisplayName().getFormattedText();
        }

        ScreenHelper.drawCenteredString(name, getScreenX() + getGuiSizeX() / 2, getScreenY() + 6, 5, TEXT_COLOR);
    }

    protected abstract String getGuiTextureName ();

    protected abstract void drawGuiBackground (int mouseX, int mouseY);

    @Override
    public void tick () {
        super.tick();

        if (getTileEntity() != null && getTileEntity() instanceof TileEntityUpgradable) {
            this.currentProgress = ((TileEntityUpgradable) getTileEntity()).currentProgress;
        }
    }

    protected TileEntityInventoryBase getTileEntity () {

        if (container instanceof ContainerBase) {

            ContainerBase containerBase = (ContainerBase) container;

            if (containerBase.tileEntity != null) {

                return containerBase.tileEntity;
            }
        }

        return null;
    }
}
