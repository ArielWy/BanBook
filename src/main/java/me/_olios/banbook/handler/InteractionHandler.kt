package me._olios.banbook.handler

import me._olios.banbook.BanBook
import net.kyori.adventure.text.Component
import org.bukkit.BanList
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.io.File
import java.util.UUID

class InteractionHandler(private val plugin: BanBook) {
        private val config = plugin.config

    fun banBookHandler(player: Player, target: Player) {

        val banBook = retrieve("BanBookItem")

        if (player.inventory.itemInMainHand != banBook) {
            return
        }

        // Define target
        TargetHandler(target, plugin).targetUUID()

        // alert the players
        targetedPlayerAlert(player, target, "Messages.TargetedPlayerMessage", "General.TargetedPlayerAlert")

        // remove the used items
        removeUsedBanBooks(player)
    }

    fun reviveBookHandler(player: Player, target: OfflinePlayer) {
        val reviveBook = retrieve("ReviveBookItem")

        if (player.inventory.itemInMainHand != reviveBook) {
            return
        }

        // revive the target
        revive(target)

        // alert the players
        targetedPlayerAlert(player, target, "Messages.TargetedPlayerReviveAlert", "General.TargetedPlayerReviveAlert")

        // remove the used items
        removeUsedBanBooks(player)
    }

    private fun targetedPlayerAlert(player: Player, target: OfflinePlayer, alertPath: String, alertTogglePath: String) {
        // Send Messages if toggled in the config
        var alert: String = config.getString(alertPath) ?: return

        val placeholders: Map<String, String> = mapOf(
            "{player}" to player.name,
            "{target}" to target.name.toString()
        )

        // Replace placeholders with their corresponding values
        for ((placeholder, value) in placeholders) {
            alert = alert.replace(placeholder, value)
        }

        // if toggled, broadcast to all the players
        if (config.getBoolean(alertTogglePath))
            Bukkit.broadcast(Component.text(alert))
        else player.sendMessage(Component.text(alert)) // else, send to the player
    }

    private fun removeUsedBanBooks(player: Player) {
        // Set the main hand slot to air
        val airItem = ItemStack(Material.AIR)
        player.inventory.setItemInMainHand(airItem)

    }

    private fun retrieve(itemPath: String): ItemStack? {
        val defineFile = File(plugin.dataFolder, "define.yml")
        val defineConfig: FileConfiguration = YamlConfiguration.loadConfiguration(defineFile)

        // Retrieving the value
        val loadedItemStack: ItemStack? = defineConfig.getItemStack(itemPath)
        var item: ItemStack? = null
        if (loadedItemStack != null) {
            try {
                item = loadedItemStack
            } catch (e: Exception) {
                // Handle any exceptions during deserialization
                e.printStackTrace()
            }
        }
        return item
    }

    private fun revive(target: OfflinePlayer) {
        if (!target.isBanned) return
        val banList: BanList<Player> = Bukkit.getBanList(BanList.Type.PROFILE)
        banList.pardon(target.name.toString())
    }
}