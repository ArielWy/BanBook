package me._olios.banbook.listeners

import me._olios.banbook.BanBook
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.persistence.PersistentDataType

class InventoryCloseListener(private val plugin: BanBook): Listener {

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val player = event.player as Player

        targetCheck(player) // remove PDC from the player
    }

    private fun targetCheck(player: Player) {
        // Check if the player has Data Container
        val key1 = NamespacedKey(plugin, "target")
        val targetPlayer = player.persistentDataContainer.get(key1, PersistentDataType.STRING) ?: return

        // Remove the persistentDataContainer from the player
        player.persistentDataContainer.remove(key1)
    }
}