package pl.syntaxdevteam.tagsx.listeners

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import pl.syntaxdevteam.tagsx.TagsX

class PlayerJoinListener(private val plugin: TagsX) : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        val tag = plugin.tagStorage.getTag(player.name)

        val displayName = "$tag §r${player.name}"
        val finalDisplayName = plugin.messageHandler.formatMixedTextToMiniMessage(displayName, TagResolver.empty())
        player.displayName(finalDisplayName)
        player.playerListName(finalDisplayName)
    }
}