package me.newburyminer.customItems.bosses.rendering.combinator

import com.destroystokyo.paper.ParticleBuilder
import me.newburyminer.customItems.bosses.rendering.HollowCylinderRenderable
import me.newburyminer.customItems.bosses.rendering.Renderable
import me.newburyminer.customItems.bosses.rendering.Transform
import me.newburyminer.customItems.helpers.CustomEffects
import org.bukkit.Material
import org.bukkit.World

class CylinderCombinator(
    val transform: Transform,

    var radius: Double,
    var length: Double,

    var material: Material,
    var particle: ParticleBuilder,
    var concentration: Double,

    val world: World,
): Renderable {

    private val blockRender = HollowCylinderRenderable(transform, radius, length, material)

    override fun spawn(world: World) {
        blockRender.spawn(world)
    }

    override fun update() {
        blockRender.radius = radius
        blockRender.length = length
        blockRender.material = material
        blockRender.update()

        val direction = transform.forward()
        val origin = transform.position

        CustomEffects.rotatedCylinder(
            particle,
            origin.toLocation(world),
            origin.toLocation(world).add(direction.normalize().multiply(length)),
            radius + 0.1,
            concentration
        )
    }

    override fun remove() {
        blockRender.remove()
    }

}