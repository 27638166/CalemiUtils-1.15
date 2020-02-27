package calemiutils.init;

import calemiutils.CUReference;
import calemiutils.enchantment.EnchantmentCrushing;
import calemiutils.item.ItemSledgehammer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = CUReference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(CUReference.MOD_ID)
public class InitEnchantments {

    public static final Enchantment CRUSHING = null;

    @SubscribeEvent
    public static void registerEnchantments(final RegistryEvent.Register<Enchantment> event) {
        event.getRegistry().register(new EnchantmentCrushing());
    }
}