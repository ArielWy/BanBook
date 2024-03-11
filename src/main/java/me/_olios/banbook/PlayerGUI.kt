package me._olios.banbook

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import java.io.File

class PlayerGUI(private val player: Player, plugin: BanBook) {
    private val config = plugin.config

    fun openGUI(pageIndex: Int) {
        val playerSkulls = getPlayerHeads()
        val pages = playerSkulls.chunked(36)
        openPage(player, pages, pageIndex)
    }

    private fun openPage(player: Player, pages: List<List<ItemStack>>, pageIndex: Int) {
        val inventory = Bukkit.createInventory(null, 54, "Online Players Page ${pageIndex + 1}")

        // create items
        val borderSlot = createItemStack(Material.GRAY_STAINED_GLASS_PANE, "")
        val infoBook = createItemStack(Material.BOOK, "info-item.name", listOf("info-item.lore"), true)

        // Set items in the inventory
        inventory.setItem(4, infoBook)
        for (i in 9..17) inventory.setItem(i, borderSlot)

        // Add skulls to the inventory
        pages[pageIndex].forEachIndexed { index, skull ->
            inventory.setItem((index + 18), skull)
        }

        // Add navigation buttons
        if (pageIndex > 0) {
            // Add "previous page" button at the bottom left
            val previousPageButton = createItemStack(Material.REDSTONE_ORE, "previous-button.name",
                listOf("previous-button.lore"), true)
            inventory.setItem(3, previousPageButton)
        }
        if (pageIndex < pages.size - 1) {
            // Add "next page" button at the bottom right
            val nextPageButton = createItemStack(Material.SCULK_SENSOR, "next-button.name",
                listOf("next-button.lore"), true)
            inventory.setItem(5, nextPageButton)
        }

        player.openInventory(inventory)
    }

    private fun getPlayerHeads(): List<ItemStack> {
        val onlinePlayers = Bukkit.getOnlinePlayers()
        return onlinePlayers.map { player ->
            val head = ItemStack(Material.PLAYER_HEAD)
            val meta = head.itemMeta as SkullMeta
            meta.owningPlayer = player
            meta.displayName(Component.text(getPlayerSkullName()))
            val loreComponents = getPlayerSkullLore().map { Component.text(it) }
            meta.lore(loreComponents)
            head.itemMeta = meta
            head
        }
    }

    private fun createItemStack(material: Material, displayName: String, lore: List<String>? = null, fromConfig: Boolean = false): ItemStack {
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

    private fun getPlayerSkullName(): String {
        var name = config.getString("skull.name")
        name = name?.replace("{player}", player.name)
        return ChatColor.translateAlternateColorCodes('§', name ?: "")
    }

    private fun getPlayerSkullLore(): List<String> {
        val lore = config.getStringList("skull.lore")
        return lore.map { skullLore ->
            var result = ChatColor.translateAlternateColorCodes('§', skullLore)
            result = result.replace("{player}", player.name)
            result
        }
    }

}