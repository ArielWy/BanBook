package me._olios.banbook.listeners

import me._olios.banbook.BanBook
import me._olios.banbook.GUI.PlayerGUI
import org.bukkit.NamespacedKey
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import java.io.File

class PlayerInteractListener(private val plugin: BanBook): Listener {


    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK && event.action != Action.RIGHT_CLICK_AIR) return
        // define values
        val banBookItem: ItemStack? = retrieve()
        val player = event.player
        val itemStack = player.inventory.itemInMainHand

        player.sendMessage("banBookItem: $banBookItem\nitem: $itemStack") // Debug message

        if (itemStack != banBookItem) return // return if the player isn't clicking the banBook item
        event.isCancelled = true // cancel the event

        player.sendMessage("§cTRUE") // Debug message

        PlayerGUI(player, plugin).openGUI(0) // Open the playerGUI
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