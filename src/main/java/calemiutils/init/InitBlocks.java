package calemiutils.init;

import calemiutils.CUReference;
import calemiutils.CalemiUtils;
import calemiutils.block.BlockBlueprint;
import calemiutils.block.BlockRaritaniumOre;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(CUReference.MOD_ID)
@Mod.EventBusSubscriber(modid = CUReference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class InitBlocks {

    public static final Block RARITANIUM_ORE = null;

    public static final Block BLUEPRINT = null;

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {

        event.getRegistry().register(new BlockRaritaniumOre());
        event.getRegistry().register(new BlockBlueprint());
    }

    @SubscribeEvent
    public static void registerBlocksItems(final RegistryEvent.Register<Item> event) {

        event.getRegistry().register(new BlockItem(RARITANIUM_ORE, new Item.Properties().group(CalemiUtils.TAB)).setRegistryName("raritanium_ore"));
        event.getRegistry().register(new BlockItem(BLUEPRINT, new Item.Properties().group(CalemiUtils.TAB)).setRegistryName("blueprint"));
    }
}
