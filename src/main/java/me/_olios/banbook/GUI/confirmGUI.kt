package me._olios.banbook.GUI

import me._olios.banbook.BanBook
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Skull
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class confirmGUI(private val player: Player, private val plugin: BanBook) {

    fun confirm(targetedPlayer: Player, skull: ItemStack) {
        val inventory = Bukkit.createInventory(null, 9, "Confirm")
        BanBook.playerInventory.getOrPut(player.uniqueId) { inventory }

        inventory.setItem(4, skull)
        for (i in 0..3) inventory.setItem(i, PlayerGUI(player, plugin).createItemStack(Material.RED_STAINED_GLASS_PANE, "§CONFIRM"))
        for (i in 5..8) inventory.setItem(i, PlayerGUI(player, plugin).createItemStack(Material.LIME_STAINED_GLASS_PANE, "§cDENIED"))

    }
}