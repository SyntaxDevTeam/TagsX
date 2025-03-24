package pl.syntaxdevteam.tagsx.common

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import pl.syntaxdevteam.tagsx.TagsX

class ConfigHandler(private val plugin: TagsX) {

    fun verifyAndUpdateConfig() {
        val confFile = File(plugin.dataFolder, "config.yml")
        val defaultConfStream = plugin.getResource("config.yml")

        if (defaultConfStream == null) {
            plugin.logger.err("Default $confFile file not found in plugin resources!")
            return
        }

        val defaultConfig = YamlConfiguration.loadConfiguration(defaultConfStream.reader())
        val currentConfig = YamlConfiguration.loadConfiguration(confFile)

        var updated = false

        fun synchronizeSections(defaultSection: ConfigurationSection, currentSection: ConfigurationSection) {
            for (key in defaultSection.getKeys(false)) {
                if (!currentSection.contains(key)) {
                    currentSection[key] = defaultSection[key]
                    updated = true
                } else if (defaultSection.isConfigurationSection(key)) {
                    synchronizeSections(
                        defaultSection.getConfigurationSection(key)!!,
                        currentSection.getConfigurationSection(key)!!
                    )
                }
            }
        }

        synchronizeSections(defaultConfig, currentConfig)

        if (updated) {
            plugin.logger.success("Updating $confFile file with missing entries.")
            currentConfig.save(confFile)
        }
    }
}
