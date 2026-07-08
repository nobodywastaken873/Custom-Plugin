package me.newburyminer.customItems.bosses.rendering.floor

import me.newburyminer.customItems.bosses.rendering.shapes.Shape

interface ShapeMesher {

    fun mesh(shape: Shape): Mesh

}