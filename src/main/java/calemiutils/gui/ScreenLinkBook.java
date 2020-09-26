package calemiutils.gui;

import calemiutils.CalemiUtils;
import calemiutils.gui.base.ButtonRect;
import calemiutils.gui.base.GuiScreenBase;
import calemiutils.gui.base.TextFieldRect;
import calemiutils.item.ItemLinkBookLocation;
import calemiutils.packet.PacketLinkBook;
import calemiutils.util.Location;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.ScreenHelper;
import calemiutils.util.helper.SoundHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenLinkBook extends GuiScreenBase {

    private final ItemStack bookStack;
    private final boolean isBookInHand;
    private TextFieldRect nameField;
    private ButtonRect resetBookBtn;
    private ButtonRect teleportBtn;

    public ScreenLinkBook (PlayerEntity player, Hand hand, ItemStack bookStack, boolean isBookInHand) {
        super(player, hand);
        this.bookStack = bookStack;
        this.isBookInHand = isBookInHand;
    }

    private void setName (String name) {

        if (isBookInHand && getBook() != null) {
            CalemiUtils.network.sendToServer(new PacketLinkBook("name", hand, name));
            ItemLinkBookLocation.bindName(bookStack, name);
        }
    }

    @Override
    protected void init () {
        super.init();

        minecraft.keyboardListener.enableRepeatEvents(true);

        if (minecraft != null && isBookInHand) {

            nameField = new TextFieldRect(minecraft.fontRenderer, getScreenX() - 80 - 8, getScreenY() - 50 - 8, 160, 32, "");
            children.add(nameField);

            if (bookStack != null) {

                if (bookStack.hasDisplayName()) {
                    nameField.setText(bookStack.getDisplayName().getFormattedText());
                }
            }

            //Set Name
            addButton(new ButtonRect(getScreenX() + 80 - 4, getScreenY() - 50 - 8, 16, "+", (btn) -> setName(nameField.getText())));

            //Bind Location
            addButton(new ButtonRect(getScreenX() - 50, getScreenY() + 35 - 8, 100, "Bind Location", (btn) -> bindLocation()));

            //Reset Book
            resetBookBtn = addButton(new ButtonRect(getScreenX() - 50, getScreenY() + 70 - 8, 100, "Reset Book", (btn) -> resetBook()));
        }

        //Teleport
        teleportBtn = addButton(new ButtonRect(getScreenX() - 50, getScreenY() - 8, 100, "Teleport", (btn) -> teleport()));
    }

    @Override
    public void tick () {
        super.tick();

        Location location = ItemLinkBookLocation.getLinkedLocation(player.world, bookStack);

        teleportBtn.active = location != null;
        if (isBookInHand) resetBookBtn.active = location != null;
    }

    @Override
    public boolean isPauseScreen () {
        return false;
    }

    private void bindLocation () {
        setName(nameField.getText());

        BlockPos pos = new BlockPos((int) Math.floor(player.getPosition().getX()), (int) Math.floor(player.getPosition().getY()), (int) Math.floor(player.getPosition().getZ()));
        Location location = new Location(player.world, pos);
        int dim = player.world.getDimension().getType().getId();

        CalemiUtils.network.sendToServer(new PacketLinkBook("bind", hand, pos));
        ItemLinkBookLocation.bindLocation(bookStack, player, location, true);
    }

    private void resetBook () {
        setName(nameField.getText());

        setName("");
        CalemiUtils.network.sendToServer(new PacketLinkBook("reset", hand));
        ItemLinkBookLocation.resetLocation(bookStack, player);
        nameField.setText("");
    }

    private void teleport () {

        if (getBook() != null && getBook().isLinked(bookStack)) {

            Location location = ItemLinkBookLocation.getLinkedLocation(player.world, bookStack);

            if (location != null) {

                BlockPos pos = location.getBlockPos();
                int dim = ItemLinkBookLocation.getLinkedDimensionId(bookStack);

                SoundHelper.playWarp(player.world, player, location);
                SoundHelper.playWarp(player.world, player);
                CalemiUtils.network.sendToServer(new PacketLinkBook("teleport", hand, pos, dim));

                player.closeScreen();
            }
        }
    }

    @Override
    public String getGuiTextureName () {
        return null;
    }

    @Override
    public int getGuiSizeX () {
        return 0;
    }

    @Override
    public int getGuiSizeY () {
        return 0;
    }

    @Override
    public void drawGuiBackground (int mouseX, int mouseY) {

        if (isBookInHand) {
            nameField.render(mouseX, mouseY, 0);
            ScreenHelper.drawCenteredString("Name Book", getScreenX(), getScreenY() - 67, 0, 0xFFFFFF);
        }

        if (getBook() != null) {

            CompoundNBT nbt = ItemHelper.getNBT(bookStack);

            Location location = ItemLinkBookLocation.getLinkedLocation(player.world, bookStack);
            String string = "Not set";

            if (getBook().isLinked(bookStack) && location != null) {
                ScreenHelper.drawCenteredString(bookStack.getDisplayName().getFormattedText(), getScreenX(), getScreenY() - 28, 0, 0xFFFFFF);
                string = nbt.getString("DimName") + " " + location.toString();
            }

            ScreenHelper.drawCenteredString(string, getScreenX(), getScreenY() - 18, 0, 0xFFFFFF);
        }
    }

    private ItemLinkBookLocation getBook () {

        if (bookStack.getItem() instanceof ItemLinkBookLocation) {
            return (ItemLinkBookLocation) bookStack.getItem();
        }

        return null;
    }

    @Override
    public void drawGuiForeground (int mouseX, int mouseY) {}

    @Override
    public boolean canCloseWithInvKey () {
        return !isBookInHand || !nameField.isFocused();
    }
}
