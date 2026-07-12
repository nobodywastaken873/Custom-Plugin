package me.newburyminer.customItems.systems.ores

import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.ItemRegistry
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class CustomOre(
    val material: Material,
    val drops: (Int) -> List<ItemStack>,
    val experience: IntRange,
) {
    // alphabetical order: down, east, north, south, up, west
    SOULSTONE(
        Material.QUARTZ_BLOCK,
        {listOf(ItemRegistry.get(CustomItem.FRAGMENT_OF_SOUND))},
        10..20
    )

    ;

    companion object {
        fun getFromState(material: Material): CustomOre? {
            return entries.firstOrNull { ore ->
                ore.material == material
            }
        }
    }
}