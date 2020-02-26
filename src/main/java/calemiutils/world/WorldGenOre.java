package calemiutils.world;

import calemiutils.config.CUConfig;
import calemiutils.init.InitBlocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class WorldGenOre {

    public static void onCommonSetup() {

        ForgeRegistries.BIOMES.forEach(biome -> {

            if (biome.getCategory() != Biome.Category.THEEND && biome.getCategory() != Biome.Category.NETHER) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.func_225566_b_(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, InitBlocks.RARITANIUM_ORE.getDefaultState(), CUConfig.worldGen.raritaniumVeinSize.get())).func_227228_a_(Placement.COUNT_RANGE.func_227446_a_(new CountRangeConfig(CUConfig.worldGen.raritaniumVeinsPerChunk.get(), CUConfig.worldGen.raritaniumOreGenMinY.get(), 0, CUConfig.worldGen.raritaniumOreGenMaxY.get()))));
            }
        });
    }
}
