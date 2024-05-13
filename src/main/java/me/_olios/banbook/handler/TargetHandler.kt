package me._olios.banbook.handler

import me._olios.banbook.BanBook
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


class TargetHandler(val player: Player, val plugin: BanBook) {
    private val config = plugin.config

    fun targetUUID() {  // add player to the targeted player list
        val currentTime = LocalDateTime.now() // get the current time
        val endTargetTime = currentTime.plusHours(24) // set the hunt end date
        val endDateTimeInMilliseconds = endTargetTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() // convent to long val

        val targetPDC: PersistentDataContainer = player.persistentDataContainer // Get the target's PDC
        val key = NamespacedKey(plugin, "hunt_end_date") // Define the NamespacedKey

        // Save the end date as a long value in milliseconds
        targetPDC.set(key, PersistentDataType.LONG, endDateTimeInMilliseconds)
    }

    fun hasDied() {  // check if player is targeted
        val targetPDC: PersistentDataContainer = player.persistentDataContainer // Get the target's PDC
        val key = NamespacedKey(plugin, "hunt_end_date") // Define the NamespacedKey
        val savedEndDateTimeInMilliseconds = targetPDC.get(key, PersistentDataType.LONG) ?: return // get the date as long val

        // Convert milliseconds back to LocalDateTime
        val retrievedDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(savedEndDateTimeInMilliseconds), ZoneId.systemDefault())

        val currentTime = LocalDateTime.now()
        if (currentTime.isBefore(retrievedDateTime))
            targetDied()
        else notTargeted()
    }

    fun revivePlayer(offlinePlayer: OfflinePlayer) { // Remove player from the ban list
        if (!offlinePlayer.isBanned) return
        val banList: BanList<Player> = Bukkit.getBanList(BanList.Type.PROFILE)
        banList.pardon(offlinePlayer.name.toString())

        val airItem = ItemStack(Material.AIR)
        player.inventory.setItemInMainHand(airItem)  // Set the main hand slot to air
        player.closeInventory()  // Close the inventory

        if (config.getBoolean("General.TargetedPlayerReviveAlert"))
            Bukkit.getServer().broadcastMessage("${offlinePlayer.name} has been unbanned!")
    }

    private fun notTargeted() { // Remove the player from the targeted player list if the time past
        val targetPDC: PersistentDataContainer = player.persistentDataContainer // Get the target's PDC
        val key = NamespacedKey(plugin, "hunt_end_date") // Define the NamespacedKe

        //  Remove the PDC from the player
        targetPDC.remove(key)
    }

    private fun targetDied() { // The player has died when it was targeted
        // Get the config values
        val configBanDate = config.getInt("General.BanTime")
        val configBanMessage = config.getString("Messages.BanMessage")

        val banExpires = LocalDateTime.now().plusDays(configBanDate.toLong()) // Get the ban expires date
        val instant = banExpires.atZone(ZoneId.systemDefault()).toInstant() // Convert LocalDateTime to Instant
        val date = Date.from(instant) // Convert Instant to java.util.Date

        // Ban the player
        player.banPlayer(configBanMessage, date)
    }
 }