package me._olios.banbook.GUI

import me._olios.banbook.BanBook
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType

class ConfirmGUI(private val player: Player, private val plugin: BanBook) {
    private val config = plugin.config

    fun confirm(targetedPlayer: Player) {
        val inventory = Bukkit.createInventory(null, 9, "Confirm")
        BanBook.playerInventory[player.uniqueId] = inventory

        // Set the inventory items
        for (i in 0..3) inventory.setItem(i, PlayerGUI(player, plugin).createItemStack(Material.LIME_STAINED_GLASS_PANE, "§aCONFIRM"))
        for (i in 5..8) inventory.setItem(i, PlayerGUI(player, plugin).createItemStack(Material.RED_STAINED_GLASS_PANE, "§cDENIED"))
        inventory.setItem(4, getConfirmItem(targetedPlayer))

        player.openInventory(inventory)
        setTarget(targetedPlayer)
    }

    private fun setTarget(targetedPlayer: Player) {
        // set the target
        val key = NamespacedKey(plugin, "target")
        player.persistentDataContainer.set(key, PersistentDataType.STRING, targetedPlayer.name)
    }


    private fun getConfirmItem(owningPlayer: Player): ItemStack {
        val confirmItem = ItemStack(Material.PLAYER_HEAD)
        val confirmItemMeta = confirmItem.itemMeta as SkullMeta

        // Assign the owner skull
        confirmItemMeta.owningPlayer = owningPlayer

        // Assign the item display name
        var name = config.getString("confirm-item.name")
        name = name?.replace("{player}", owningPlayer.name)
        name = ChatColor.translateAlternateColorCodes('§', name ?: "")
        confirmItemMeta.displayName(Component.text(name))

        // Assign the item display lore
        val configLore = config.getStringList("confirm-item.lore")
        val lore = configLore.map { loreLine ->
            var result = ChatColor.translateAlternateColorCodes('§', loreLine)
            result = result.replace("{player}", owningPlayer.name)
            result
        }
        val loreComponents = lore.map { Component.text(it) }
        confirmItemMeta.lore(loreComponents)

        // Return the ItemStack
        confirmItem.itemMeta = confirmItemMeta
        return confirmItem
    }
}