package calemiutils.world;

import calemiutils.config.CUConfig;
import calemiutils.init.InitItems;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class WorldGen {

    public static void onCommonSetup () {

        ForgeRegistries.BIOMES.forEach(biome -> {

            if (biome.getCategory() != Biome.Category.THEEND && biome.getCategory() != Biome.Category.NETHER) {

                if (CUConfig.worldGen.raritaniumOreGen.get()) biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                        Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, InitItems.RARITANIUM_ORE.get().getDefaultState(),
                                CUConfig.worldGen.raritaniumVeinSize.get()))
                                .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(CUConfig.worldGen.raritaniumVeinsPerChunk.get(), CUConfig.worldGen.raritaniumOreGenMinY.get(), 0, CUConfig.worldGen.raritaniumOreGenMaxY.get()))));
            }
        });
    }
}
