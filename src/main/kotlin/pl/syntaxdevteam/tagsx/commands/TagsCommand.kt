package pl.syntaxdevteam.tagsx.commands

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.entity.Player
import org.jetbrains.annotations.NotNull
import pl.syntaxdevteam.tagsx.TagsX
import pl.syntaxdevteam.tagsx.gui.TagsGui

@Suppress("UnstableApiUsage")
class TagsCommand(private val plugin: TagsX) : BasicCommand {

    private val gui = TagsGui(plugin)

    override fun execute(@NotNull stack: CommandSourceStack, @NotNull args: Array<String>) {
        if (stack.sender !is Player) {
            stack.sender.sendMessage(plugin.messageHandler.getMessage("error", "console"))
            return
        }
        if (!(stack.sender.hasPermission("tagsx.cmd.tags"))) {
            stack.sender.sendMessage(plugin.messageHandler.getMessage("error", "no_permission"))
            return
        }
            gui.open(stack.sender.name)
    }
}
