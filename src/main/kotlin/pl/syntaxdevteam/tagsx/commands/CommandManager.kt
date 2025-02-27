package pl.syntaxdevteam.tagsx.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player
import pl.syntaxdevteam.tagsx.TagsX
import pl.syntaxdevteam.tagsx.gui.TagsGui

class CommandManager(private val plugin: TagsX) {

    private val gui = TagsGui(plugin)

    fun registerCommands() {
        val tagsCommand = object : BukkitCommand("tags") {
            override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
                if (sender !is Player) {
                    sender.sendMessage("§cTa komenda jest tylko dla graczy!")
                    return false
                }
                gui.open(sender)
                return true
            }
        }

        val tagsxCommand = object : BukkitCommand("tagsx"), TabCompleter {
            override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
                if (args.isEmpty() || args[0].lowercase() != "reload") {
                    sender.sendMessage("§cUżycie: /tagsx reload")
                    return true
                }

                if (!sender.hasPermission("tagsx.reload")) {
                    if (sender is Player) {
                        plugin.sendMessageWithPrefix(sender, "§cNie masz uprawnień do tej komendy!")
                    } else {
                        sender.sendMessage("§cNie masz uprawnień do tej komendy!")
                    }
                    return true
                }

                plugin.reloadConfig()
                plugin.sendMessageWithPrefix(sender, "§aPlugin TagsX został poprawnie przeładowany!")
                return true
            }

            override fun onTabComplete(
                sender: CommandSender,
                command: Command,
                alias: String,
                args: Array<out String>
            ): MutableList<String> {
                return if (args.size == 1) {
                    mutableListOf("reload").filter { it.startsWith(args[0], ignoreCase = true) }.toMutableList()
                } else mutableListOf()
            }
        }

        plugin.server.commandMap.register("tags", tagsCommand)
        plugin.server.commandMap.register("tagsx", tagsxCommand)
    }
}
