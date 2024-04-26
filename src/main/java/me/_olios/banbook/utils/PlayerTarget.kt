package me._olios.banbook.utils

import me._olios.banbook.BanBook
import org.bukkit.BanList
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

class PlayerTarget(private val player: Player, private val plugin: BanBook) {
    private val config = plugin.config

    // add player to the targeted player list
    fun targetUUID() {
        val currentTime = LocalDateTime.now()
        val endTargetTime = currentTime.plusHours(24)

        BanBook.targetedPlayer[player.uniqueId] = endTargetTime
    }

    // check if player was targeted
    fun hasDied() {
        val currentTime = LocalDateTime.now()
        val targetTime = BanBook.targetedPlayer[player.uniqueId]
        if (currentTime.isBefore(targetTime))
            targetDied()
        else notTargeted()
    }

    // Remove the player from the targeted player list if the time past
    private fun notTargeted() {
        BanBook.targetedPlayer.remove(player.uniqueId)
    }

    // The player has died when it was targeted
    private fun targetDied() {
        // Get the config values
        val configBanDate = config.getInt("General.BanTime")
        val configBanMessage = config.getString("General.BanMessage")

        val banExpires = LocalDateTime.now().plusDays(configBanDate.toLong())
        val instant = banExpires.atZone(ZoneId.systemDefault()).toInstant() // Convert LocalDateTime to Instant
        val date = Date.from(instant) // Convert Instant to java.util.Date

        // Ban the player
        player.banPlayer(configBanMessage, date)
    }
}