package me._olios.banbook.utils

import me._olios.banbook.BanBook
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.io.File
import java.io.IOException

class DefineItem(private val player: Player, private val plugin: BanBook) {
    private val defineFile: File = File(plugin.dataFolder, "define.yml")
    private val defineConfig: FileConfiguration = YamlConfiguration.loadConfiguration(defineFile)


    init {
        // Load or create the define.yml configuration file
        if (!defineFile.exists())
            plugin.saveResource("define.yml", false)
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
        val configPath = "BanBookItem"
        val itemString = item.toString()
        defineConfig.set(configPath, itemString)

        // try to save the file
        try {
            defineConfig.save(defineFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (defineConfig.getString(configPath) == itemString) {
            player.sendMessage("§aDefine the item successfully as §6{§e${defineConfig.getString(configPath)}§6}")
        } else {
            player.sendMessage("§cItem not found in the config!")
        }
    }
}