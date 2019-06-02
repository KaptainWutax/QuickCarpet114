package quickcarpet;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quickcarpet.commands.*;
import quickcarpet.helper.TickSpeed;
import quickcarpet.init.KeyBindings;
import quickcarpet.logging.LoggerRegistry;
import quickcarpet.module.ModuleHost;
import quickcarpet.module.QuickCarpetModule;
import quickcarpet.network.PluginChannelManager;
import quickcarpet.network.channels.StructureChannel;
import quickcarpet.pubsub.PubSubManager;
import quickcarpet.pubsub.PubSubMessenger;
import quickcarpet.settings.Settings;
import quickcarpet.utils.CarpetRegistry;
import quickcarpet.utils.HUDController;

import java.util.Set;
import java.util.TreeSet;

public final class QuickCarpet implements ModInitializer, ModuleHost {
    private static final Logger LOG = LogManager.getLogger();
    private static QuickCarpet instance = new QuickCarpet();

    public static final PubSubManager PUBSUB = new PubSubManager();
    public static MinecraftServer minecraft_server;

    public PluginChannelManager pluginChannels;
    public final Set<QuickCarpetModule> modules = new TreeSet<>();
    private final PubSubMessenger pubSubMessenger = new PubSubMessenger(PUBSUB);
    private CommandDispatcher<ServerCommandSource> dispatcher;

    // Fabric on dedicated server will call getInstance at return of DedicatedServer::<init>(...)
    // new CommandManager(...) is before that so QuickCarpet is created from that
    // Client will call getInstance at head of MinecraftClient::init()
    public QuickCarpet() {
        instance = this;
    }

    public static QuickCarpet getInstance() {
        return instance;
    }

    public void init(MinecraftServer server) {
        minecraft_server = server;
        pluginChannels = new PluginChannelManager(server);
        pluginChannels.register(pubSubMessenger);
        pluginChannels.register(new StructureChannel());
        for (QuickCarpetModule m : modules) m.onServerInit(server);
    }

    public void onServerLoaded(MinecraftServer server) {
        Settings.MANAGER.init(server);
        TickSpeed.resetLoadAvg = true;
        for (QuickCarpetModule m : modules) m.onServerLoaded(server);
        registerCarpetCommands();
    }

    public void tick(MinecraftServer server) {
        TickSpeed.tick(server);
        HUDController.update_hud(server);
        StructureChannel.instance.tick();
        for (QuickCarpetModule m : modules) m.tick(server);
    }
    
    public void onGameStarted() {
        LoggerRegistry.initLoggers();
        CarpetRegistry.init();
        Settings.MANAGER.parse();
        for (QuickCarpetModule m : modules) {
            m.onGameStarted();
            LOG.info(Build.NAME + " module " + m.getId() + " version " + m.getVersion() + " initialized");
        }

        KeyBindings.registerKeyBindings();
    }

    public void registerCarpetCommands() {
        CarpetCommand.register(dispatcher);
        TickCommand.register(dispatcher);
        CarpetFillCommand.register(dispatcher);
        CarpetCloneCommand.register(dispatcher);
        CarpetSetBlockCommand.register(dispatcher);
        CounterCommand.register(dispatcher);
        PlayerCommand.register(dispatcher);
        LogCommand.register(dispatcher);
        SpawnCommand.register(dispatcher);
        PingCommand.register(dispatcher);
        CameraModeCommand.register(dispatcher);
        for (QuickCarpetModule m : modules) m.registerCommands(dispatcher);
    }

    public void setCommandDispatcher(CommandDispatcher<ServerCommandSource> dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void registerModule(QuickCarpetModule module) {
        LOG.info(Build.NAME + " module " + module.getId() + " version " + module.getVersion() + " registered");
        modules.add(module);
    }

    @Override
    public void onInitialize() {

    }
}
