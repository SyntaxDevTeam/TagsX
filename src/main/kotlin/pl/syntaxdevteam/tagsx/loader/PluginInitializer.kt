package pl.syntaxdevteam.tagsx.loader

import pl.syntaxdevteam.core.SyntaxCore
import pl.syntaxdevteam.message.SyntaxMessages
import pl.syntaxdevteam.tagsx.TagsX
import pl.syntaxdevteam.tagsx.commands.CommandManager
import pl.syntaxdevteam.tagsx.common.CommandLoggerPlugin
import pl.syntaxdevteam.tagsx.common.ConfigHandler
import pl.syntaxdevteam.tagsx.data.TagList
import pl.syntaxdevteam.tagsx.gui.TagsGui
import pl.syntaxdevteam.tagsx.hooks.HookHandler
import pl.syntaxdevteam.tagsx.listeners.PlayerJoinListener
import pl.syntaxdevteam.tagsx.placeholders.TagPlaceholder

class PluginInitializer(private val plugin: TagsX) {

    fun onEnable() {
        setUpLogger()
        setupConfig()
        setupHandlers()
        registerEvents()
        registerCommands()
        checkForUpdates()
    }

    fun onDisable() {
        plugin.logger.err(plugin.pluginMeta.name + " " + plugin.pluginMeta.version + " has been disabled ☹️")
    }

    private fun setUpLogger() {
        plugin.pluginConfig = plugin.config
        plugin.logger = SyntaxCore.logger
    }

    /**
     * Sets up the plugin configuration.
     */
    private fun setupConfig() {
        plugin.saveDefaultConfig()
        plugin.configHandler = ConfigHandler(plugin)
        plugin.configHandler.verifyAndUpdateConfig()
    }

    private fun setupHandlers() {
        SyntaxMessages.initialize(plugin)
        plugin.messageHandler = SyntaxMessages.messages
        plugin.pluginsManager = SyntaxCore.pluginManagerx
        plugin.hookHandler = HookHandler(plugin)
        plugin.versionChecker = VersionChecker(plugin)
        //plugin.versionCompatibility = VersionCompatibility(plugin.versionChecker)
    }

    /**
     * Registers the plugin commands.
     */
    private fun registerCommands(){
        plugin.commandLoggerPlugin = CommandLoggerPlugin(plugin)
        plugin.commandManager = CommandManager(plugin)
        plugin.commandManager.registerCommands()
    }

    private fun registerEvents() {
        val versionChecker = runCatching { plugin.versionChecker }
            .getOrElse {
                VersionChecker(plugin).also { plugin.versionChecker = it }
            }
        if (plugin.hookHandler.checkPlaceholderAPI()) {
            TagPlaceholder(plugin).register()
        }
        plugin.tagStorage.loadTags()
        TagList.loadTags(plugin)

        plugin.server.pluginManager.registerEvents(PlayerJoinListener(plugin), plugin)
        plugin.server.pluginManager.registerEvents(TagsGui(plugin), plugin)
    }

    /**
     * Checks for updates to the plugin.
     */
    private fun checkForUpdates() {
        plugin.statsCollector = SyntaxCore.statsCollector
        SyntaxCore.updateChecker.checkAsync()
    }
}