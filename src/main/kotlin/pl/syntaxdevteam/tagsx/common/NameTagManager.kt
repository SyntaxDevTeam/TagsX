package pl.syntaxdevteam.tagsx.common

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team
import pl.syntaxdevteam.tagsx.TagsX

class NameTagManager(private val plugin: TagsX) {

    fun updatePlayerNameTag(player: Player, tag: String) {
        val sanitizedTag = tag.trim()

        Bukkit.getOnlinePlayers().forEach { viewer ->
            updateNameTagOnScoreboard(viewer.scoreboard, player, sanitizedTag)
        }
    }

    private fun updateNameTagOnScoreboard(scoreboard: Scoreboard, player: Player, tag: String) {
        val teamName = teamName(player)

        removePlayerFromOtherTeams(scoreboard, player, teamName)

        if (tag.isBlank()) {
            scoreboard.getTeam(teamName)?.unregister()
            return
        }

        val team = scoreboard.getTeam(teamName) ?: scoreboard.registerNewTeam(teamName).apply {
            setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS)
        }

        val prefix: Component = plugin.messageHandler.formatMixedTextToMiniMessage("$tag <reset>", TagResolver.empty())
        team.prefix(prefix)
        team.suffix(Component.empty())

        if (!team.entries.contains(player.name)) {
            team.addEntry(player.name)
        }
    }

    private fun removePlayerFromOtherTeams(scoreboard: Scoreboard, player: Player, currentTeamName: String) {
        scoreboard.teams
            .filter { it.name != currentTeamName && it.entries.contains(player.name) }
            .forEach { it.removeEntry(player.name) }
    }

    private fun teamName(player: Player): String {
        val uuid = player.uniqueId.toString().replace("-", "")
        val suffix = if (uuid.length >= 14) uuid.substring(0, 14) else uuid
        return "tx$suffix"
    }
}
