package me.newburyminer.customItems.bosses.rendering.floor

import org.bukkit.Material
import org.bukkit.util.Vector

data class FloorTile(

    var center: Vector,

    var normal: Vector,

    var up: Vector,

    var width: Double,

    var height: Double,

    var material: Material

) {

    val area = width * height

}