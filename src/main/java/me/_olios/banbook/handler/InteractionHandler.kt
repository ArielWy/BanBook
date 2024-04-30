package me._olios.banbook.handler

import me._olios.banbook.BanBook
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class InteractionHandler(private val player: Player, private val target: Player, private val plugin: BanBook) {
        private val config = plugin.config

    fun handler() {
        // alert the players
        targetedPlayerAlert()

        // remove the used items
        removeUsedBanBooks()
    }

    private fun targetedPlayerAlert() {
        // Send Messages if toggled in the config
        var alert: String = config.getString("Messages.TargetedPlayerMessage") ?: return
        val placeholders: Map<String, String> = mapOf(
            "{player}" to player.name,
            "{target}" to target.name
        )

        // Replace placeholders with their corresponding values
        for ((placeholder, value) in placeholders) {
            alert = alert.replace(placeholder, value)
        }

        // if toggled, broadcast to all the players
        if (config.getBoolean("General.TargetedPlayerAlert"))
            Bukkit.broadcast(Component.text(alert))
        else player.sendMessage(Component.text(alert)) // else, send to the player
    }

    private fun removeUsedBanBooks() {
        val key = NamespacedKey(plugin, "isUsed")
        val inventory = player.inventory
        val toRemove = mutableListOf<ItemStack>() // Store items to remove

        for (i in 0 until inventory.size) {
            val itemStack = inventory.getItem(i) ?: continue // Skip null slots, continue loop
            if (!itemStack.hasItemMeta()) continue
            val itemMeta = itemStack.itemMeta
            val value = itemMeta.persistentDataContainer.get(key, PersistentDataType.BOOLEAN) ?: false
            if (value) {
                toRemove.add(itemStack)
            }
        }
        toRemove.reversed().forEach { inventory.removeItem(it) } // remove all the items
    }
}