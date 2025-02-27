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
import pl.syntaxdevteam.tagsx.data.TagList

class TagsGui(private val plugin: TagsX) : Listener {

    fun open(player: Player) {
        val gui: Inventory = Bukkit.createInventory(null, 27, "Wybierz swój tag")

        TagList.tags.forEachIndexed { index, tagItem ->
            gui.setItem(11 + index, createTagItem(tagItem.material, tagItem.name))
        }

        player.openInventory(gui)
    }

    private fun createTagItem(material: Material, tagName: String): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta!!
        meta.setDisplayName("§7TAG: §r[$tagName]")
        item.itemMeta = meta
        return item
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.view.title != "Wybierz swój tag") return

        event.isCancelled = true
        val player = event.whoClicked as Player
        val clickedItem = event.currentItem ?: return

        val selectedTag = TagList.tags.find { it.material == clickedItem.type } ?: return

        plugin.tagStorage.setTag(player.name, selectedTag.name)
        plugin.tagStorage.saveTags()
        player.sendMessage("§aTwój nowy tag: §f[${selectedTag.name}]")
        player.closeInventory()
    }
}
