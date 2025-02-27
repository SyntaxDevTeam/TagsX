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
                permission == null || player.hasPermission(permission) // Sprawdzanie uprawnień
            } ?: return

        // Zmieniamy wielkość GUI, żeby pasowała do ilości dostępnych tagów
        val guiSize = 9 * ((availableTags.size / 9) + 1)
        val gui: Inventory = Bukkit.createInventory(null, guiSize, "Wybierz swój tag")

        // Tworzymy przedmioty dla każdego tagu
        availableTags.forEachIndexed { index, tagKey ->
            val tagDisplayName = plugin.config.getString("tags.$tagKey.display") ?: "§7[$tagKey]"
            val tagMaterial = Material.matchMaterial(plugin.config.getString("tags.$tagKey.material") ?: "STONE") ?: Material.STONE

            // Tworzymy przedmiot tagu
            gui.setItem(index, createTagItem(tagMaterial, tagKey, player))
        }

        // Otwieramy GUI dla gracza
        player.openInventory(gui)
    }

    // Funkcja do tworzenia przedmiotu z tagiem
    private fun createTagItem(material: Material, tagKey: String, player: Player): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta?.clone() // Tworzymy kopię, aby uniknąć modyfikacji oryginału

        // Pobieramy nazwę tagu z konfiguracji
        val tagDisplayName = plugin.config.getString("tags.$tagKey.display") ?: "[Brak]"

        // Ustawiamy nazwę przedmiotu w formacie: [TAG] Nick
        val fullDisplayName = "§f[$tagDisplayName§f] §r${player.name}"  // Zmieniliśmy z player.displayName na player.name

        if (meta != null) {
            meta.setDisplayName(fullDisplayName)

            // Dodanie opisu przedmiotu
            meta.lore = listOf("§7Naciśnij, aby ustawić ten tag")

            item.itemMeta = meta
        }

        return item
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        // Sprawdzamy, czy to kliknięcie dotyczy okna wyboru tagu
        if (event.view.title != "Wybierz swój tag") return

        event.isCancelled = true // Anulujemy domyślne zachowanie kliknięcia
        val player = event.whoClicked as Player
        val clickedItem = event.currentItem ?: return

        // Sprawdzamy, który tag został kliknięty
        val selectedTag = plugin.config.getConfigurationSection("tags")?.getKeys(false)
            ?.find { tagKey ->
                val tagMaterial = Material.matchMaterial(plugin.config.getString("tags.$tagKey.material") ?: "STONE")
                tagMaterial == clickedItem.type
            } ?: return

        // Ustawiamy tag gracza
        val tagDisplay = plugin.config.getString("tags.$selectedTag.display") ?: "[Brak]"
        plugin.tagStorage.setTag(player.name, tagDisplay)
        plugin.tagStorage.saveTags()

        // Wysyłamy wiadomość do gracza z prefiksem
        plugin.sendMessageWithPrefix(player, "§aTwój nowy tag: §f$tagDisplay")

        // Zamykamy GUI
        player.closeInventory()
    }
}