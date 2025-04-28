package pl.syntaxdevteam.tagsx.listeners

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import pl.syntaxdevteam.tagsx.TagsX

class PlayerJoinListener(private val plugin: TagsX) : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        setTag(player)
    }

    fun setTag(player: Player) {
        val tag = plugin.tagStorage.getTag(player.name)
        val displayName: Component = plugin.messageHandler.formatMixedTextToMiniMessage("$tag <reset>${player.name}", TagResolver.empty())

        player.displayName(displayName)
        player.playerListName(displayName)
    }
}
