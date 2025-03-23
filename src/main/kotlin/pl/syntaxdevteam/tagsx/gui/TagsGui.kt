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
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

class TagsGui(private val plugin: TagsX) : Listener {

    private val legacySerializer = LegacyComponentSerializer.legacySection()

    fun open(player: Player) {
        val availableTags = plugin.config.getConfigurationSection("tags")?.getKeys(false)
            ?.filter { tag ->
                val permission = plugin.config.getString("tags.$tag.permission")
                permission == null || player.hasPermission(permission)
            } ?: return

        val guiSize = 9 * ((availableTags.size / 9) + 1)
        val gui: Inventory = Bukkit.createInventory(null, guiSize, "Select Your Tag")

        availableTags.forEachIndexed { index, tagKey ->
            val tagDisplayName = plugin.config.getString("tags.$tagKey.display") ?: "§7[$tagKey]"
            val tagMaterial = Material.matchMaterial(plugin.config.getString("tags.$tagKey.material") ?: "STONE") ?: Material.STONE

            gui.setItem(index, createTagItem(tagMaterial, tagKey, player))
        }

        player.openInventory(gui)
    }

    private fun createTagItem(material: Material, tagKey: String, player: Player): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta?.clone()

        val tagDisplayName = plugin.config.getString("tags.$tagKey.display") ?: "[None]"

        val fullDisplayName = "§f[$tagDisplayName§f] §r${player.name}"

        if (meta != null) {
            meta.setDisplayName(fullDisplayName)
            meta.lore = listOf("§7Click to set this tag")

            item.itemMeta = meta
        }

        return item
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.view.title != "Select Your Tag") return

        event.isCancelled = true
        val player = event.whoClicked as Player
        val clickedItem = event.currentItem ?: return

        val selectedTag = plugin.config.getConfigurationSection("tags")?.getKeys(false)
            ?.find { tagKey ->
                val tagMaterial = Material.matchMaterial(plugin.config.getString("tags.$tagKey.material") ?: "STONE")
                tagMaterial == clickedItem.type
            } ?: return

        val tagDisplay = plugin.config.getString("tags.$selectedTag.display") ?: "[None]"
        plugin.tagStorage.setTag(player.name, tagDisplay)
        plugin.tagStorage.saveTags()

        plugin.sendMessageWithPrefix(player, "§aYour new tag: §f$tagDisplay")

        player.closeInventory()
    }
}
