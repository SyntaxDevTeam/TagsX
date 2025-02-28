package pl.syntaxdevteam.tagsx.storage

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import pl.syntaxdevteam.tagsx.TagsX
import java.io.File

class TagStorage(private val plugin: TagsX) {

    private lateinit var tagsFile: File

    private val tags = mutableMapOf<String, String>()

    fun loadTags() {
        tagsFile = File(plugin.dataFolder, "tags.yml")

        if (!tagsFile.exists()) {
            tagsFile.parentFile.mkdirs()
            plugin.saveResource("tags.yml", false)
        }
        val tagsConfig: FileConfiguration = YamlConfiguration.loadConfiguration(tagsFile)
        for (key in tagsConfig.getKeys(false)) {
            tags[key] = tagsConfig.getString(key) ?: ""
        }

        plugin.logger.debug("Załadowano ${tags.size} tagów z tags.yml!")
    }

    fun saveTags() {
        val tagsConfig: FileConfiguration = YamlConfiguration.loadConfiguration(tagsFile)
        for ((player, tag) in tags) {
            tagsConfig.set(player, tag)
        }

        tagsConfig.save(tagsFile)
        plugin.logger.debug("Zapisano tagi do tags.yml!")
    }

    fun setTag(player: String, tag: String) {
        val tagsConfig: FileConfiguration = YamlConfiguration.loadConfiguration(tagsFile)
        tags[player] = tag
        tagsConfig.set(player, tag)
        tagsConfig.save(tagsFile)

        plugin.logger.debug("Ustawiono tag '$tag' dla gracza $player i zapisano do tags.yml")
    }

    fun getTag(player: String): String {
        return tags[player] ?: "Brak"
    }
}
