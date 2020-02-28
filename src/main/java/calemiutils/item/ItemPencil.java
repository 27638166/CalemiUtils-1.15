package calemiutils.item;

import calemiutils.CalemiUtils;
import calemiutils.block.BlockBlueprint;
import calemiutils.gui.GuiPencil;
import calemiutils.init.InitBlocks;
import calemiutils.item.base.ItemBase;
import calemiutils.util.Location;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.LoreHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLClientLaunchProvider;
import net.minecraftforge.fml.loading.FMLCommonLaunchHandler;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPencil extends ItemBase {

    public ItemPencil() {
        super("pencil", new Item.Properties().group(CalemiUtils.TAB).maxStackSize(1));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltipList, ITooltipFlag advanced) {

        LoreHelper.addInformationLore(tooltipList, "Places Blueprint. Blueprint can be used for mass building!");
        LoreHelper.addControlsLore(tooltipList, "Place Blueprint", LoreHelper.Type.USE, true);
        LoreHelper.addControlsLore(tooltipList, "Change Blueprint Color", LoreHelper.Type.SNEAK_USE);
        LoreHelper.addBlankLine(tooltipList);
        tooltipList.add(new StringTextComponent(ChatFormatting.GRAY + "Color: " + ChatFormatting.AQUA + (DyeColor.byId(getColorId(stack)).getName()).toUpperCase()));
    }

    public int getColorId(ItemStack stack) {

        int meta = 11;

        if (ItemHelper.getNBT(stack).contains("color")) {
            meta = ItemHelper.getNBT(stack).getInt("color");
        }

        return meta;
    }

    public void setColorById(ItemStack stack, int meta) {

        ItemHelper.getNBT(stack).putInt("color", meta);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {

        World world = context.getWorld();
        BlockPos pos = context.getPos();
        PlayerEntity player = context.getPlayer();
        Direction dir = context.getFace();
        Hand hand = context.getHand();

        BlockBlueprint BLUEPRINT = (BlockBlueprint) InitBlocks.BLUEPRINT;
        Location location = new Location(world, pos);

        if (player.isCrouching()) {
            return ActionResultType.FAIL;
        }

        if (!location.getBlock().getMaterial(location.getBlockState().getBlockState()).isReplaceable()) {

            location = new Location(location, dir);

            if (!location.isBlockValidForPlacing(InitBlocks.BLUEPRINT)) return ActionResultType.FAIL;
        }

        if (!player.canPlayerEdit(pos, dir, player.getHeldItem(hand))) return ActionResultType.FAIL;

        else {

            if (location.isBlockValidForPlacing(InitBlocks.BLUEPRINT)) {
                location.setBlock(BLUEPRINT.getStateFromId(getColorId(player.getHeldItem(hand))));
            }

            return ActionResultType.SUCCESS;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {

        ItemStack stack = player.getHeldItemMainhand();

        if (world.isRemote && player.isCrouching() && !stack.isEmpty() && stack.getItem() instanceof ItemPencil) {
            openGui(player, stack);
            return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
        }

        return new ActionResult<>(ActionResultType.FAIL, player.getHeldItem(hand));
    }

    @OnlyIn(Dist.CLIENT)
    private void openGui(PlayerEntity player, ItemStack stack) {

        Minecraft.getInstance().displayGuiScreen(new GuiPencil(player, stack));
    }
}
