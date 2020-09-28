package calemiutils.tool;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

public enum SledgehammerTiers implements IItemTier {

    WOOD      (20 * 11, 2F, 3F, 0, 15, 50, 1.4F, () -> {return Ingredient.fromTag(ItemTags.PLANKS);}),
    STONE     (44 * 11, 4F, 4F, 1, 5, 35, 1.4F, () -> {return Ingredient.fromItems(Items.STONE);}),
    IRON      (83 * 11, 6F, 5F, 2, 14, 25, 1.4F, () -> {return Ingredient.fromItems(Items.IRON_INGOT);}),
    GOLD      (11 * 11, 12F, 3F, 0, 22, 15, 1.4F, () -> {return Ingredient.fromItems(Items.GOLD_INGOT);}),
    DIAMOND   (520 * 11, 8F, 6F, 3, 10, 20, 1.4F, () -> {return Ingredient.fromItems(Items.DIAMOND);}),
    STARLIGHT (10000000, 20F, 13F, 5, 25, 15, 1.4F, () -> null);

    public final int durability;
    public final float efficiency;
    public final float attackDamage;
    public final int harvestLevel;
    public final int enchantability;
    public final int baseChargeTime;
    public final float attackSpeed;
    public final LazyValue<Ingredient> repairMaterial;

    @SuppressWarnings("SameParameterValue")
    SledgehammerTiers (int durability, float efficiency, float attackDamage, int harvestLevel, int enchantability, int baseChargeTime, float attackSpeed, Supplier<Ingredient> repairMaterial) {
        this.durability = durability;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.harvestLevel = harvestLevel;
        this.enchantability = enchantability;
        this.baseChargeTime = baseChargeTime;
        this.attackSpeed = attackSpeed;
        this.repairMaterial = new LazyValue<>(repairMaterial);
    }

    @Override
    public int getMaxUses () {
        return durability;
    }

    @Override
    public float getEfficiency () {
        return efficiency;
    }

    @Override
    public float getAttackDamage () {
        return attackDamage;
    }

    @Override
    public int getHarvestLevel () {
        return harvestLevel;
    }

    @Override
    public int getEnchantability () {
        return enchantability;
    }

    @Override
    public Ingredient getRepairMaterial () {
        return repairMaterial.getValue();
    }
}
