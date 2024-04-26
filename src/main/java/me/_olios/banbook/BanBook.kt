package me._olios.banbook

import me._olios.banbook.listeners.InventoryClickListener
import me._olios.banbook.listeners.PlayerDeathListener
import me._olios.banbook.utils.BanBookCommand
import me._olios.banbook.utils.DefineItem
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.java.JavaPlugin
import java.time.LocalDateTime
import java.util.*

class BanBook : JavaPlugin() {
    override fun onEnable() {
        // Plugin startup logic
        saveDefaultConfig()

        registerCommands()
        registerListeners()
    }

    companion object {
        val playerInventory: MutableMap<UUID, Inventory> = mutableMapOf()
        val targetedPlayer: MutableMap<UUID, LocalDateTime> = mutableMapOf()
    }

    private fun registerCommands() {
        getCommand("banbook")?.setExecutor(BanBookCommand(this))
    }

    private fun registerListeners() {
        Bukkit.getServer().pluginManager.registerEvents(InventoryClickListener(this), this)
        Bukkit.getServer().pluginManager.registerEvents(PlayerDeathListener(this), this)
    }

        override fun onDisable() {
        // Plugin shutdown logic
    }
}