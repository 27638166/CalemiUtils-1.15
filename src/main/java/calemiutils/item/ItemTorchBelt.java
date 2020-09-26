package calemiutils.item;

import calemiutils.CalemiUtils;
import calemiutils.item.base.ItemBase;
import calemiutils.util.Location;
import calemiutils.util.helper.*;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTorchBelt extends ItemBase {

    public ItemTorchBelt () {
        super(new Item.Properties().group(CalemiUtils.TAB).maxStackSize(1));
    }

    @Override
    public void addInformation (ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        LoreHelper.addInformationLore(tooltip, "Place this anywhere in your inventory. Automatically uses and places torches in dark areas.", true);
        LoreHelper.addControlsLore(tooltip, "Toggle ON/OFF", LoreHelper.Type.USE, true);
        LoreHelper.addBlankLine(tooltip);
        tooltip.add(new StringTextComponent("Status: " + ChatFormatting.AQUA + (ItemHelper.getNBT(stack).getBoolean("on") ? "ON" : "OFF")));
    }

    @Override
    public boolean hasEffect (ItemStack stack) {
        return ItemHelper.getNBT(stack).getBoolean("on");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick (World worldIn, PlayerEntity playerIn, Hand handIn) {

        ItemStack stack = playerIn.getHeldItem(handIn);

        ItemHelper.getNBT(stack).putBoolean("on", !ItemHelper.getNBT(stack).getBoolean("on"));
        SoundHelper.playClick(worldIn, playerIn);

        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    @Override
    public void inventoryTick (ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        tick(stack, worldIn, entityIn);
    }

    private void tick (ItemStack stack, World worldIn, Entity entityIn) {

        if (entityIn instanceof PlayerEntity && ItemHelper.getNBT(stack).getBoolean("on")) {

            PlayerEntity player = (PlayerEntity) entityIn;

            Location location = new Location(worldIn, (int) Math.floor(player.getPosition().getX()), (int) Math.floor(player.getPosition().getY()), (int) Math.floor(player.getPosition().getZ()));

            if (location.getLightValue() <= 7) {

                if (player.abilities.isCreativeMode || player.inventory.hasItemStack(new ItemStack(Blocks.TORCH))) {

                    if (TorchHelper.canPlaceTorchAt(location)) {

                        location.setBlock(Blocks.TORCH);

                        if (!player.abilities.isCreativeMode) InventoryHelper.consumeItem(player.inventory, 1, new ItemStack(Blocks.TORCH));
                    }
                }
            }
        }
    }
}
