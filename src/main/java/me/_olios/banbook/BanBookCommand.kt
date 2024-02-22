package me._olios.banbook

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class BanBookCommand: CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if (sender !is Player) return false
        when {
            p3!!.isEmpty() -> {
                return false
            }
            p3[0].equals("give", ignoreCase = true) -> {
                sender.sendMessage("GIVE BAN BOOK")
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
                return mutableListOf("give")
            }
        }
        return null
    }


}