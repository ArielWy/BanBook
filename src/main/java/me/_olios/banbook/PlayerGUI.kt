package me._olios.banbook

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.io.File

class PlayerGUI(private val player: Player, private val plugin: BanBook) {

    fun openGUI(pageIndex: Int) {
        val playerSkulls = getPlayerHeads()
        val pages = playerSkulls.chunked(36)
        openPage(player, pages, pageIndex)
    }

    private fun openPage(player: Player, pages: List<List<ItemStack>>, pageIndex: Int) {
        val inventory = Bukkit.createInventory(null, 54, "Online Players Page ${pageIndex + 1}")

        // Add skulls to the inventory
        pages[pageIndex].forEachIndexed { index, skull ->
            inventory.setItem(index, skull)
        }

        // Add navigation buttons
        if (pageIndex > 0) {
            // Add "previous page" button at the bottom left
            player.sendMessage("previous page")
        }
        if (pageIndex < pages.size - 1) {
            // Add "next page" button at the bottom right
            player.sendMessage("next page")
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


    private fun getPlayerSkullName(): String {
        val config = YamlConfiguration.loadConfiguration(File("config.yml"))
        var name = config.getString("skull.name")
        name = name?.replace("{player}", player.toString())
        return ChatColor.translateAlternateColorCodes('&', name ?: "")
    }

    private fun getPlayerSkullLore(): List<String> {
        val config = YamlConfiguration.loadConfiguration(File("config.yml"))
        val lore = config.getStringList("skull.lore")
        return lore.map { skullLore ->
            var result = ChatColor.translateAlternateColorCodes('&', skullLore)
            result = result.replace("{player}", player.toString())
            result
        }
    }

}