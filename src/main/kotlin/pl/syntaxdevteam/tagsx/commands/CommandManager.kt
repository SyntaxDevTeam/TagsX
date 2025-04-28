package pl.syntaxdevteam.tagsx.commands

import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.plugin.Plugin
import pl.syntaxdevteam.tagsx.TagsX

@Suppress("UnstableApiUsage")
class CommandManager(private val plugin: TagsX) {

    fun registerCommands() {
        val manager: LifecycleEventManager<Plugin> = plugin.lifecycleManager
        manager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            val commands: Commands = event.registrar()
            commands.register(
                "tags",
                "Type /tags help to check available commands",
                TagsCommand(plugin)
            )
            commands.register(
                "tagsx",
                "TagsX plugin command. Type /tagsx help to check available commands",
                TagsXCommand(plugin)
            )
        }
    }
}