package me.newburyminer.customItems.bosses.rendering.combinator

import com.destroystokyo.paper.ParticleBuilder
import me.newburyminer.customItems.bosses.rendering.RectangularPrismRenderable
import me.newburyminer.customItems.bosses.rendering.Renderable
import me.newburyminer.customItems.bosses.rendering.Transform
import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Material
import org.bukkit.World

class PrismCombinator(
    val transform: Transform,

    var width: Float,
    var height: Float,
    var length: Float,

    var material: Material,
    var thickness: Float = 0.05f,

    var particle: ParticleBuilder,
    var concentration: Double,

    val world: World
): Renderable {

    private val prism = RectangularPrismRenderable(transform, width, height, length, material, thickness)

    override fun update() {
        prism.width = width
        prism.height = height
        prism.length = length
        prism.material = material
        prism.thickness = thickness

        prism.update()

        prism.getFaces().forEach { (center, normal, up, width1, height1) ->
            CustomEffects.renderParticlePlane(
                particle,
                center.toLocation(world),
                normal,
                up,
                width1.toDouble(),
                height1.toDouble(),
                concentration
            )
        }
    }

    override fun spawn(world: World) {
        prism.spawn(world)
    }

    override fun remove() {
        prism.remove()
    }

}