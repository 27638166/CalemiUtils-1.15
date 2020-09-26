package calemiutils;

import calemiutils.command.CUCommandBase;
import calemiutils.event.*;
import calemiutils.gui.*;
import calemiutils.init.*;
import calemiutils.item.base.ItemPencilColored;
import calemiutils.packet.*;
import calemiutils.render.RenderBookStand;
import calemiutils.render.RenderItemStand;
import calemiutils.render.RenderTradingPost;
import calemiutils.util.helper.LogHelper;
import calemiutils.world.WorldGenOre;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.awt.*;

@Mod(CUReference.MOD_ID)
public class CalemiUtils {

    public static final ItemGroup TAB = new CUTab();
    public static CalemiUtils instance;
    public static SimpleChannel network;
    public static IEventBus MOD_EVENT_BUS;

    public CalemiUtils () {

        MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
        MOD_EVENT_BUS.addListener(this::setup);
        MOD_EVENT_BUS.addListener(this::doClientStuff);

        InitTileEntityTypes.TILE_ENTITY_TYPES.register(MOD_EVENT_BUS);
        InitContainersTypes.CONTAINER_TYPES.register(MOD_EVENT_BUS);
        InitEnchantments.ENCHANTMENTS.register(MOD_EVENT_BUS);

        InitItems.init();

        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CUConfig.spec);
    }

    private void setup (final FMLCommonSetupEvent event) {

        int id = 0;
        network = NetworkRegistry.newSimpleChannel(new ResourceLocation(CUReference.MOD_ID, CUReference.MOD_ID), () -> "1.0", s -> true, s -> true);
        network.registerMessage(++id, PacketEnableTileEntity.class, PacketEnableTileEntity::toBytes, PacketEnableTileEntity::new, PacketEnableTileEntity::handle);
        network.registerMessage(++id, PacketPencilSetColor.class, PacketPencilSetColor::toBytes, PacketPencilSetColor::new, PacketPencilSetColor::handle);
        network.registerMessage(++id, PacketLinkBook.class, PacketLinkBook::toBytes, PacketLinkBook::new, PacketLinkBook::handle);
        network.registerMessage(++id, PacketItemStand.class, PacketItemStand::toBytes, PacketItemStand::new, PacketItemStand::handle);
        network.registerMessage(++id, PacketWallet.class, PacketWallet::toBytes, PacketWallet::new, PacketWallet::handle);
        network.registerMessage(++id, PacketOpenWallet.class, PacketOpenWallet::toBytes, PacketOpenWallet::new, PacketOpenWallet::handle);
        network.registerMessage(++id, PacketBank.class, PacketBank::toBytes, PacketBank::new, PacketBank::handle);
        network.registerMessage(++id, PacketTradingPost.class, PacketTradingPost::toBytes, PacketTradingPost::new, PacketTradingPost::handle);

        MinecraftForge.EVENT_BUS.register(new WrenchEvent());
        MinecraftForge.EVENT_BUS.register(new SecurityEvent());
        MinecraftForge.EVENT_BUS.register(new MobBeaconEvent());

        DeferredWorkQueue.runLater(WorldGenOre::onCommonSetup);
    }

    private void doClientStuff (final FMLClientSetupEvent event) {

        MinecraftForge.EVENT_BUS.register(new WrenchLoreEvent());
        MinecraftForge.EVENT_BUS.register(new ScreenOverlayEvent());
        MinecraftForge.EVENT_BUS.register(new KeyEvent());

        RenderTypeLookup.setRenderLayer(InitItems.BLUEPRINT.get(), RenderType.func_228643_e_());
        RenderTypeLookup.setRenderLayer(InitItems.IRON_SCAFFOLD.get(), RenderType.func_228643_e_());
        RenderTypeLookup.setRenderLayer(InitItems.BOOK_STAND.get(), RenderType.func_228643_e_());
        RenderTypeLookup.setRenderLayer(InitItems.ITEM_STAND.get(), RenderType.func_228643_e_());
        RenderTypeLookup.setRenderLayer(InitItems.TRADING_POST.get(), RenderType.func_228643_e_());

        ScreenManager.registerFactory(InitContainersTypes.WALLET.get(), ScreenWallet::new);
        ScreenManager.registerFactory(InitContainersTypes.TORCH_PLACER.get(), ScreenTorchPlacer::new);
        ScreenManager.registerFactory(InitContainersTypes.BOOK_STAND.get(), ScreenOneSlot::new);
        ScreenManager.registerFactory(InitContainersTypes.ITEM_STAND.get(), ScreenOneSlot::new);
        ScreenManager.registerFactory(InitContainersTypes.BANK.get(), ScreenBank::new);
        ScreenManager.registerFactory(InitContainersTypes.TRADING_POST.get(), ScreenTradingPost::new);

        ClientRegistry.bindTileEntityRenderer(InitTileEntityTypes.BOOK_STAND.get(), RenderBookStand::new);
        ClientRegistry.bindTileEntityRenderer(InitTileEntityTypes.ITEM_STAND.get(), RenderItemStand::new);
        ClientRegistry.bindTileEntityRenderer(InitTileEntityTypes.TRADING_POST.get(), RenderTradingPost::new);

        InitKeyBindings.init();
    }

    @SubscribeEvent
    public void onServerStarting (FMLServerStartingEvent event) {

        CUCommandBase.register(event.getCommandDispatcher());
    }
}