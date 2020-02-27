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
    private static final Set<Block> EFFECTIVE_ON = ImmutableSet.of(Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.POWERED_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.BLUE_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.CHISELED_SANDSTONE, Blocks.CUT_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE, Blocks.CUT_RED_SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.GRANITE, Blocks.POLISHED_GRANITE, Blocks.DIORITE, Blocks.POLISHED_DIORITE, Blocks.ANDESITE, Blocks.POLISHED_ANDESITE, Blocks.STONE_SLAB, Blocks.SMOOTH_STONE_SLAB, Blocks.SANDSTONE_SLAB, Blocks.PETRIFIED_OAK_SLAB, Blocks.COBBLESTONE_SLAB, Blocks.BRICK_SLAB, Blocks.STONE_BRICK_SLAB, Blocks.NETHER_BRICK_SLAB, Blocks.QUARTZ_SLAB, Blocks.RED_SANDSTONE_SLAB, Blocks.PURPUR_SLAB, Blocks.SMOOTH_QUARTZ, Blocks.SMOOTH_RED_SANDSTONE, Blocks.SMOOTH_SANDSTONE, Blocks.SMOOTH_STONE, Blocks.STONE_BUTTON, Blocks.STONE_PRESSURE_PLATE, Blocks.POLISHED_GRANITE_SLAB, Blocks.SMOOTH_RED_SANDSTONE_SLAB, Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.POLISHED_DIORITE_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.END_STONE_BRICK_SLAB, Blocks.SMOOTH_SANDSTONE_SLAB, Blocks.SMOOTH_QUARTZ_SLAB, Blocks.GRANITE_SLAB, Blocks.ANDESITE_SLAB, Blocks.RED_NETHER_BRICK_SLAB, Blocks.POLISHED_ANDESITE_SLAB, Blocks.DIORITE_SLAB, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.CLAY, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.PODZOL, Blocks.FARMLAND, Blocks.GRASS_BLOCK, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.RED_SAND, Blocks.SNOW_BLOCK, Blocks.SNOW, Blocks.SOUL_SAND, Blocks.GRASS_PATH, Blocks.WHITE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER, Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER, Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER, Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS, Blocks.BIRCH_PLANKS, Blocks.JUNGLE_PLANKS, Blocks.ACACIA_PLANKS, Blocks.DARK_OAK_PLANKS, Blocks.BOOKSHELF, Blocks.OAK_WOOD, Blocks.SPRUCE_WOOD, Blocks.BIRCH_WOOD, Blocks.JUNGLE_WOOD, Blocks.ACACIA_WOOD, Blocks.DARK_OAK_WOOD, Blocks.OAK_LOG, Blocks.SPRUCE_LOG, Blocks.BIRCH_LOG, Blocks.JUNGLE_LOG, Blocks.ACACIA_LOG, Blocks.DARK_OAK_LOG, Blocks.CHEST, Blocks.PUMPKIN, Blocks.CARVED_PUMPKIN, Blocks.JACK_O_LANTERN, Blocks.MELON, Blocks.LADDER, Blocks.SCAFFOLDING, Blocks.OAK_BUTTON, Blocks.SPRUCE_BUTTON, Blocks.BIRCH_BUTTON, Blocks.JUNGLE_BUTTON, Blocks.DARK_OAK_BUTTON, Blocks.ACACIA_BUTTON, Blocks.OAK_PRESSURE_PLATE, Blocks.SPRUCE_PRESSURE_PLATE, Blocks.BIRCH_PRESSURE_PLATE, Blocks.JUNGLE_PRESSURE_PLATE, Blocks.DARK_OAK_PRESSURE_PLATE, Blocks.ACACIA_PRESSURE_PLATE);

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

            float hardness = nextLocation.getBlockState().getBlockState().getBlockHardness(worldIn, nextLocation.getBlockPos());
            int harvestLevel = nextLocation.getBlock().getHarvestLevel(nextLocation.getBlockState().getBlockState());

            if (hardness >= 0 && hardness <= 50 && getTier().getHarvestLevel() >= harvestLevel) {
                nextLocation.breakBlock(player, heldStack);
                heldStack.damageItem(1, player, (p_220038_0_) -> {});
                damage++;
            }
        }
    }

    private void veinMine(ItemStack heldStack, PlayerEntity player, Location location) {

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
        return EFFECTIVE_ON.contains(blockState.getBlock()) || super.canHarvestBlock(blockState);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState blockState) {

        Material material = blockState.getMaterial();
        if( material == Material.WOOD && material == Material.PLANTS) {
            return this.efficiency;
        }

        return EFFECTIVE_ON.contains(blockState.getBlock()) ? this.efficiency : super.getDestroySpeed(stack, blockState);
    }
}
