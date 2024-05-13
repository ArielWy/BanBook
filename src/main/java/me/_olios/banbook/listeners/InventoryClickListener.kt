package me._olios.banbook.listeners

import me._olios.banbook.BanBook
import me._olios.banbook.GUI.ConfirmGUI
import me._olios.banbook.handler.InteractionHandler
import me._olios.banbook.handler.TargetHandler
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import java.util.*

class InventoryClickListener(private val plugin: BanBook): Listener {
    private val config = plugin.config

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as Player
        val playerUUID = player.uniqueId
        val item = event.currentItem?: return
        val inventory = event.clickedInventory
        val gui = BanBook.playerInventory[playerUUID]

        if (inventory != gui) return  // the player doesn't interact with the gui
        event.isCancelled = true  // Prevent people from interacting with their inventory
        skullInteract(player, item)
        confirmInteract(player, item, event)
    }

    private fun skullInteract(player: Player, item: ItemStack) {
        player.sendMessage("skull interact")
        if (item.itemMeta !is SkullMeta) return  // return if it's not skull interact in the GUI
        val skullMeta = item.itemMeta as SkullMeta
        val targetedPlayerUUID: UUID = skullMeta.owningPlayer?.uniqueId ?: UUID.randomUUID()
        player.sendMessage("target UUID: $targetedPlayerUUID")

        // Check if online before casting
        val targetedPlayer = Bukkit.getOfflinePlayer(targetedPlayerUUID)
        player.sendMessage("try target: $targetedPlayer")
        if (targetedPlayer.hasPlayedBefore()) {  // Player exist
            player.sendMessage("target: ${targetedPlayer.name}")
            if (targetedPlayer.isBanned && !targetedPlayer.isOnline) {
                player.sendMessage("§9revive!")
                TargetHandler(player, plugin).revivePlayer(targetedPlayer)
            }
            else{  // player currently online
                val onlinePlayer: Player = Bukkit.getPlayer(targetedPlayerUUID)!!
                ConfirmGUI(player, plugin).confirm(onlinePlayer)
            }
        }
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

            InteractionHandler(player, targetPlayer, plugin).handler()  // define target

            // close everything
            BanBook.playerInventory.remove(player.uniqueId)
            event.clickedInventory?.close()

            // Remove the persistentDataContainer from the player
            player.persistentDataContainer.remove(key)
        }

    }
}