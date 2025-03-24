package pl.syntaxdevteam.tagsx.gui

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import pl.syntaxdevteam.tagsx.TagsX
import pl.syntaxdevteam.tagsx.listeners.PlayerJoinListener

class TagsGui(private val plugin: TagsX) : Listener {
    private val pjl: PlayerJoinListener = PlayerJoinListener(plugin)

    fun open(player: Player) {
        val availableTags = plugin.config.getConfigurationSection("tags")?.getKeys(false)
            ?.filter { tag ->
                val permission = plugin.config.getString("tags.$tag.permission")
                permission == null || player.hasPermission(permission)
            } ?: return

        val guiSize = 9 * ((availableTags.size / 9) + 1)
        val gui: Inventory = Bukkit.createInventory(null, guiSize, plugin.messageHandler.getLogMessage("tags", "title"))

        availableTags.forEachIndexed { index, tagKey ->
            val tagMaterial = Material.matchMaterial(plugin.config.getString("tags.$tagKey.material") ?: "STONE") ?: Material.STONE

            gui.setItem(index, createTagItem(tagMaterial, tagKey, player))
        }

        player.openInventory(gui)
    }

    private fun createTagItem(material: Material, tagKey: String, player: Player): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta?.clone()
        val tagDisplayName = plugin.config.getString("tags.$tagKey.display") ?: "" //Bo po co masz mieć na czacie jakieś NONE czy tam BRAK

        val fullDisplayName = plugin.messageHandler.getLogMessage("tags", "display_tag", mapOf("tag_display_name" to tagDisplayName, "player" to player.name))

        if (meta != null) {
            meta.displayName(fullDisplayName)

            meta.lore(listOf(plugin.messageHandler.getLogMessage("tags", "info")))

            item.itemMeta = meta
        }

        return item
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.view.title() != plugin.messageHandler.getLogMessage("tags", "title")) return

        event.isCancelled = true
        val player = event.whoClicked as Player
        val clickedItem = event.currentItem ?: return
        val selectedTag = plugin.config.getConfigurationSection("tags")?.getKeys(false)
            ?.find { tagKey ->
                val tagMaterial = Material.matchMaterial(plugin.config.getString("tags.$tagKey.material") ?: "STONE")
                tagMaterial == clickedItem.type
            } ?: return
        val tagDisplay = plugin.config.getString("tags.$selectedTag.display") ?: ""

        plugin.tagStorage.setTag(player.name, tagDisplay)
        plugin.tagStorage.saveTags()

        player.sendMessage(plugin.messageHandler.getMessage("tags", "new_tag", mapOf("tag_display" to tagDisplay)))
        pjl.setTag(player) // Upewniam się że tag jest ustawiony bez konieczności relogowania
        player.closeInventory()
    }
}