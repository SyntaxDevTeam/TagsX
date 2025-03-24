package pl.syntaxdevteam.tagsx.commands

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.jetbrains.annotations.NotNull
import pl.syntaxdevteam.tagsx.TagsX

@Suppress("UnstableApiUsage")
class TagsXCommand(private val plugin: TagsX) : BasicCommand {

    override fun execute(@NotNull stack: CommandSourceStack, @NotNull args: Array<String>) {
        if (args.isEmpty() || args[0].lowercase() != "reload") {
            stack.sender.sendMessage("§cUżycie: /tagsx reload")
            return
        }

        if (!stack.sender.hasPermission("tagsx.reload")) {
            stack.sender.sendMessage("§cNie masz uprawnień do tej komendy!")
            return
        }

        plugin.reloadConfig()
        stack.sender.sendMessage("§aPlugin TagsX został poprawnie przeładowany!")
        return
    }

    override fun suggest(@NotNull stack: CommandSourceStack, @NotNull args: Array<String>): List<String> {
        return if (args.size == 1) {
            listOf("reload").filter { it.startsWith(args[0], ignoreCase = true) }
        } else {
            emptyList()
        }
    }
}