package me._olios.banbook.utils

import me._olios.banbook.BanBook
import me._olios.banbook.GUI.PlayerGUI
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File

class BanBookCommand(private val plugin: BanBook): CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if (sender !is Player) return false
        when {
            p3!!.isEmpty() -> {
                return false
            }
            p3[0].equals("gui", ignoreCase = true) -> {
                PlayerGUI(sender, plugin).openGUI(0)
                return true
            }
            p3[0].equals("define", ignoreCase = true) -> {
                DefineItem(sender, plugin).checkForItem()
                return true
            }
            p3[0].equals("reload", ignoreCase = true) -> {
                reloadPlugin(sender)
                return true
            }
        }
        return false
    }

    override fun onTabComplete(
        p0: CommandSender,
        p1: Command,
        p2: String,
        p3: Array<out String>?
    ): MutableList<String>? {
        if (p1.name.equals("banbook", ignoreCase = true)) {
            if (p3!!.size == 1) {
                return mutableListOf("gui", "define", "reload")
            }
        }
        return null
    }

    private fun reloadPlugin(player: Player) {
        try {
            plugin.reloadConfig()
            val defineFile = File(plugin.dataFolder, "define.yml")
            YamlConfiguration.loadConfiguration(defineFile)
            player.sendMessage("§b[§6BanBook§b] §athe plugin has been reloaded!")
        } catch (exception: Exception) {
            // Log the error
            plugin.logger.severe("Failed to reload configuration: ${exception.message}")

            // Send a message to the player
            player.sendMessage("§b[§6BanBook§b] §cThe plugin failed to reload. Please check the server logs for more details.")

            // Reload the original file
            plugin.saveDefaultConfig()
            plugin.reloadConfig()
            player.sendMessage("§b[§6BanBook§b] §aThe original configuration has been reloaded.")
        }
    }



}