package me._olios.banbook.listeners

import me._olios.banbook.BanBook
import me._olios.banbook.handler.TargetHandler
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class PlayerDeathListener(private val plugin: BanBook): Listener {

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.player
        if (BanBook.targetedPlayer.containsKey(player.uniqueId))
            TargetHandler(player, plugin).hasDied()
    }
}