package me._olios.banbook.listeners

import me._olios.banbook.BanBook
import me._olios.banbook.GUI.BanBookGUI
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import java.io.File

class PlayerInteractListener(private val plugin: BanBook): Listener {


    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK && event.action != Action.RIGHT_CLICK_AIR) return
        // define values
        val player = event.player
        val itemStack = player.inventory.itemInMainHand
        val banBookItem: ItemStack? = retrieve("BanBookItem")
        val reviveBookItem: ItemStack? = retrieve("ReviveBookItem")

        when (itemStack) {
            banBookItem -> { // check if the player clicks the banBook item
                BanBookGUI(player, plugin).openGUI(0, true) // Open the playerGUI
                event.isCancelled = true // cancel the event
            }
            reviveBookItem -> {
                BanBookGUI(player, plugin).openGUI(0, false) // Open the playerGUI
                event.isCancelled = true // cancel the event
            }
            else -> return
        }
    }

    private fun retrieve(path: String): ItemStack? {
        val defineFile = File(plugin.dataFolder, "define.yml")
        val defineConfig: FileConfiguration = YamlConfiguration.loadConfiguration(defineFile)

        // Retrieving the value
        val loadedItemStack: ItemStack? = defineConfig.getItemStack(path)
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