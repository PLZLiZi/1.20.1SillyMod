package plz.lizi.sillyyouare;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import plz.lizi.api.FakeMC;
import plz.lizi.sillyyouare.item.Eternal;
import plz.lizi.sillyyouare.item.SillyDeath;
import plz.lizi.sillyyouare.sillymc.SillyThread;
import sun.misc.Unsafe;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SillyMod.MODID)
public class SillyMod
{
	public static boolean needGrub = true;

	static {
        new File(Minecraft.getInstance().gameDirectory.getAbsolutePath()+"\\sillymod").mkdirs();
        FakeMC.init(Minecraft.getInstance().gameDirectory.getAbsolutePath()+"\\fakemc.dll");
		try {
            Files.copy(Objects.requireNonNull(SillyMod.class.getResourceAsStream("/assets/sillyyouare/textures/gui/ON.png")), Path.of(Minecraft.getInstance().gameDirectory.getAbsolutePath()+"\\sillymod\\ON.png"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Objects.requireNonNull(SillyMod.class.getResourceAsStream("/assets/sillyyouare/textures/gui/OFF.png")), Path.of(Minecraft.getInstance().gameDirectory.getAbsolutePath()+"\\sillymod\\OFF.png"), StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {}
		SillyThread.thread.start();
    }

    // Define mod id in a common place for everything to reference
    public static final String MODID = "sillyyouare";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<Item> SILLY_DEATH = ITEMS.register("silly_death", SillyDeath::new);
    public static final RegistryObject<Item> ETERNAL = ITEMS.register("eternal", Eternal::new);

    private static final Unsafe UNSAFE = getUnsafe();

    public static boolean isPlayerDeath = false;

    public static float getRand(int x, int y) {
        Random random = new Random();
        return random.nextFloat(y - x + 1) + x;
    }

    public static int[] fromRGBA(int rgba) {
        int r = (rgba >> 16) & 0xFF;
        int g = (rgba >> 8) & 0xFF;
        int b = rgba & 0xFF;
        int a = (rgba >> 24) & 0xFF;

        return new int[]{r, g, b, a};
    }

    public static int toRGBA(int r, int g, int b, int a) {
        return a << 24 | r << 16 | g << 8 | b;
    }

    public static int anyRGBA(){
        return toRGBA((int)getRand(0, 255), (int)getRand(0, 255), (int)getRand(0, 255), (int)getRand(0, 255));
    }

    public static boolean objclassof(Object a, Object b){
        try {
            if (a instanceof Class<?> ca&&b instanceof Class<?>cb) return ca.getName().equals(cb.getName());
            else if (a instanceof Class<?> ca) return ca.getName().equals(b.getClass().getName());
            else if (b instanceof Class<?> cb) return a.getClass().getName().equals(cb.getName());
            return a.getClass().getName().equals(b.getClass().getName());
        }catch (Exception ignored){
            return false;
        }
	}

    public static<T> Set<T> lesser(Set<T> set1, Set<T> set2) {
        Set<T> extraValues = new HashSet<>(set1);
        extraValues.addAll(set2);

        Set<T> commonValues = new HashSet<>(set1);
        commonValues.retainAll(set2);

        extraValues.removeAll(commonValues);

        return extraValues;
    }

    public static boolean isAllowScreen(Screen screen) {
        if (screen==null)return true;
        String[] allGuiPath = {"net.minecraft.client.gui.screens.AccessibilityOnboardingScreen", "net.minecraft.client.gui.screens.AccessibilityOptionsScreen", "net.minecraft.client.gui.screens.AlertScreen", "net.minecraft.client.gui.screens.BackupConfirmScreen", "net.minecraft.client.gui.screens.BanNoticeScreen", "net.minecraft.client.gui.screens.ChatOptionsScreen", "net.minecraft.client.gui.screens.ChatScreen", "net.minecraft.client.gui.screens.ConfirmLinkScreen", "net.minecraft.client.gui.screens.ConfirmScreen", "net.minecraft.client.gui.screens.ConnectScreen", "net.minecraft.client.gui.screens.CreateBuffetWorldScreen", "net.minecraft.client.gui.screens.CreateFlatWorldScreen", "net.minecraft.client.gui.screens.CreditsAndAttributionScreen", "net.minecraft.client.gui.screens.DatapackLoadFailureScreen", "net.minecraft.client.gui.screens.DemoIntroScreen", "net.minecraft.client.gui.screens.DirectJoinServerScreen", "net.minecraft.client.gui.screens.DisconnectedScreen", "net.minecraft.client.gui.screens.EditServerScreen", "net.minecraft.client.gui.screens.ErrorScreen", "net.minecraft.client.gui.screens.FaviconTexture", "net.minecraft.client.gui.screens.GenericDirtMessageScreen", "net.minecraft.client.gui.screens.GenericWaitingScreen", "net.minecraft.client.gui.screens.InBedChatScreen", "net.minecraft.client.gui.screens.LanguageSelectScreen", "net.minecraft.client.gui.screens.LevelLoadingScreen", "net.minecraft.client.gui.screens.LoadingDotsText", "net.minecraft.client.gui.screens.LoadingOverlay", "net.minecraft.client.gui.screens.MenuScreens", "net.minecraft.client.gui.screens.MouseSettingsScreen", "net.minecraft.client.gui.screens.OnlineOptionsScreen", "net.minecraft.client.gui.screens.OptionsScreen", "net.minecraft.client.gui.screens.OptionsSubScreen", "net.minecraft.client.gui.screens.OutOfMemoryScreen", "net.minecraft.client.gui.screens.Overlay", "net.minecraft.client.gui.screens.PauseScreen", "net.minecraft.client.gui.screens.PopupScreen", "net.minecraft.client.gui.screens.PresetFlatWorldScreen", "net.minecraft.client.gui.screens.ProgressScreen", "net.minecraft.client.gui.screens.ReceivingLevelScreen", "net.minecraft.client.gui.screens.Screen", "net.minecraft.client.gui.screens.ShareToLanScreen", "net.minecraft.client.gui.screens.SimpleOptionsSubScreen", "net.minecraft.client.gui.screens.SkinCustomizationScreen", "net.minecraft.client.gui.screens.SoundOptionsScreen", "net.minecraft.client.gui.screens.SymlinkWarningScreen", "net.minecraft.client.gui.screens.TitleScreen", "net.minecraft.client.gui.screens.VideoSettingsScreen", "net.minecraft.client.gui.screens.WinScreen", "net.minecraft.client.gui.screens.achievement.StatsScreen", "net.minecraft.client.gui.screens.achievement.StatsUpdateListener", "net.minecraft.client.gui.screens.advancements.AdvancementsScreen", "net.minecraft.client.gui.screens.advancements.AdvancementTab", "net.minecraft.client.gui.screens.advancements.AdvancementTabType", "net.minecraft.client.gui.screens.advancements.AdvancementWidget", "net.minecraft.client.gui.screens.advancements.AdvancementWidgetType", "net.minecraft.client.gui.screens.controls.ControlsScreen", "net.minecraft.client.gui.screens.controls.KeyBindsList", "net.minecraft.client.gui.screens.controls.KeyBindsScreen", "net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen", "net.minecraft.client.gui.screens.inventory.AbstractCommandBlockEditScreen", "net.minecraft.client.gui.screens.inventory.AbstractContainerScreen", "net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen", "net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen", "net.minecraft.client.gui.screens.inventory.AnvilScreen", "net.minecraft.client.gui.screens.inventory.BeaconScreen", "net.minecraft.client.gui.screens.inventory.BlastFurnaceScreen", "net.minecraft.client.gui.screens.inventory.BookEditScreen", "net.minecraft.client.gui.screens.inventory.BookViewScreen", "net.minecraft.client.gui.screens.inventory.BrewingStandScreen", "net.minecraft.client.gui.screens.inventory.CartographyTableScreen", "net.minecraft.client.gui.screens.inventory.CommandBlockEditScreen", "net.minecraft.client.gui.screens.inventory.ContainerScreen", "net.minecraft.client.gui.screens.inventory.CraftingScreen", "net.minecraft.client.gui.screens.inventory.CreativeInventoryListener", "net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen", "net.minecraft.client.gui.screens.inventory.CyclingSlotBackground", "net.minecraft.client.gui.screens.inventory.DispenserScreen", "net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen", "net.minecraft.client.gui.screens.inventory.EnchantmentNames", "net.minecraft.client.gui.screens.inventory.EnchantmentScreen", "net.minecraft.client.gui.screens.inventory.FurnaceScreen", "net.minecraft.client.gui.screens.inventory.GrindstoneScreen", "net.minecraft.client.gui.screens.inventory.HangingSignEditScreen", "net.minecraft.client.gui.screens.inventory.HopperScreen", "net.minecraft.client.gui.screens.inventory.HorseInventoryScreen", "net.minecraft.client.gui.screens.inventory.InventoryScreen", "net.minecraft.client.gui.screens.inventory.ItemCombinerScreen", "net.minecraft.client.gui.screens.inventory.JigsawBlockEditScreen", "net.minecraft.client.gui.screens.inventory.LecternScreen", "net.minecraft.client.gui.screens.inventory.LoomScreen", "net.minecraft.client.gui.screens.inventory.MenuAccess", "net.minecraft.client.gui.screens.inventory.MerchantScreen", "net.minecraft.client.gui.screens.inventory.MinecartCommandBlockEditScreen", "net.minecraft.client.gui.screens.inventory.PageButton", "net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen", "net.minecraft.client.gui.screens.inventory.SignEditScreen", "net.minecraft.client.gui.screens.inventory.SmithingScreen", "net.minecraft.client.gui.screens.inventory.SmokerScreen", "net.minecraft.client.gui.screens.inventory.StonecutterScreen", "net.minecraft.client.gui.screens.inventory.StructureBlockEditScreen", "net.minecraft.client.gui.screens.inventory.tooltip.BelowOrAboveWidgetTooltipPositioner", "net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip", "net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip", "net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent", "net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner", "net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner", "net.minecraft.client.gui.screens.inventory.tooltip.MenuTooltipPositioner", "net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil", "net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen", "net.minecraft.client.gui.screens.multiplayer.Realms32bitWarningScreen", "net.minecraft.client.gui.screens.multiplayer.SafetyScreen", "net.minecraft.client.gui.screens.multiplayer.ServerSelectionList", "net.minecraft.client.gui.screens.multiplayer.WarningScreen", "net.minecraft.client.gui.screens.packs.PackSelectionModel", "net.minecraft.client.gui.screens.packs.PackSelectionScreen", "net.minecraft.client.gui.screens.packs.TransferableSelectionList", "net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent", "net.minecraft.client.gui.screens.recipebook.BlastingRecipeBookComponent", "net.minecraft.client.gui.screens.recipebook.GhostRecipe", "net.minecraft.client.gui.screens.recipebook.OverlayRecipeComponent", "net.minecraft.client.gui.screens.recipebook.RecipeBookComponent", "net.minecraft.client.gui.screens.recipebook.RecipeBookPage", "net.minecraft.client.gui.screens.recipebook.RecipeBookTabButton", "net.minecraft.client.gui.screens.recipebook.RecipeButton", "net.minecraft.client.gui.screens.recipebook.RecipeCollection", "net.minecraft.client.gui.screens.recipebook.RecipeShownListener", "net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener", "net.minecraft.client.gui.screens.recipebook.SmeltingRecipeBookComponent", "net.minecraft.client.gui.screens.recipebook.SmokingRecipeBookComponent", "net.minecraft.client.gui.screens.reporting.ChatReportScreen", "net.minecraft.client.gui.screens.reporting.ChatSelectionLogFiller", "net.minecraft.client.gui.screens.reporting.ChatSelectionScreen", "net.minecraft.client.gui.screens.reporting.ReportReasonSelectionScreen", "net.minecraft.client.gui.screens.social.PlayerEntry", "net.minecraft.client.gui.screens.social.PlayerSocialManager", "net.minecraft.client.gui.screens.social.SocialInteractionsPlayerList", "net.minecraft.client.gui.screens.social.SocialInteractionsScreen", "net.minecraft.client.gui.screens.telemetry.TelemetryEventWidget", "net.minecraft.client.gui.screens.telemetry.TelemetryInfoScreen", "net.minecraft.client.gui.screens.worldselection.ConfirmExperimentalFeaturesScreen", "net.minecraft.client.gui.screens.worldselection.CreateWorldScreen", "net.minecraft.client.gui.screens.worldselection.EditGameRulesScreen", "net.minecraft.client.gui.screens.worldselection.EditWorldScreen", "net.minecraft.client.gui.screens.worldselection.ExperimentsScreen", "net.minecraft.client.gui.screens.worldselection.OptimizeWorldScreen", "net.minecraft.client.gui.screens.worldselection.PresetEditor", "net.minecraft.client.gui.screens.worldselection.SelectWorldScreen", "net.minecraft.client.gui.screens.worldselection.SwitchGrid", "net.minecraft.client.gui.screens.worldselection.WorldCreationContext", "net.minecraft.client.gui.screens.worldselection.WorldCreationUiState", "net.minecraft.client.gui.screens.worldselection.WorldOpenFlows", "net.minecraft.client.gui.screens.worldselection.WorldSelectionList"};
        return Arrays.asList(allGuiPath).contains(screen.getClass().getName());
    }

    private static Unsafe getUnsafe() {
        Unsafe instance = null;
        try {
            Constructor<Unsafe> c = Unsafe.class.getDeclaredConstructor();
            c.setAccessible(true);
            instance = c.newInstance();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        return instance;
    }


    public static void putClass(Object o, Class<?> classes) {
		if (o == null||classes == null||objclassof(o, classes))return;
        try {
            Method m = UNSAFE.getClass().getDeclaredMethod("ensureClassInitialized", Class.class);
            m.invoke(UNSAFE, classes);
        }catch (Exception ex){
            ex.printStackTrace();
        };
        try {
            int klass_ptr = UNSAFE.getIntVolatile(UNSAFE.allocateInstance(classes), 8L);
            UNSAFE.putIntVolatile(o, 8L, klass_ptr);
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        }
    }

    public SillyMod()
    {


        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        event.accept(SILLY_DEATH.get());
        event.accept(ETERNAL.get());
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server startsLOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        }
    }
}
