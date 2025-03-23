package pl.syntaxdevteam.tagsx

import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import pl.syntaxdevteam.tagsx.commands.CommandManager
import pl.syntaxdevteam.tagsx.data.TagList
import pl.syntaxdevteam.tagsx.gui.TagsGui
import pl.syntaxdevteam.tagsx.listeners.PlayerJoinListener
import pl.syntaxdevteam.tagsx.placeholders.TagPlaceholder
import pl.syntaxdevteam.tagsx.storage.TagStorage

class TagsX : JavaPlugin() {

    val tagStorage = TagStorage(this)
    private val commandManager = CommandManager(this)

    val mm = MiniMessage.miniMessage()

    companion object {
        val PREFIX = MiniMessage.miniMessage().deserialize("<gradient:#ffcc66:#ff6600>[TagsX]</gradient> ")
    }

    override fun onEnable() {
        saveDefaultConfig()
        tagStorage.loadTags()
        TagList.loadTags(this)
        commandManager.registerCommands()

        server.pluginManager.registerEvents(PlayerJoinListener(this), this)
        server.pluginManager.registerEvents(TagsGui(this), this)

        if (server.pluginManager.isPluginEnabled("PlaceholderAPI")) {
            TagPlaceholder(this).register()
        }

        logger.info("TagsX on")
    }

    override fun onDisable() {
        tagStorage.saveTags()
        logger.info("TagsX off")
    }

    fun sendMessageWithPrefix(receiver: CommandSender, message: String) {
        val formattedMessage = LegacyComponentSerializer.legacySection().serialize(PREFIX) + message
        receiver.sendMessage(formattedMessage)
    }
}
