package calemiutils.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CUConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final CategoryTooltips tooltips = new CategoryTooltips(BUILDER);
    public static final CategoryWorldGen worldGen = new CategoryWorldGen(BUILDER);
    public static final CategoryBlockScans blockScans = new CategoryBlockScans(BUILDER);
    public static final CategoryEconomy economy = new CategoryEconomy(BUILDER);
    public static final CategoryMisc misc = new CategoryMisc(BUILDER);

    public static final ForgeConfigSpec spec = BUILDER.build();

    private static final String NEEDED_FOR_SERVERS = "(Only needed on Servers)";

    public static class CategoryTooltips {

        public final ForgeConfigSpec.ConfigValue<Boolean> showInfoOnTooltips;
        public final ForgeConfigSpec.ConfigValue<Boolean> showControlsOnTooltips;

        public CategoryTooltips(ForgeConfigSpec.Builder builder) {

            builder.push("Tooltips");

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

            builder.push("WorldGen");

            raritaniumOreGen = builder.comment("Raritanium Ore Gen").define("raritaniumOreGen", true);
            raritaniumVeinsPerChunk = builder.comment("Raritanium Veins Per Chunk").define("raritaniumOreVeinsPerChunk", 4);
            raritaniumVeinSize = builder.comment("Raritanium Vein Size").define("raritaniumVeinSize", 8);
            raritaniumOreGenMinY = builder.comment("Raritanium Ore Min Y").define("raritaniumOreGenMinY", 0);
            raritaniumOreGenMaxY = builder.comment("Raritanium Ore Max Y").define("raritaniumOreGenMaxY", 30);

            builder.pop();
        }
    }

    public static class CategoryBlockScans {

        public final ForgeConfigSpec.ConfigValue<Integer> veinScanMaxSize;
        public final ForgeConfigSpec.ConfigValue<Integer> worldEditMaxSize;

        public CategoryBlockScans(ForgeConfigSpec.Builder builder) {

            builder.push("BlockScans");

            veinScanMaxSize = builder.comment("Vein Scan Max Size", "The Vein Scan is a system used by Blueprints, Scaffolds and Networks. It scans for blocks in a chain. The max size is how many chains will occur. Lower values run faster on servers.").define("veinScanMaxSize", 1500);
            worldEditMaxSize = builder.comment("Brush Max Size", "0 to Disable. The max size of blocks the Brush can place. Lower values run faster on servers.").define("worldEditMaxSize", 5000);

            builder.pop();
        }
    }

    public static class CategoryEconomy {

        public final ForgeConfigSpec.ConfigValue<String> currencyName;

        public CategoryEconomy(ForgeConfigSpec.Builder builder) {

            builder.push("Economy");

            currencyName = builder.comment("Currency Name").define("currencyName", "RC");

            builder.pop();
        }
    }

    public static class CategoryMisc {

        public final ForgeConfigSpec.ConfigValue<Boolean> useSecurity;

        public CategoryMisc(ForgeConfigSpec.Builder builder) {

            builder.push("Misc");

            useSecurity = builder.comment("Use Security", "Disable this to allow everyone access to anyone's blocks.").define("currencyName", true);

            builder.pop();
        }
    }
}