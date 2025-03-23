package pl.syntaxdevteam.tagsx.commands

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pl.syntaxdevteam.tagsx.TagsX
import pl.syntaxdevteam.tagsx.gui.TagsGui

class TagsCommand(private val plugin: TagsX) : CommandExecutor {

    private val gui = TagsGui(plugin)

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            gui.open(sender)
            return true
        }

        plugin.sendMessageWithPrefix(sender, "<red>This command can only be used by players!</red>")
        return false
    }
}
