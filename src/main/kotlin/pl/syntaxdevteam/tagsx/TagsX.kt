package pl.syntaxdevteam.tagsx

import org.bukkit.plugin.java.JavaPlugin
import pl.syntaxdevteam.tagsx.commands.CommandManager
import pl.syntaxdevteam.tagsx.gui.TagsGui
import pl.syntaxdevteam.tagsx.listeners.PlayerJoinListener
import pl.syntaxdevteam.tagsx.placeholders.TagPlaceholder
import pl.syntaxdevteam.tagsx.storage.TagStorage

class TagsX : JavaPlugin() {
    val tagStorage = TagStorage(this)
    private val commandManager = CommandManager(this)

    override fun onEnable() {
        saveDefaultConfig()

        tagStorage.loadTags()

        commandManager.registerCommands(this)

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
}