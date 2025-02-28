package pl.syntaxdevteam.tagsx

import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin
import pl.syntaxdevteam.tagsx.commands.CommandManager
import pl.syntaxdevteam.tagsx.common.*
import pl.syntaxdevteam.tagsx.data.TagList
import pl.syntaxdevteam.tagsx.gui.TagsGui
import pl.syntaxdevteam.tagsx.listeners.PlayerJoinListener
import pl.syntaxdevteam.tagsx.placeholders.TagPlaceholder
import pl.syntaxdevteam.tagsx.storage.TagStorage
import java.io.File
import java.util.*

class TagsX : JavaPlugin() {
    private val config: FileConfiguration = getConfig()
    var logger: Logger = Logger(this, config.getBoolean("debug"))
    lateinit var pluginsManager: PluginManager
    private lateinit var statsCollector: StatsCollector
    lateinit var messageHandler: MessageHandler
    private lateinit var updateChecker: UpdateChecker
    private lateinit var commandManager: CommandManager
    val tagStorage = TagStorage(this)

    companion object {
        val PREFIX = MiniMessage.miniMessage().deserialize("<gradient:#ffcc66:#ff6600>[TagsX]</gradient> ")
    }

    override fun onEnable() {
        saveDefaultConfig()
        messageHandler = MessageHandler(this).apply { initial() }
        commandManager = CommandManager(this)
        pluginsManager = PluginManager(this)



        statsCollector = StatsCollector(this)
        updateChecker = UpdateChecker(this)
        updateChecker.checkForUpdates()





        tagStorage.loadTags()
        TagList.loadTags(this)
        commandManager.registerCommands()

        server.pluginManager.registerEvents(PlayerJoinListener(this), this)
        server.pluginManager.registerEvents(TagsGui(this), this)

        if (server.pluginManager.isPluginEnabled("PlaceholderAPI")) {
            TagPlaceholder(this).register()
        }

        logger.info("✅ TagsX został włączony pomyślnie!")
    }

    override fun onDisable() {
        tagStorage.saveTags()
        logger.info("🛑 TagsX został wyłączony.")
    }

    fun sendMessageWithPrefix(receiver: CommandSender, message: String) {
        val formattedMessage = LegacyComponentSerializer.legacySection().serialize(PREFIX) + message
        receiver.sendMessage(formattedMessage)
    }

    /**
     * Retrieves the plugin file.
     *
     * @return The plugin file.
     */
    fun getPluginFile(): File {
        return this.file
    }

    /**
     * Retrieves the server name from the server.properties file.
     *
     * @return The server name, or "Unknown Server" if not found.
     */
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
