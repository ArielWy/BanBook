package me._olios.banbook.GUI

import me._olios.banbook.BanBook
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType

class BanBookGUI(private val player: Player, private val plugin: BanBook) {
    private val config = plugin.config

    fun openGUI(pageIndex: Int, isBanBookGUI: Boolean) {
        player.sendMessage("page index: $pageIndex, open ban book: $isBanBookGUI")

        val playerSkulls: List<ItemStack> = if (isBanBookGUI)
            getPlayerHeads("Items.BanBookSkull.name", "Items.BanBookSkull.lore", true)
        else getPlayerHeads("Items.ReviveBookSkull.name", "Items.ReviveBookSkull.lore", false)
        val pages = playerSkulls.chunked(36)
        if (isBanBookGUI)
            openPage(player, pages, pageIndex, "Items.BanBookInfoItem.name", "Items.BanBookInfoItem.lore")
        else openPage(player, pages, pageIndex, "Items.ReviveBookInfoItem.name", "Items.ReviveBookInfoItem.lore")
    }

    private fun openPage(player: Player, pages: List<List<ItemStack>>, pageIndex: Int, infoNamePath: String, infoLorePath: String) {
        player.sendMessage("pages: $pages")

        val inventory = Bukkit.createInventory(null, 54, "Online Players Page ${pageIndex + 1}")
        BanBook.playerInventory[player.uniqueId] = inventory

        // create items
        val borderSlot = createItemStack(Material.GRAY_STAINED_GLASS_PANE, "")
        val infoBook = createItemStack(Material.BOOK, infoNamePath, listOf(infoLorePath), true)

        // Set items in the inventory
        inventory.setItem(4, infoBook)
        for (i in 9..17) inventory.setItem(i, borderSlot)

        // If there is, add skulls to the inventory
        if (pageIndex < pages.size && pages[pageIndex].isNotEmpty()) {
            pages[pageIndex].forEachIndexed { index, skull ->
                inventory.setItem((index + 18), skull)
            }
        }

        // Add navigation buttons
        if (pageIndex > 0) {
            // Add "previous page" button at the bottom left
            val previousPageButton = createItemStack(Material.REDSTONE_ORE, "Items.PreviousButton.name",
                listOf("Items.PreviousButton.lore"), true)
            inventory.setItem(3, previousPageButton)
        }
        if (pageIndex < pages.size - 1) {
            // Add "next page" button at the bottom right
            val nextPageButton = createItemStack(Material.SCULK_SENSOR, "Items.NextButton.name",
                listOf("Items.NextButton.lore"), true)
            inventory.setItem(5, nextPageButton)
        }

        player.openInventory(inventory) // Open the GUI
    }

    private fun getPlayerHeads(namePath: String, lorePath: String, isBanBookGUI: Boolean): List<ItemStack> {
        player.sendMessage("getPlayerHeads")

        val skullKey = NamespacedKey(plugin, "is_ban_book_GUI")
        val onlinePlayers = Bukkit.getOnlinePlayers()

        val banList: BanList<Player> = Bukkit.getBanList(BanList.Type.PROFILE)
        val banEntries: MutableSet<BanEntry<in Player>> = banList.getEntries()


        player.sendMessage("ban list: $banList, ban entries: $banEntries")

        return if (isBanBookGUI)
            onlinePlayers.map { onlinePlayerHead(it, skullKey, namePath, lorePath) }
        else {
            val configReason = config.getString("Messages.BanMessage")  // Get the config reason
            player.sendMessage("ban entries size: ${banEntries.size}\nconfigReason: $configReason\n")
            banEntries.mapNotNull { banEntry ->
                val playerUUID = banEntry.target
                val player = Bukkit.getOfflinePlayer(playerUUID)
                if (player.hasPlayedBefore() && banEntry.reason == configReason) {
                    banPlayerHead(player, skullKey, namePath, lorePath)
                } else {
                    null
                }
            }
        }
    }

    private fun onlinePlayerHead(player: Player, skullKey: NamespacedKey, namePath: String, lorePath: String): ItemStack {
        val head = ItemStack(Material.PLAYER_HEAD)
        val meta = head.itemMeta as SkullMeta
        meta.persistentDataContainer.set(skullKey, PersistentDataType.BOOLEAN, true)
        meta.owningPlayer = player

        // Set the display as the config
        meta.displayName(Component.text(getPlayerSkullName(namePath, player)))
        val loreComponents = getPlayerSkullLore(lorePath).map { Component.text(it) }
        meta.lore(loreComponents)
        head.itemMeta = meta
        return head
    }

    private fun banPlayerHead(banPlayer: OfflinePlayer, skullKey: NamespacedKey, namePath: String, lorePath: String): ItemStack {
        player.sendMessage("Processing player: ${banPlayer.name}")  // Print the name of the player being processed
        player.sendMessage("Reason matches config reason")  // Print a message if the reason matches the config reason
        val head = ItemStack(Material.PLAYER_HEAD)
        val meta = head.itemMeta as SkullMeta
        meta.persistentDataContainer.set(skullKey, PersistentDataType.BOOLEAN, false)
        meta.owningPlayer = banPlayer

        // Set the display as the config
        meta.displayName(Component.text(getPlayerSkullName(namePath, banPlayer)))
        val loreComponents = getPlayerSkullLore(lorePath).map { Component.text(it) }
        meta.lore(loreComponents)
        head.itemMeta = meta
        return head
    }

    fun createItemStack(material: Material, displayName: String, lore: List<String>? = null, fromConfig: Boolean = false): ItemStack {
        val itemStack = ItemStack(material)
        val itemMeta: ItemMeta = itemStack.itemMeta

        var finalDisplayName = displayName
        var finalLore: List<String> = lore ?: listOf()

        if (fromConfig) {
            val configItemStack = configItemStack(displayName, lore?.get(0))
            finalDisplayName = configItemStack.first
            finalLore = configItemStack.second
        }

        itemMeta.displayName(Component.text(finalDisplayName))
        if (finalLore.isNotEmpty()) {
            val loreComponents = finalLore.map { Component.text(it) }
            itemMeta.lore(loreComponents)
        }
        itemStack.itemMeta = itemMeta

        return itemStack
    }

    private fun configItemStack(displayName: String, lore: String? = null): Pair<String, List<String>> {
        // Set the displayName from the config
        var name = config.getString(displayName)
        name = name?.replace("{player}", player.name)
        val finalDisplayName = ChatColor.translateAlternateColorCodes('§', name ?: "")

        // Set the lore from the config
        var finalLore: List<String> = listOf()
        if (lore != null) {
            val configLore = config.getStringList(lore)
            finalLore = configLore.map { skullLore ->
                var result = ChatColor.translateAlternateColorCodes('§', skullLore)
                result = result.replace("{player}", player.name)
                result
            }
        }

        return Pair(finalDisplayName, finalLore)
    }

    private fun getPlayerSkullName(path: String, player: OfflinePlayer): String {
        var name = config.getString(path)
        name = name?.replace("{player}", player.name.toString())
        return ChatColor.translateAlternateColorCodes('§', name ?: "")
    }

    private fun getPlayerSkullLore(path: String): List<String> {
        val lore = config.getStringList(path)
        return lore.map { skullLore ->
            var result = ChatColor.translateAlternateColorCodes('§', skullLore)
            result = result.replace("{player}", player.name)
            result
        }
    }

}