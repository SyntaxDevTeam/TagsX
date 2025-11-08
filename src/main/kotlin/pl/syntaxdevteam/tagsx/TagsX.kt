package pl.syntaxdevteam.tagsx

import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import pl.syntaxdevteam.core.SyntaxCore
import pl.syntaxdevteam.core.SyntaxCore.updateChecker
import pl.syntaxdevteam.core.manager.PluginManagerX
import pl.syntaxdevteam.message.SyntaxMessages
import pl.syntaxdevteam.core.logging.Logger
import pl.syntaxdevteam.core.stats.StatsCollector
import pl.syntaxdevteam.core.update.GitHubSource
import pl.syntaxdevteam.core.update.ModrinthSource
import pl.syntaxdevteam.core.update.UpdateChecker
import pl.syntaxdevteam.message.MessageHandler
import pl.syntaxdevteam.tagsx.commands.CommandManager
import pl.syntaxdevteam.tagsx.common.*
import pl.syntaxdevteam.tagsx.data.TagList
import pl.syntaxdevteam.tagsx.gui.TagsGui
import pl.syntaxdevteam.tagsx.hooks.HookHandler
import pl.syntaxdevteam.tagsx.listeners.PlayerJoinListener
import pl.syntaxdevteam.tagsx.loader.PluginInitializer
import pl.syntaxdevteam.tagsx.loader.VersionChecker
import pl.syntaxdevteam.tagsx.placeholders.TagPlaceholder
import pl.syntaxdevteam.tagsx.storage.TagStorage
import java.io.File
import java.util.*

@Suppress("UnstableApiUsage")
class TagsX : JavaPlugin() {
    private lateinit var pluginInitializer: PluginInitializer
    lateinit var logger: Logger
    lateinit var messageHandler: MessageHandler
    lateinit var pluginsManager: PluginManagerX

    lateinit var configHandler: ConfigHandler
    lateinit var pluginConfig: FileConfiguration
    lateinit var statsCollector: StatsCollector

    lateinit var hookHandler: HookHandler
    lateinit var commandLoggerPlugin: CommandLoggerPlugin
    lateinit var commandManager: CommandManager
    val tagStorage = TagStorage(this)

    lateinit var versionChecker: VersionChecker
    //lateinit var versionCompatibility: VersionCompatibility

    override fun onEnable() {
        SyntaxCore.registerUpdateSources(
            GitHubSource("SyntaxDevTeam/TagsX"),
            ModrinthSource("")
        )
        SyntaxCore.init(this)
        pluginInitializer = PluginInitializer(this)
        pluginInitializer.onEnable()
        versionChecker.checkAndLog()
    }

    fun onReload() {
       // reloadMyConfig()
    }

    /**
     * Called when the plugin is disabled.
     * Closes the database connection and unregisters events.
     */
    override fun onDisable() {
        AsyncChatEvent.getHandlerList().unregister(this as Plugin)
        pluginInitializer.onDisable()
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
