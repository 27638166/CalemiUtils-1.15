package calemiutils;

import calemiutils.command.CUCommandBase;
import calemiutils.config.CUConfig;
import calemiutils.event.OverlayEvent;
import calemiutils.event.WrenchEvent;
import calemiutils.init.InitBlocks;
import calemiutils.init.InitEnchantments;
import calemiutils.init.InitItems;
import calemiutils.item.base.ItemPencilColored;
import calemiutils.packet.PacketPencilSetColor;
import calemiutils.world.WorldGenOre;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(CUReference.MOD_ID)
public class CalemiUtils {

    public static CalemiUtils instance;

    public static final ItemGroup TAB = new CUTab();

    public static SimpleChannel network;

    public CalemiUtils() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CUConfig.spec);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        instance = this;

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {

        network = NetworkRegistry.newSimpleChannel(new ResourceLocation(CUReference.MOD_ID, CUReference.MOD_ID), () -> "1.0", s -> true, s -> true);
        network.registerMessage(0, PacketPencilSetColor.class, PacketPencilSetColor::toBytes, PacketPencilSetColor::new, PacketPencilSetColor::handle);

        MinecraftForge.EVENT_BUS.register(new WrenchEvent());

        DeferredWorkQueue.runLater(WorldGenOre::onCommonSetup);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {

        MinecraftForge.EVENT_BUS.register(new OverlayEvent());
        RenderTypeLookup.setRenderLayer(InitBlocks.BLUEPRINT, RenderType.func_228643_e_());

        Minecraft.getInstance().getItemColors().register(new ItemPencilColored(), InitItems.PENCIL);
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {

        CUCommandBase.register(event.getCommandDispatcher());
    }
}