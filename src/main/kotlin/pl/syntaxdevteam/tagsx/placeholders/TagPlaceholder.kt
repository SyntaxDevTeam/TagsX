package pl.syntaxdevteam.tagsx.placeholders

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.jetbrains.annotations.NotNull
import pl.syntaxdevteam.tagsx.TagsX

@Suppress("UnstableApiUsage")
class TagPlaceholder(private val plugin: TagsX) : PlaceholderExpansion() {

    override fun getIdentifier(): @NotNull String {
        return "tagsx"
    }

    override fun getAuthor(): @NotNull String {
        return "SyntaxDevTeam"
    }

    override fun getVersion(): @NotNull String {
        return plugin.pluginMeta.version
    }

    override fun onRequest(player: OfflinePlayer?, @NotNull params: String): String? {
        if (player == null) return null

        return when (params.lowercase()) {
            "tag" -> {
                val tag = plugin.tagStorage.getTag(player.name ?: "")
                if (tag.isNotEmpty()) plugin.config.getString("default_tags_format", "§f[§r$tag§f]§r")?.replace("{tag}", tag) ?: ""
                else ""
            }
            else -> null
        }
    }

    override fun onPlaceholderRequest(player: Player, @NotNull params: String): String? {
        return onRequest(player as OfflinePlayer, params)
    }
}