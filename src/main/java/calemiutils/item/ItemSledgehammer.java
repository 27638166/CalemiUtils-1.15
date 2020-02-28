package calemiutils.item;

import calemiutils.init.InitEnchantments;
import calemiutils.init.InitItems;
import calemiutils.util.Location;
import calemiutils.util.VeinScan;
import calemiutils.util.helper.LoreHelper;
import calemiutils.util.helper.WorldEditHelper;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeBlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ItemSledgehammer extends PickaxeItem {

    private static final ResourceLocation oreTags = new ResourceLocation(ForgeMod.getInstance().getModId(), "ores");
    private static final ResourceLocation logTags = new ResourceLocation("minecraft", "logs");

    private double attackSpeed, attackDamage;
    public int baseChargeTime;
    public int chargeTime;

    public ItemSledgehammer(String name, Properties properties, IItemTier tier, int baseChargeTime, float attackSpeed) {
        super(tier, 0, attackSpeed, properties);

        String realName = "sledgehammer_" + name;
        setRegistryName(realName);

        this.chargeTime = baseChargeTime;
        this.baseChargeTime = baseChargeTime;

        this.attackSpeed = attackSpeed;
        this.attackDamage = 2 + tier.getAttackDamage();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltipList, ITooltipFlag advanced) {
        LoreHelper.addInformationLore(tooltipList, "Need a pickaxe, axe, shovel and sword in one single tool? This is your best bet.");
        LoreHelper.addControlsLore(tooltipList, "Charge", LoreHelper.Type.USE, true);
        LoreHelper.addControlsLore(tooltipList, "Excavates, Mines Veins & Fells Trees", LoreHelper.Type.RELEASE_USE);

        if (stack.isEnchanted()) {
            LoreHelper.addBlankLine(tooltipList);
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {

        return this == InitItems.SLEDGEHAMMER_STARLIGHT || stack.isEnchanted();
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack heldStack, World world, LivingEntity e, int timeLeft) {

        PlayerEntity player = (PlayerEntity)e;

        Hand hand = Hand.OFF_HAND;

        if (ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), heldStack)) {
            hand = Hand.MAIN_HAND;
        }

        if (getUseDuration(heldStack) - timeLeft >= chargeTime) {

            player.swingArm(hand);

            Vec3d posVec = new Vec3d(player.getPositionVector().x, player.getPositionVector().y + player.getEyeHeight(), player.getPositionVector().z);

            Vec3d lookVec = player.getLookVec();
            Direction dir = Direction.getFacingFromVector(lookVec.x, lookVec.y, lookVec.z);

            BlockRayTraceResult trace = world.rayTraceBlocks(new RayTraceContext(posVec, posVec.add(lookVec.scale(5)), RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, player));
            ItemUseContext itemUseContext = new ItemUseContext(player, hand, trace);
            BlockItemUseContext blockUseContext = new BlockItemUseContext(itemUseContext);

            if (trace.getType() == RayTraceResult.Type.BLOCK) {

                BlockPos pos = blockUseContext.getPos();
                Location locationOffset = new Location(world, pos);
                player.spawnSweepParticles();

                BlockPos difference = locationOffset.getBlockPos().subtract(itemUseContext.getPos());
                Direction blockSide = Direction.getFacingFromVector(difference.getX(), difference.getY(), difference.getZ());

                Location locationReal = locationOffset.translate(blockSide.getOpposite(), 1);

                if (Objects.requireNonNull(ItemTags.getCollection().get(oreTags)).contains(locationReal.getBlock().asItem())) {

                    veinMine(heldStack, player, locationReal);
                    return;
                }

                if (Objects.requireNonNull(ItemTags.getCollection().get(logTags)).contains(locationReal.getBlock().asItem())) {
                    veinMine(heldStack, player, locationReal);
                    return;
                }

                excavateRock(world, heldStack, player, locationReal, blockSide);
            }
        }
    }

    private void excavateRock(World worldIn, ItemStack heldStack, PlayerEntity player, Location location, Direction face) {

        int radius = EnchantmentHelper.getEnchantmentLevel(InitEnchantments.CRUSHING, heldStack) + 1;

        ArrayList<Location> locations = WorldEditHelper.selectFlatCubeFromFace(location, face, radius);

        int damage = getDamage(heldStack);

        for (Location nextLocation : locations) {

            int maxDamage = getMaxDamage(heldStack);

            if (damage > maxDamage && maxDamage > 0) {
                return;
            }

            if (canBreakBlock(nextLocation)) {
                nextLocation.breakBlock(player, heldStack);
                heldStack.damageItem(1, player, (p_220038_0_) -> {});
                damage++;
            }
        }
    }

    private void veinMine(ItemStack heldStack, PlayerEntity player, Location location) {

        if (canBreakBlock(location)) {

            IForgeBlockState state = location.getBlockState();

            VeinScan scan = new VeinScan(location, state.getBlockState().getBlock());
            scan.startScan(64, true);

            int damage = getDamage(heldStack);

            for (Location nextLocation : scan.buffer) {

                int maxDamage = getMaxDamage(heldStack);

                if (damage > getMaxDamage(heldStack) && maxDamage > 0) {
                    return;
                }

                nextLocation.breakBlock(player, heldStack);
                heldStack.damageItem(1, player, (p_220038_0_) -> {});
                damage++;
            }
        }
    }

    private boolean canBreakBlock(Location location) {

        float hardness = location.getBlockState().getBlockState().getBlockHardness(location.world, location.getBlockPos());
        int harvestLevel = location.getBlockState().getHarvestLevel();

        return hardness >= 0 && hardness <= 50 && getTier().getHarvestLevel() >= harvestLevel;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {

        ItemStack itemstack = player.getHeldItem(hand);

        chargeTime = Math.max(1, baseChargeTime - EnchantmentHelper.getEfficiencyModifier(player) * 3);

        player.setActiveHand(hand);
        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {

        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EquipmentSlotType.MAINHAND) {

            multimap.clear();
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", attackDamage, AttributeModifier.Operation.ADDITION));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", attackSpeed - 4, AttributeModifier.Operation.ADDITION));
        }

        return multimap;
    }

    @Override
    public Set<ToolType> getToolTypes(ItemStack stack) {
        return ImmutableSet.of(ToolType.PICKAXE, ToolType.AXE, ToolType.SHOVEL);
    }

    @Override
    public boolean canHarvestBlock(BlockState blockState) {
        return getTier().getHarvestLevel() >= blockState.getHarvestLevel();
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState blockState) {
        return this.efficiency;
    }
}
