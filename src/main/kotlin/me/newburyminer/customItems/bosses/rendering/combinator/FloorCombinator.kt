package me.newburyminer.customItems.bosses.rendering.combinator

import com.destroystokyo.paper.ParticleBuilder
import me.newburyminer.customItems.bosses.rendering.Renderable
import me.newburyminer.customItems.bosses.rendering.floor.FloorPatternRenderer
import me.newburyminer.customItems.bosses.rendering.floor.GreedyShapeMesher
import me.newburyminer.customItems.bosses.rendering.shapes.Shape
import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Material
import org.bukkit.World

class FloorCombinator(
    val shape: Shape,
    cellSize: Double,
    var material: Material,
    var particle: ParticleBuilder,
    var concentration: Double,
    val world: World,
): Renderable {

    private val mesher = GreedyShapeMesher(cellSize, material)
    private val renderer = FloorPatternRenderer(
        mesher,
        shape,
        world
    )

    override fun spawn(world: World) {
        renderer.spawn(world)
    }

    override fun update() {
        mesher.material = material
        renderer.update()

        CustomEffects.particleShape(world, particle, shape, concentration)
    }

    override fun remove() {
        renderer.remove()
    }

}