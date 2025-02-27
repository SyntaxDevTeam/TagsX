package pl.syntaxdevteam.tagsx.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pl.syntaxdevteam.tagsx.TagsX

class ReloadCommand(private val plugin: TagsX) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            plugin.sendMessageWithPrefix(sender, "§cNie masz dostępu do tej komendy!")
        } else {
            sender.sendMessage("§cTę komendę może wykonać tylko gracz!")
        }


        plugin.reloadConfig()
        plugin.sendMessageWithPrefix(sender, "§aPlugin TagsX został poprawnie przeładowany!")
        return true
    }
}
