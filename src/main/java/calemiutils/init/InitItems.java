package calemiutils.init;

import calemiutils.CUReference;
import calemiutils.CalemiUtils;
import calemiutils.item.base.ItemBase;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = CUReference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(CUReference.MOD_ID)
public class InitItems {

    public static final Item RARITANIUM = null;

    public static final Item GOLD_CHIP = null;
    public static final Item MOTOR = null;

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {

        event.getRegistry().register(new ItemBase("raritanium", new Item.Properties().group(CalemiUtils.TAB)));
        event.getRegistry().register(new ItemBase("gold_chip", new Item.Properties().group(CalemiUtils.TAB)));
        event.getRegistry().register(new ItemBase("motor", new Item.Properties().group(CalemiUtils.TAB)));
    }
}
