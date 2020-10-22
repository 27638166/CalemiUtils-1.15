package calemiutils.world;

import calemiutils.config.CUConfig;
import calemiutils.init.InitItems;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.ChanceRangeConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class WorldGen {

    private static final BlockState baseBlock = Blocks.STONE.getDefaultState();

    private static final BlockState coinStackPennyState = InitItems.COIN_STACK_PENNY.get().getDefaultState();
    private static final BlockState coinStackNickelState = InitItems.COIN_STACK_NICKEL.get().getDefaultState();
    private static final BlockState coinStackQuarterState = InitItems.COIN_STACK_QUARTER.get().getDefaultState();
    private static final BlockState coinStackDollarState = InitItems.COIN_STACK_DOLLAR.get().getDefaultState();

    public static final BlockClusterFeatureConfig coinStackPennyConfig = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(coinStackPennyState), new SimpleBlockPlacer())).func_227315_a_(64).func_227316_a_(ImmutableSet.of(baseBlock.getBlock())).func_227317_b_().func_227322_d_();
    public static final BlockClusterFeatureConfig coinStackNickelConfig = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(coinStackNickelState), new SimpleBlockPlacer())).func_227315_a_(64).func_227316_a_(ImmutableSet.of(baseBlock.getBlock())).func_227317_b_().func_227322_d_();
    public static final BlockClusterFeatureConfig coinStackQuarterConfig = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(coinStackQuarterState), new SimpleBlockPlacer())).func_227315_a_(64).func_227316_a_(ImmutableSet.of(baseBlock.getBlock())).func_227317_b_().func_227322_d_();
    public static final BlockClusterFeatureConfig coinStackDollarConfig = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(coinStackDollarState), new SimpleBlockPlacer())).func_227315_a_(64).func_227316_a_(ImmutableSet.of(baseBlock.getBlock())).func_227317_b_().func_227322_d_();

    public static void onCommonSetup () {

        ForgeRegistries.BIOMES.forEach(biome -> {

            if (biome.getCategory() != Biome.Category.THEEND && biome.getCategory() != Biome.Category.NETHER) {

                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                        Feature.ORE.func_225566_b_(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, InitItems.RARITANIUM_ORE.get().getDefaultState(),
                                CUConfig.worldGen.raritaniumVeinSize.get()))
                                .func_227228_a_(Placement.COUNT_RANGE.func_227446_a_(
                                        new CountRangeConfig(CUConfig.worldGen.raritaniumVeinsPerChunk.get(), CUConfig.worldGen.raritaniumOreGenMinY.get(), 0, CUConfig.worldGen.raritaniumOreGenMaxY.get()))));

                if (CUConfig.worldGen.coinStackPennyGen.get()) biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.func_225566_b_(coinStackPennyConfig).func_227228_a_(Placement.CHANCE_RANGE.func_227446_a_(new ChanceRangeConfig((float)(double)CUConfig.worldGen.coinStackPennyRarity.get(), 0, 0, 50))));
                if (CUConfig.worldGen.coinStackNickelGen.get()) biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.func_225566_b_(coinStackNickelConfig).func_227228_a_(Placement.CHANCE_RANGE.func_227446_a_(new ChanceRangeConfig((float)(double)CUConfig.worldGen.coinStackNickelRarity.get(), 0, 0, 50))));
                if (CUConfig.worldGen.coinStackQuarterGen.get()) biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.func_225566_b_(coinStackQuarterConfig).func_227228_a_(Placement.CHANCE_RANGE.func_227446_a_(new ChanceRangeConfig((float)(double)CUConfig.worldGen.coinStackQuarterRarity.get(), 0, 0, 50))));
                if (CUConfig.worldGen.coinStackDollarGen.get()) biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.field_227248_z_.func_225566_b_(coinStackDollarConfig).func_227228_a_(Placement.CHANCE_RANGE.func_227446_a_(new ChanceRangeConfig((float)(double)CUConfig.worldGen.coinStackDollarRarity.get(), 0, 0, 50))));
            }
        });
    }
}
