package pl.syntaxdevteam.tagsx.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import pl.syntaxdevteam.tagsx.TagsX

class PlayerJoinListener(private val plugin: TagsX) : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        val tag = plugin.tagStorage.getTag(player.name) ?: plugin.config.getString("settings.default-tag", "none")

        val displayName = "$tag §r${player.name}"

        player.setDisplayName(displayName)
        player.setPlayerListName(displayName)
    }
}