package me._olios.banbook.listeners

import me._olios.banbook.BanBook
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import java.io.File

class PlayerInteractListener(private val plugin: BanBook): Listener {


    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
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

        val player = event.player
        val item = player.inventory.itemInMainHand

        player.sendMessage("banBookItem: $banBookItem\nitem: $item")

        if (item == banBookItem)
            player.sendMessage("§cTRUE")
    }
}