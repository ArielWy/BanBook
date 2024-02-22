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

    fun setGUI() {
        val onlinePlayers = Bukkit.createInventory(player, 36, "Online Players")
    }

    fun getPlayerHeads() {
        val onlinePlayers = Bukkit.getOnlinePlayers()
        return onlinePlayers.forEach { player ->
            val head = ItemStack(Material.PLAYER_HEAD)
            val meta = head.itemMeta as SkullMeta
            meta.owningPlayer = player
            meta.displayName(Component.text(getPlayerSkullName()))
            val loreComponents = getPlayerSkullLore().map { Component.text(it) }
            meta.lore(loreComponents)
            head.itemMeta = meta
        }
    }

    private fun getPlayerSkullName(): String {
        val config = YamlConfiguration.loadConfiguration(File("config.yml"))
        var name = config.getString("skull.name")
        name = name?.replace("{player}", player.toString())
        return ChatColor.translateAlternateColorCodes('§', name ?: "")
    }

    private fun getPlayerSkullLore(): List<String> {
        val config = YamlConfiguration.loadConfiguration(File("config.yml"))
        val lore = config.getStringList("skull.lore")
        return lore.map { skullLore ->
            var result = ChatColor.translateAlternateColorCodes('§', skullLore)
            result = result.replace("{player}", player.toString())
            result
        }
    }

}