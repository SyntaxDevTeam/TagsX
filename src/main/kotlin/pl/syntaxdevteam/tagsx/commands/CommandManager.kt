package pl.syntaxdevteam.tagsx.commands

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pl.syntaxdevteam.tagsx.TagsX
import pl.syntaxdevteam.tagsx.gui.TagsGui

class CommandManager(private val plugin: TagsX) {

    private val mm = MiniMessage.miniMessage()
    private val gui = TagsGui(plugin)

    fun registerCommands(plugin: TagsX) {
        plugin.server.commandMap.register("tagsx", object : org.bukkit.command.Command("tags") {
            override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
                if (sender is Player) {
                    gui.open(sender) // ✅ Otwiera GUI
                    return true
                }

                sender.sendMessage(mm.deserialize("<red>Ta komenda jest tylko dla graczy!</red>"))
                return false
            }
        })
    }
}
