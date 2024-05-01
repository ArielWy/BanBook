package me._olios.banbook.listeners

import me._olios.banbook.BanBook
import me._olios.banbook.GUI.ConfirmGUI
import me._olios.banbook.handler.InteractionHandler
import me._olios.banbook.handler.TargetHandler
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType


class InventoryClickListener(private val plugin: BanBook): Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as Player
        val playerUUID = player.uniqueId
        val item = event.currentItem?: return
        val inventory = event.clickedInventory
        val gui = BanBook.playerInventory[playerUUID]

        if (inventory != gui) return // the player doesn't interact with the gui
        event.isCancelled = true // Prevent people from interacting with their inventory
        skullInteract(player, item)
        confirmInteract(player, item, event)
    }

    private fun skullInteract(player: Player, item: ItemStack) {
        if (item.itemMeta !is SkullMeta) return
        val skullMeta = item.itemMeta as SkullMeta
        val targetedPlayer = skullMeta.owningPlayer as Player

        if (!targetedPlayer.isOnline) return

        ConfirmGUI(player, plugin).confirm(targetedPlayer)
    }

    private fun confirmInteract(player: Player, item: ItemStack, event: InventoryClickEvent) {
        // Check if the player has Data Container
        val key = NamespacedKey(plugin, "target")
        val targetPlayerName = player.persistentDataContainer.get(key, PersistentDataType.STRING)?: return
        val targetPlayer: Player = Bukkit.getServer().getPlayer(targetPlayerName) ?: return
        val displayName = item.itemMeta.displayName

        // When the player cancel the
        if (item.type == Material.RED_STAINED_GLASS_PANE && displayName == "§cDENIED") {
            player.sendMessage("§cDENIED")
            // close everything
            BanBook.playerInventory.remove(player.uniqueId)
            event.clickedInventory?.close()

            // Remove the persistentDataContainer from the player
            player.persistentDataContainer.remove(key)
        }
        else if (item.type == Material.LIME_STAINED_GLASS_PANE && displayName == "§aCONFIRM") {
            player.sendMessage("§aCONFIRM")

            InteractionHandler(player, targetPlayer, plugin).handler() // define target

            // close everything
            BanBook.playerInventory.remove(player.uniqueId)
            event.clickedInventory?.close()

            // Remove the persistentDataContainer from the player
            player.persistentDataContainer.remove(key)
        }

    }
}