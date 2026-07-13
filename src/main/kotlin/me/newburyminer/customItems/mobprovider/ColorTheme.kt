package me.newburyminer.customItems.mobprovider

import org.bukkit.inventory.meta.trim.TrimMaterial

enum class ColorTheme(val color: Array<Int>, val trimMaterial: TrimMaterial) {

    BLACKSTONE(arrayOf(40, 32, 48), TrimMaterial.DIAMOND),
    WARDEN(arrayOf(15, 74, 82), TrimMaterial.QUARTZ),
    CAVES(arrayOf(111, 117, 117), TrimMaterial.NETHERITE),
    COLD_OCEAN(arrayOf(47, 64, 158), TrimMaterial.COPPER),
    DESERT(arrayOf(181, 160, 103), TrimMaterial.GOLD),
    MILITARY(arrayOf(56, 102, 52), TrimMaterial.NETHERITE),
    MYSTIC(arrayOf(89, 70, 46), TrimMaterial.EMERALD),
    ROCKY(arrayOf(80, 82, 89), TrimMaterial.QUARTZ),
    SURFACE(arrayOf(125, 122, 109), TrimMaterial.NETHERITE),
    WARM_OCEAN(arrayOf(49, 159, 181), TrimMaterial.GOLD)

}