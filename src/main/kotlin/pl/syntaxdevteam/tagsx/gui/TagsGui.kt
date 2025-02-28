package pl.syntaxdevteam.tagsx.gui

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import pl.syntaxdevteam.tagsx.TagsX

class TagsGui(private val plugin: TagsX) : Listener {

    private val tagKeyNamespaced = NamespacedKey(plugin, "tagKey")

    fun open(player: Player) {

        val tagsSection = plugin.config.getConfigurationSection("tags") ?: return
        val availableTags = tagsSection.getKeys(false).filter { key ->
            val permission = plugin.config.getString("tags.$key.permission")
            permission == null || player.hasPermission(permission)
        }

        if (availableTags.isEmpty()) {
            plugin.sendMessageWithPrefix(player, "§cBrak dostępnych tagów.")
            return
        }

        val rows = ((availableTags.size - 1) / 9) + 1
        val guiSize = rows * 9
        val gui: Inventory = Bukkit.createInventory(null, guiSize, plugin.messageHandler.getMessage("tags", "gui_title"))

        availableTags.forEachIndexed { index, tagKey ->
            val tagDisplayName = plugin.config.getString("tags.$tagKey.display") ?: "§7[$tagKey]"
            val tagMaterial = Material.matchMaterial(plugin.config.getString("tags.$tagKey.material") ?: "STONE") ?: Material.STONE
            gui.setItem(index, createTagItem(tagMaterial, tagKey, tagDisplayName, player))
        }
        player.openInventory(gui)
    }

    private fun createTagItem(material: Material, tagKey: String, tagDisplayName: String, player: Player): ItemStack {
        return ItemStack(material).apply {
            val meta = itemMeta ?: return@apply
            val formatDisplayName = "&f[$tagDisplayName&f] §r${player.name}"
            val finalDisplayName = plugin.messageHandler.formatMixedTextToMiniMessage(formatDisplayName, TagResolver.empty())
            meta.displayName(finalDisplayName)

            meta.lore(plugin.messageHandler.getListComponents("tags", "tag_lore"))

            meta.persistentDataContainer.set(tagKeyNamespaced, PersistentDataType.STRING, tagKey)
            itemMeta = meta
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val title = plugin.messageHandler.getMessage("tags", "gui_title")
        if (event.view.title() != title) return

        event.isCancelled = true
        val player = event.whoClicked as? Player ?: return
        val clickedItem = event.currentItem ?: return

        val tagKey = clickedItem.itemMeta?.persistentDataContainer?.get(tagKeyNamespaced, PersistentDataType.STRING) ?: return
        val tagDisplay = plugin.config.getString("tags.$tagKey.display") ?: ""

        plugin.tagStorage.setTag(player.name, tagDisplay)
        plugin.tagStorage.saveTags()

        player.sendMessage(plugin.messageHandler.getMessage("tags", "tag_set", mapOf("newTag" to tagDisplay)))
        player.closeInventory()
    }
}
