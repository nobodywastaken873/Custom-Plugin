package me.newburyminer.customItems.systems.ores

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.eventbus.EventRegistry
import me.newburyminer.customItems.eventbus.ListenerEntry
import org.bukkit.Location
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.block.BlockBreakEvent

object OreSystemManager {

    fun registerEvents() {

        EventRegistry.register(ListenerEntry(BlockBreakEvent::class,
            { e ->
                e.block.world == CustomItems.aridWorld
            },
            {e ->
                val customOre = CustomOre.getFromState(e.block.type) ?: return@ListenerEntry
                e.isDropItems = false
                //e.player.addItemorDrop(ItemStack(Material.DIAMOND_BLOCK))
                val fortune = e.player.equipment.itemInMainHand.getEnchantmentLevel(Enchantment.FORTUNE)
                customOre.drops(fortune).forEach {
                    e.block.world.dropItemNaturally(
                        selectDropLocation(e.block.location),
                        it
                    )
                }
                e.expToDrop = customOre.experience.random()
            })
        )

    }

    private fun selectDropLocation(blockLocation: Location): Location {
        val center = blockLocation.clone().add(0.5, 0.5, 0.5)
        center.add(Math.random() * 0.45 - 0.225, Math.random() * 0.45 - 0.225, Math.random() * 0.45 - 0.225)
        return center
    }

}