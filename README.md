# 🏷️ TagsX


### 📝 Description
TagsX is a lightweight and intuitive plugin that allows players to select custom tags through an interactive GUI menu. Administrators can easily manage tags directly from the configuration file, while selected tags are stored in `tags.yml`. The plugin supports PlaceholderAPI with the `%tagsx_tag%` placeholder, making it compatible with many other plugins.

### 🔑 Commands
| Command        | Description                  | Permission      |
|---------------|-----------------------------|----------------|
| `/tags`       | Opens the tags selection GUI (for all players) | `tagsx.command.tags`         |
| `/tagsx reload` | Reloads the plugin configuration | `tagsx.reload` |

### 🔒 Permissions
- `tagsx.reload` — Allows reloading the plugin configuration with `/tagsx reload`
- `tagsx.command.tags` — Allows you to open a gui tags with `/tags`

### ⚙️ Configuration
Tags can be added in:
```
./plugins/TagsX/config.yml
```
Example:
```yaml
 tags:
   name1:
     display-name: "§bname1"
     display: "§bNAME1"
     material: "BLUE_DYE"
     permission: "tag.name1"

   name2:
     display-name: "§dNAME2"
     display: "§dNAME2"
     material: "BLACK_DYE"
     permission: "tag.name2"
```
Each tag must have a unique identifier and display name that will be shown in the GUI.

### 🧑‍💻 Player Experience
1. Use `/tags` to open the tags GUI.
2. Select a tag by clicking on it.
3. The selected tag is automatically saved in `tags.yml`.
4. Display the selected tag using the `%tagsx_tag%` placeholder (requires PlaceholderAPI).

### 🔌 PlaceholderAPI Support
| Placeholder   | Description                  |
|---------------|-----------------------------|
| `%tagsx_tag%` | Displays the currently selected player tag |

---
⭐ **TagsX is the perfect solution for personalizing player identities and making your Minecraft server unique!**
