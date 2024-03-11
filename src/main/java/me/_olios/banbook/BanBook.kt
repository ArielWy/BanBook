package me._olios.banbook

import me._olios.banbook.listeners.InventoryClickListener
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
    }

        override fun onDisable() {
        // Plugin shutdown logic
    }
}