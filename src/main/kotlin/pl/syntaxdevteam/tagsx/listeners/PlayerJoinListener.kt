package pl.syntaxdevteam.tagsx.listeners

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import pl.syntaxdevteam.tagsx.TagsX

class PlayerJoinListener(private val plugin: TagsX) : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        val tag = plugin.tagStorage.getTag(player.name) ?: "Gracz"


        val displayComponent = Component.text("[$tag] ${player.name}")


        val legacyDisplayName = LegacyComponentSerializer.legacySection().serialize(displayComponent)

        player.customName = legacyDisplayName
        var playerListName = legacyDisplayName
        player.setDisplayName(legacyDisplayName)
    }
}
