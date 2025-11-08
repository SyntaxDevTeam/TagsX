package pl.syntaxdevteam.tagsx.data

import org.bukkit.Material
import pl.syntaxdevteam.tagsx.TagsX

object TagList {
    val tags = mutableListOf<TagItem>()

    fun loadTags(plugin: TagsX) {
        tags.clear()
        val config = plugin.config.getConfigurationSection("tags") ?: return

        for (key in config.getKeys(false)) {
            val display = config.getString("tags.$key.display") ?: continue
            val material = Material.matchMaterial(config.getString("tags.$key.material") ?: "NAME_TAG") ?: Material.NAME_TAG

            tags.add(TagItem(material, display))
        }
        plugin.logger.info("✅ Loaded ${tags.size} tags from config.yml!")
    }
}

data class TagItem(val material: Material, val name: String)

