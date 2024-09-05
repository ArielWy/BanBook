package me._olios.banbook.listeners

import me._olios.banbook.BanBook
import me._olios.banbook.handler.TargetHandler
import org.bukkit.Bukkit.getServer
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.persistence.PersistentDataContainer


class PlayerDeathListener(private val plugin: BanBook): Listener {

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.player

        val key = NamespacedKey(plugin, "hunt_end_date") // Define the NamespacedKey
        val targetPDC: PersistentDataContainer = player.persistentDataContainer // Get the target's PDC
        if (!targetPDC.has(key)) return

        getServer().scheduler.runTaskLater(plugin, Runnable {
            TargetHandler(player, plugin).hasDied()
        }, 20)
    }
}