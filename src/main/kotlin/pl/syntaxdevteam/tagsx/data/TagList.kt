package pl.syntaxdevteam.tagsx.data

import org.bukkit.Material

object TagList {
    val tags = listOf(
        TagItem(Material.RED_DYE, "§cWojownik§r"),
        TagItem(Material.BLUE_DYE, "§dMag§r"),
        TagItem(Material.BLACK_DYE, "§0Mroczny§r"),
        TagItem(Material.YELLOW_DYE, "§bBłyskawica§r"),
        TagItem(Material.PURPLE_DYE, "§6Król§r"),
        TagItem(Material.GREEN_DYE, "§aSzczęściarz§r")

    )
}
// §§§

data class TagItem(val material: Material, val name: String)
