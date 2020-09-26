package calemiutils.item;

import calemiutils.CUConfig;
import calemiutils.CalemiUtils;
import calemiutils.item.base.ItemBase;
import calemiutils.util.helper.*;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.FoodStats;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlender extends ItemBase {

    public ItemBlender () {
        super(new Item.Properties().group(CalemiUtils.TAB).maxStackSize(1));
    }

    @Override
    public void addInformation (ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        LoreHelper.addInformationLore(tooltip, "Blends up food into juice which you can drink!", true);
        LoreHelper.addControlsLore(tooltip, "Drink", LoreHelper.Type.USE);
        LoreHelper.addControlsLore(tooltip, "Toggle Processing Mode", LoreHelper.Type.SNEAK_USE, true);
        LoreHelper.addBlankLine(tooltip);
        tooltip.add(new StringTextComponent("Process Food: " + ChatFormatting.AQUA + (ItemHelper.getNBT(stack).getBoolean("process") ? "ON" : "OFF")));
        tooltip.add(new StringTextComponent("Juice: " + ChatFormatting.AQUA + StringHelper.printCommas((int) ItemHelper.getNBT(stack).getFloat("juice")) + " / " + StringHelper.printCommas(CUConfig.misc.blenderMaxJuice.get())));
    }

    @Override
    public boolean hasEffect (ItemStack stack) {
        return ItemHelper.getNBT(stack).getBoolean("process");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick (World worldIn, PlayerEntity playerIn, Hand handIn) {

        ItemStack stack = playerIn.getHeldItem(handIn);
        float juice = ItemHelper.getNBT(stack).getFloat("juice");

        if (playerIn.isCrouching()) {

            ItemHelper.getNBT(stack).putBoolean("process", !ItemHelper.getNBT(stack).getBoolean("process"));
            SoundHelper.playClick(worldIn, playerIn);

            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }

        else {

            if (playerIn.getFoodStats().needFood() && juice >= 1) {

                playerIn.setActiveHand(handIn);

                return new ActionResult<>(ActionResultType.SUCCESS, stack);
            }
        }

        return new ActionResult<>(ActionResultType.FAIL, stack);
    }

    @Override
    public ItemStack onItemUseFinish (ItemStack stack, World worldIn, LivingEntity entityLiving) {

        CompoundNBT nbt = ItemHelper.getNBT(stack);

        while (true) {

            FoodStats stats = ((PlayerEntity) entityLiving).getFoodStats();

            if (!stats.needFood()) {
                break;
            }

            float juice = nbt.getFloat("juice");

            int missingFood = 20 - stats.getFoodLevel();

            int addedFood = 0;
            int addedSat = 0;

            if (juice >= 1) {

                if (missingFood > 0) {
                    addedFood += 1;
                    addedSat += 2;
                }

                decreaseJuice(nbt, 1);

                stats.addStats(addedFood, addedSat);
            }

            else {
                break;
            }
        }

        return stack;
    }

    @Override
    public void inventoryTick (ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

        if (ItemHelper.getNBT(stack).getBoolean("process")) {

            if (entityIn instanceof PlayerEntity) {

                PlayerEntity player = (PlayerEntity) entityIn;

                for (int i = 0; i < player.inventory.getSizeInventory(); i++) {

                    ItemStack currentStack = player.inventory.getStackInSlot(i);

                    if (!currentStack.isEmpty() && currentStack.isFood() && currentStack.getItem().getFood() != null) {

                        float food = (float) (currentStack.getItem().getFood().getHealing()) / 2;

                        if (ItemHelper.getNBT(stack).getFloat("juice") + food <= CUConfig.misc.blenderMaxJuice.get()) {

                            InventoryHelper.consumeItem(player.inventory, 1, currentStack);
                            ItemHelper.getNBT(stack).putFloat("juice", ItemHelper.getNBT(stack).getFloat("juice") + food);
                        }
                    }
                }
            }
        }
    }

    @Override
    public UseAction getUseAction (ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getUseDuration (ItemStack stack) {
        return 64;
    }

    @SuppressWarnings("SameParameterValue")
    private void decreaseJuice (CompoundNBT nbt, float amount) {
        nbt.putFloat("juice", nbt.getFloat("juice") - amount);
    }
}
