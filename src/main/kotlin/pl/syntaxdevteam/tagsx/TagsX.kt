package pl.syntaxdevteam.tagsx

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin
import pl.syntaxdevteam.tagsx.commands.CommandManager
import pl.syntaxdevteam.tagsx.common.*
import pl.syntaxdevteam.tagsx.data.TagList
import pl.syntaxdevteam.tagsx.gui.TagsGui
import pl.syntaxdevteam.tagsx.hooks.HookHandler
import pl.syntaxdevteam.tagsx.listeners.PlayerJoinListener
import pl.syntaxdevteam.tagsx.placeholders.TagPlaceholder
import pl.syntaxdevteam.tagsx.storage.TagStorage
import java.io.File
import java.util.*

@Suppress("UnstableApiUsage")
class TagsX : JavaPlugin() {
    private val config: FileConfiguration = getConfig()
    var logger: Logger = Logger(this, config.getBoolean("debug"))
    lateinit var pluginsManager: PluginManager
    private lateinit var statsCollector: StatsCollector
    lateinit var messageHandler: MessageHandler
    private var configHandler = ConfigHandler(this)
    private lateinit var updateChecker: UpdateChecker
    private lateinit var hookHandler: HookHandler
    //private lateinit var commandManager: CommandManager
    val tagStorage = TagStorage(this)
    private val commandManager = CommandManager(this)


    override fun onEnable() {
        saveDefaultConfig()
        configHandler.verifyAndUpdateConfig()
        messageHandler = MessageHandler(this).apply { initial() }
        hookHandler = HookHandler(this)
        tagStorage.loadTags()
        TagList.loadTags(this)

        commandManager.registerCommands()
        server.pluginManager.registerEvents(PlayerJoinListener(this), this)
        server.pluginManager.registerEvents(TagsGui(this), this)

        if (hookHandler.checkPlaceholderAPI()) {
            TagPlaceholder(this).register()
        }
        pluginsManager = PluginManager(this)
        statsCollector = StatsCollector(this)
        updateChecker = UpdateChecker(this)
        updateChecker.checkForUpdates()
    }

    override fun onDisable() {
        tagStorage.saveTags()
        logger.err("🛑 " + pluginMeta.name + " " + pluginMeta.version + " has been disabled ☹️")
    }

    fun getPluginFile(): File {
        return this.file
    }

    fun getServerName(): String {
        val properties = Properties()
        val file = File("server.properties")
        if (file.exists()) {
            properties.load(file.inputStream())
            val serverName = properties.getProperty("server-name")
            if (serverName != null) {
                return serverName
            } else {
                logger.debug("Property 'server-name' not found in server.properties file.")
            }
        } else {
            logger.debug("The server.properties file does not exist.")
        }
        return "Unknown Server"
    }
}
