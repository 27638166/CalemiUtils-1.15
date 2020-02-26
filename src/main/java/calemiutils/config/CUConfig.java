package calemiutils.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CUConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final CategoryTooltips tooltips = new CategoryTooltips(BUILDER);
    public static final CategoryWorldGen worldGen = new CategoryWorldGen(BUILDER);

    public static final ForgeConfigSpec spec = BUILDER.build();

    private static final String NEEDED_FOR_SERVERS = "(Only needed on Servers)";

    public static class CategoryTooltips {

        public final ForgeConfigSpec.ConfigValue<Boolean> showInfoOnTooltips;
        public final ForgeConfigSpec.ConfigValue<Boolean> showControlsOnTooltips;

        public CategoryTooltips(ForgeConfigSpec.Builder builder) {

            builder.push("General");

            showInfoOnTooltips = builder.comment("Show Information On Tooltips").define("showInfoOnTooltips", true);
            showControlsOnTooltips = builder.comment("Show Controls On Tooltips").define("showControlsOnTooltips", true);

            builder.pop();
        }
    }

    public static class CategoryWorldGen {

        public final ForgeConfigSpec.ConfigValue<Boolean> raritaniumOreGen;
        public final ForgeConfigSpec.ConfigValue<Integer> raritaniumVeinsPerChunk;
        public final ForgeConfigSpec.ConfigValue<Integer> raritaniumVeinSize;
        public final ForgeConfigSpec.ConfigValue<Integer> raritaniumOreGenMinY;
        public final ForgeConfigSpec.ConfigValue<Integer> raritaniumOreGenMaxY;

        public CategoryWorldGen(ForgeConfigSpec.Builder builder) {

            builder.push("General");

            raritaniumOreGen = builder.comment("Raritanium Ore Gen").define("raritaniumOreGen", true);
            raritaniumVeinsPerChunk = builder.comment("Raritanium Veins Per Chunk").define("raritaniumOreVeinsPerChunk", 4);
            raritaniumVeinSize = builder.comment("Raritanium Vein Size").define("raritaniumVeinSize", 8);
            raritaniumOreGenMinY = builder.comment("Raritanium Ore Min Y").define("raritaniumOreGenMinY", 0);
            raritaniumOreGenMaxY = builder.comment("Raritanium Ore Max Y").define("raritaniumOreGenMaxY", 30);

            builder.pop();
        }
    }
}