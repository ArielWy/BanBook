package me._olios.banbook.listeners

import me._olios.banbook.BanBook
import me._olios.banbook.GUI.confirmGUI
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.block.Skull
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.meta.SkullMeta


class InventoryClickListener(private val plugin: BanBook): Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as Player
        val playerUUID = player.uniqueId
        val item = event.currentItem
        val inventory = event.clickedInventory
        val gui = BanBook.playerInventory[playerUUID]

        if (inventory != gui) return // the player doesn't interact with the gui
        event.isCancelled = true // Prevent people from interacting with their inventory

        if (item?.itemMeta !is SkullMeta) return
        val skullMeta = item.itemMeta as SkullMeta
        val targetedPlayer = skullMeta.owningPlayer as Player

        if (!targetedPlayer.isOnline) return

        confirmGUI(player, plugin).confirm(targetedPlayer, item)
    }
}