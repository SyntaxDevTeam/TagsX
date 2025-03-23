package pl.syntaxdevteam.tagsx.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pl.syntaxdevteam.tagsx.TagsX

class ReloadCommand(private val plugin: TagsX) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player && !sender.hasPermission("tagsx.reload")) {
            plugin.sendMessageWithPrefix(sender, "§cYou do not have permission to use this command!")
            return true
        }

        plugin.reloadConfig()
        plugin.sendMessageWithPrefix(sender, "§aThe TagsX plugin has been successfully reloaded!")
        return true
    }
}
