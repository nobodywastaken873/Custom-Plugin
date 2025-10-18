package me.newburyminer.customItems.systems.materials

import me.newburyminer.customItems.Utils
import net.kyori.adventure.text.Component

enum class MaterialCategory(val title: Component) {
    REDSTONE(Utils.text("Redstone Materials", arrayOf(130, 41, 33))),
    BUILDING(Utils.text("Building Materials", arrayOf(163, 119, 91))),
}