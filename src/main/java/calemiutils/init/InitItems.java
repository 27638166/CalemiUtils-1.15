package calemiutils.init;

import calemiutils.CUReference;
import calemiutils.CalemiUtils;
import calemiutils.item.ItemCurrency;
import calemiutils.item.ItemSecurityWrench;
import calemiutils.item.ItemSledgehammer;
import calemiutils.item.base.ItemBase;
import calemiutils.tool.SledgehammerTiers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
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

    public static final Item COIN_PENNY = null;
    public static final Item COIN_NICKEL = null;
    public static final Item COIN_QUARTER = null;
    public static final Item COIN_DOLLAR = null;

    public static final Item GOLD_CHIP = null;
    public static final Item MOTOR = null;

    public static final Item KNOB_WOOD = null;
    public static final Item KNOB_STONE = null;
    public static final Item KNOB_IRON = null;
    public static final Item KNOB_GOLD = null;
    public static final Item KNOB_DIAMON = null;
    public static final Item KNOB_STARLIGHT = null;

    public static final Item SLEDGEHAMMER_WOOD = null;
    public static final Item SLEDGEHAMMER_STONE = null;
    public static final Item SLEDGEHAMMER_IRON = null;
    public static final Item SLEDGEHAMMER_GOLD = null;
    public static final Item SLEDGEHAMMER_DIAMOND = null;
    public static final Item SLEDGEHAMMER_STARLIGHT = null;

    public static final Item SECURITY_WRENCH = null;

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {

        event.getRegistry().register(new ItemBase("raritanium", new Item.Properties().group(CalemiUtils.TAB)));

        event.getRegistry().register(new ItemCurrency("penny", new Item.Properties().group(CalemiUtils.TAB), 1));
        event.getRegistry().register(new ItemCurrency("nickel", new Item.Properties().group(CalemiUtils.TAB), 5));
        event.getRegistry().register(new ItemCurrency("quarter", new Item.Properties().group(CalemiUtils.TAB), 25));
        event.getRegistry().register(new ItemCurrency("dollar", new Item.Properties().group(CalemiUtils.TAB), 100));

        event.getRegistry().register(new ItemBase("gold_chip", new Item.Properties().group(CalemiUtils.TAB)));
        event.getRegistry().register(new ItemBase("motor", new Item.Properties().group(CalemiUtils.TAB)));

        event.getRegistry().register(new ItemBase("knob_wood", new Item.Properties().group(CalemiUtils.TAB)));
        event.getRegistry().register(new ItemBase("knob_stone", new Item.Properties().group(CalemiUtils.TAB)));
        event.getRegistry().register(new ItemBase("knob_iron", new Item.Properties().group(CalemiUtils.TAB)));
        event.getRegistry().register(new ItemBase("knob_gold", new Item.Properties().group(CalemiUtils.TAB)));
        event.getRegistry().register(new ItemBase("knob_diamond", new Item.Properties().group(CalemiUtils.TAB)));
        event.getRegistry().register(new ItemBase("knob_starlight", new Item.Properties().group(CalemiUtils.TAB)));

        event.getRegistry().register(new ItemSledgehammer("wood", new Item.Properties().group(CalemiUtils.TAB), SledgehammerTiers.WOOD, 1.4F));
        event.getRegistry().register(new ItemSledgehammer("stone", new Item.Properties().group(CalemiUtils.TAB), SledgehammerTiers.STONE, 1.4F));
        event.getRegistry().register(new ItemSledgehammer("iron", new Item.Properties().group(CalemiUtils.TAB), SledgehammerTiers.IRON, 1.4F));
        event.getRegistry().register(new ItemSledgehammer("gold", new Item.Properties().group(CalemiUtils.TAB), SledgehammerTiers.GOLD, 1.4F));
        event.getRegistry().register(new ItemSledgehammer("diamond", new Item.Properties().group(CalemiUtils.TAB), SledgehammerTiers.DIAMOND, 1.4F));
        event.getRegistry().register(new ItemSledgehammer("starlight", new Item.Properties().group(CalemiUtils.TAB), SledgehammerTiers.STARLIGHT, 1.4F));

        event.getRegistry().register(new ItemSecurityWrench(new Item.Properties().group(CalemiUtils.TAB)));
    }
}
