package me._olios.banbook.utils

import me._olios.banbook.BanBook
import org.bukkit.BanList
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class UnbanCommand(private val plugin: BanBook) : CommandExecutor {
    private val config = plugin.config
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.size != 1) {
            sender.sendMessage("Usage: /unban <player>")
            return false
        }

        val playerName = args[0]
        val offlinePlayer: OfflinePlayer = Bukkit.getOfflinePlayer(playerName)

        unbanPlayer(offlinePlayer)
        return true
    }

    private fun unbanPlayer(player: OfflinePlayer) {
        val banList: BanList<Player> = Bukkit.getBanList(BanList.Type.PROFILE)
        banList.pardon(player.name.toString())
        if (config.getBoolean("General.TargetedPlayerReviveAlert"))
            Bukkit.getServer().broadcastMessage("${player.name} has been unbanned!")
    }
}