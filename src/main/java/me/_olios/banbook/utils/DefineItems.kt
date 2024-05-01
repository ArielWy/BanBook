package me._olios.banbook.utils

import me._olios.banbook.BanBook
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.io.File
import java.io.IOException

class DefineItems(private val player: Player, private val plugin: BanBook, private val configPath: String) {
    private val defineFile: File = File(plugin.dataFolder, "define.yml")
    private val defineConfig: FileConfiguration = YamlConfiguration.loadConfiguration(defineFile)

    init {
        // Load or create the define.yml configuration file
        if (!defineFile.exists())
            plugin.saveResource("define.yml", false)
    }

    fun giveItem() {
        val item = retrieve()
        if (item != null) {
            player.inventory.setItemInMainHand(item)
            player.sendMessage("§agive §2${player.name} §6{§e${item.type}§6}")
        }
        else player.sendMessage("§cThe item is not found in the config file!")
    }

    fun checkForItem() {
        val item: ItemStack = player.inventory.itemInMainHand
        if (item.type == Material.AIR) isCanceled()
        else defineItem(item)
    }

    private fun isCanceled() {
        player.sendMessage("§cYou are not holding anything in your hand!")
    }

    private fun defineItem(item: ItemStack) {
        defineConfig.set(configPath, item)

        try { // try to save the file
            defineConfig.save(defineFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (defineConfig.getItemStack(configPath) == item) {
            player.sendMessage("§aDefine the item successfully as §6{§e${defineConfig.getString(configPath)}§6}")
        } else {
            player.sendMessage("§cThe item is not found in the config file")
        }
    }

    private fun retrieve(): ItemStack? {
        val defineFile = File(plugin.dataFolder, "define.yml")
        val defineConfig: FileConfiguration = YamlConfiguration.loadConfiguration(defineFile)

        // Retrieving the value
        val loadedItemStack: ItemStack? = defineConfig.getItemStack(configPath)
        var configItem: ItemStack? = null
        if (loadedItemStack != null) {
            try {
                configItem = loadedItemStack
            } catch (e: Exception) {
                // Handle any exceptions during deserialization
                e.printStackTrace()
            }
        }
        return configItem
    }
}