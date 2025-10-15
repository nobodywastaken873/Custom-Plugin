package me.newburyminer.customItems.systems

import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.smelt
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomEnchantments
import me.newburyminer.customItems.items.CustomItem
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class EnchantmentListener: Listener {
    @EventHandler fun onBlockDropItems(e: BlockDropItemEvent) {
        autoSmelt(e)
    }
    private fun autoSmelt(e: BlockDropItemEvent) {
        if (CustomEnchantments.AUTOSMELT !in e.player.inventory.itemInMainHand.enchantments) return
        for (drop in e.items) {
            drop.itemStack.smelt()
            if (drop.itemStack.type == Material.RAW_GOLD_BLOCK) drop.itemStack.type = Material.GOLD_BLOCK
            if (drop.itemStack.type == Material.RAW_IRON_BLOCK) drop.itemStack.type = Material.IRON_BLOCK
            if (drop.itemStack.type == Material.RAW_COPPER_BLOCK) drop.itemStack.type = Material.COPPER_BLOCK
        }
        CustomEffects.particle(Particle.FLAME.builder(), e.block.location.add(Vector(0.5, 0.5, 0.5)), 10, 0.5)
    }

    @EventHandler fun onInteract(e: PlayerInteractEvent) {
        ancientTome(e)
    }
    private fun ancientTome(e: PlayerInteractEvent) {
        var tome: ItemStack? = null
        for (custom in arrayOf<CustomItem>()) if (e.player.inventory.itemInOffHand.isItem(custom)) tome = e.player.inventory.itemInOffHand
        if (tome == null) return
        val tomeEnchant = tome.enchantments.entries.random()
        val enchantable = e.player.inventory.itemInMainHand
        if (enchantable.enchantments[tomeEnchant.key] != tomeEnchant.value-1) return
        e.isCancelled = true
        enchantable.removeEnchantment(tomeEnchant.key)
        enchantable.addUnsafeEnchantment(tomeEnchant.key, tomeEnchant.value)
        tome.amount -= 1
        CustomEffects.playSound(e.player.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.1F)
    }


}