package me._olios.banbook

import org.bukkit.plugin.java.JavaPlugin

class BanBook : JavaPlugin() {
    override fun onEnable() {
        // Plugin startup logic
        saveDefaultConfig()

        registerCommands()
        registerListeners()
    }

    private fun registerCommands() {
        getCommand("banbook")?.setExecutor(BanBookCommand(this))
    }

    private fun registerListeners() {

    }

        override fun onDisable() {
        // Plugin shutdown logic
    }
}