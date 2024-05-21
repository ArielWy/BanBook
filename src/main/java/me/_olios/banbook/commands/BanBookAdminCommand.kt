package me._olios.banbook.commands

import me._olios.banbook.BanBook
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class BanBookAdminCommand(private val plugin: BanBook): CommandExecutor, TabCompleter {

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        val command = p3?.getOrElse(0) { return false }?.lowercase()
        when (command) {
            "lookup" -> {

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
        if (p1.name.equals("banbookadmin", ignoreCase = true)) {
            if (p3!!.size == 1) {
                return mutableListOf("lookup", "revive", "player")
            }
        }
        return null
    }

}