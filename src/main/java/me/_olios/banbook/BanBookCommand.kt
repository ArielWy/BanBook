package me._olios.banbook

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

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
                return mutableListOf("gui", "reload")
            }
        }
        return null
    }

    private fun reloadPlugin(player: Player) {
        plugin.reloadConfig()
        player.sendMessage("§b[§6BanBook§b] §athe plugin reloaded!")
    }


}