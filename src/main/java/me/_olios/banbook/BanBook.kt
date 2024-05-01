package me._olios.banbook

import me._olios.banbook.listeners.InventoryClickListener
import me._olios.banbook.listeners.InventoryCloseListener
import me._olios.banbook.listeners.PlayerDeathListener
import me._olios.banbook.listeners.PlayerInteractListener
import me._olios.banbook.utils.BanBookCommand
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.java.JavaPlugin
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
    }

    private fun registerCommands() {
        getCommand("banbook")?.setExecutor(BanBookCommand(this))
    }

    private fun registerListeners() {
        Bukkit.getServer().pluginManager.registerEvents(InventoryClickListener(this), this)
        Bukkit.getServer().pluginManager.registerEvents(PlayerDeathListener(this), this)
        Bukkit.getServer().pluginManager.registerEvents(InventoryCloseListener(this), this)
        Bukkit.getServer().pluginManager.registerEvents(PlayerInteractListener(this), this)
    }

        override fun onDisable() {
        // Plugin shutdown logic
    }
}