package calemiutils.tileentity;

import calemiutils.gui.ScreenTradingPost;
import calemiutils.init.InitTileEntityTypes;
import calemiutils.inventory.ContainerTradingPost;
import calemiutils.security.ISecurity;
import calemiutils.security.SecurityProfile;
import calemiutils.tileentity.base.ICurrencyNetworkUnit;
import calemiutils.tileentity.base.ITileEntityGuiHandler;
import calemiutils.tileentity.base.TileEntityInventoryBase;
import calemiutils.util.Location;
import calemiutils.util.UnitChatMessage;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.NetworkHelper;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityTradingPost extends TileEntityInventoryBase implements ITileEntityGuiHandler, ICurrencyNetworkUnit, ISecurity {

    private final SecurityProfile profile = new SecurityProfile();
    public int amountForSale;
    public int salePrice;
    public boolean hasValidTradeOffer;
    public boolean adminMode = false;
    public boolean buyMode = false;
    private Location bankLocation;
    private ItemStack stackForSale = ItemStack.EMPTY;

    public TileEntityTradingPost () {
        super(InitTileEntityTypes.TRADING_POST.get());
        amountForSale = 1;
        salePrice = 0;
        hasValidTradeOffer = false;
    }

    public int getStoredCurrencyInBank () {

        if (getBank() != null) {
            return getBank().getStoredCurrency();
        }

        return 0;
    }

    public TileEntityBank getBank () {
        TileEntityBank bank = NetworkHelper.getConnectedBank(getLocation(), bankLocation);
        if (bank == null) bankLocation = null;
        return bank;
    }

    public void addStoredCurrencyInBank (int amount) {

        if (getBank() != null) {
            getBank().setCurrency(getBank().getStoredCurrency() + amount);
        }
    }

    public void decrStoredCurrencyInBank (int amount) {

        if (getBank() != null) {
            getBank().addCurrency(-amount);
        }
    }

    @Override
    public void tick () {
        super.tick();

        hasValidTradeOffer = getStackForSale() != null && !getStackForSale().isEmpty() && amountForSale >= 1;
    }

    public UnitChatMessage getUnitName (PlayerEntity player) {

        if (adminMode) {
            return new UnitChatMessage("Admin Post", player);
        }

        return new UnitChatMessage(getSecurityProfile().getOwnerName() + "'s Trading Post", player);
    }

    public ItemStack getStackForSale () {
        return stackForSale;
    }

    public void setStackForSale (ItemStack stack) {
        stackForSale = stack;
    }

    public int getStock () {

        if (getStackForSale() != null) {

            int count = 0;

            for (int i = 0; i < getSizeInventory(); i++) {

                if (getInventory().getStackInSlot(i).isItemEqual(getStackForSale())) {

                    if (getStackForSale().hasTag()) {

                        if (getInventory().getStackInSlot(i).hasTag() && getInventory().getStackInSlot(i).getTag().equals(getStackForSale().getTag())) {
                            count += getInventory().getStackInSlot(i).getCount();
                        }
                    }

                    else count += getInventory().getStackInSlot(i).getCount();
                }
            }

            return count;
        }

        return 0;
    }

    @Override
    public Location getBankLocation () {
        return bankLocation;
    }

    @Override
    public void setBankLocation (Location location) {
        bankLocation = location;
    }

    @Override
    public int getSizeInventory () {
        return 27;
    }

    @Override
    public SecurityProfile getSecurityProfile () {
        return profile;
    }

    @Override
    public Direction[] getConnectedDirections () {
        return new Direction[] {Direction.DOWN};
    }

    @Override
    public ITextComponent getDefaultName () {
        return new StringTextComponent("Trading Post");
    }

    @Override
    public Container getTileContainer (int windowId, PlayerInventory playerInv) {
        return new ContainerTradingPost(windowId, playerInv, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ContainerScreen getTileGuiContainer (int windowId, PlayerInventory playerInv) {
        return new ScreenTradingPost(getTileContainer(windowId, playerInv), playerInv, new StringTextComponent("Wallet"));
    }

    @Override
    public void read (CompoundNBT nbt) {

        super.read(nbt);

        amountForSale = nbt.getInt("amount");
        salePrice = nbt.getInt("price");

        stackForSale = ItemHelper.getStackFromString(nbt.getString("stack"));

        if (!nbt.getString("nbt").isEmpty()) {
            ItemHelper.attachNBTFromString(stackForSale, nbt.getString("nbt"));
        }

        adminMode = nbt.getBoolean("adminMode");
        buyMode = nbt.getBoolean("buyMode");
    }

    @Override
    public CompoundNBT write (CompoundNBT nbt) {

        super.write(nbt);

        nbt.putInt("amount", amountForSale);
        nbt.putInt("price", salePrice);

        nbt.putString("stack", ItemHelper.getStringFromStack(stackForSale));

        String nbtString = "";

        if (stackForSale.hasTag()) {
            nbtString = stackForSale.getTag().toString();
        }

        nbt.putString("nbt", nbtString);

        nbt.putBoolean("adminMode", adminMode);
        nbt.putBoolean("buyMode", buyMode);

        return nbt;
    }
}
