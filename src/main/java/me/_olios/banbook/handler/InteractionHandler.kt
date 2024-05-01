package me._olios.banbook.handler

import me._olios.banbook.BanBook
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.io.File

class InteractionHandler(private val player: Player, private val target: Player, private val plugin: BanBook) {
        private val config = plugin.config

    fun handler() {
        val banBook = retrieve()

        if (player.inventory.itemInMainHand != banBook) {
            return
        }

        // Define target
        TargetHandler(target, plugin).targetUUID()

        // alert the players
        targetedPlayerAlert()

        // remove the used items
        removeUsedBanBooks()
    }

    private fun targetedPlayerAlert() {
        // Send Messages if toggled in the config
        var alert: String = config.getString("Messages.TargetedPlayerMessage") ?: return
        val placeholders: Map<String, String> = mapOf(
            "{player}" to player.name,
            "{target}" to target.name
        )

        // Replace placeholders with their corresponding values
        for ((placeholder, value) in placeholders) {
            alert = alert.replace(placeholder, value)
        }

        // if toggled, broadcast to all the players
        if (config.getBoolean("General.TargetedPlayerAlert"))
            Bukkit.broadcast(Component.text(alert))
        else player.sendMessage(Component.text(alert)) // else, send to the player
    }

    private fun removeUsedBanBooks() {
        // Set the main hand slot to air
        val airItem = ItemStack(Material.AIR)
        player.inventory.setItemInMainHand(airItem)

    }

    private fun retrieve(): ItemStack? {
        val defineFile = File(plugin.dataFolder, "define.yml")
        val defineConfig: FileConfiguration = YamlConfiguration.loadConfiguration(defineFile)

        // Retrieving the value
        val loadedItemStack: ItemStack? = defineConfig.getItemStack("BanBookItem")
        var banBookItem: ItemStack? = null
        if (loadedItemStack != null) {
            try {
                banBookItem = loadedItemStack
            } catch (e: Exception) {
                // Handle any exceptions during deserialization
                e.printStackTrace()
            }
        }
        return banBookItem
    }
}