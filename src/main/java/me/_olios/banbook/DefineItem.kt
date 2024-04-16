package me._olios.banbook

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class DefineItem(private val player: Player, private val plugin: BanBook) {
    private val config = plugin.config

    fun checkForItem() {
        val item: ItemStack = player.inventory.itemInMainHand
        if (item.type == Material.AIR) isCanceled()
        else defineItem(item)
    }

    private fun isCanceled() {
        player.sendMessage("§cYou are not holding anything in you hand!")
    }

    private fun defineItem(item: ItemStack) {
        val itemString = item.toString()
        config.set("BanBook-item", itemString)
        plugin.saveConfig()

        if (config.getString("BanBook-item") == itemString) {
            player.sendMessage("§aDefine the item successfully as §6{§e${config.getString("BanBook-item")}§6}")
        } else {
            player.sendMessage("§cItem not found in the config!")
        }
    }
}