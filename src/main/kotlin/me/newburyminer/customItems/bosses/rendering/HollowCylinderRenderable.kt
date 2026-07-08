package me.newburyminer.customItems.bosses.rendering

import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.BlockDisplay
import org.bukkit.util.Transformation
import org.bukkit.util.Vector
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.tan

class HollowCylinderRenderable(

    val transform: Transform,

    var radius: Double,
    var length: Double,

    var material: Material

) : DisplayRenderable() {

    private val quads = mutableListOf<QuadRenderable>()
    private val segments = (radius * 14)
        .roundToInt()
        .coerceIn(8, 48)

    override fun spawn(world: World) {

        val center = transform.position.clone()
           .add(transform.forward().normalize().multiply(length / 2.0))

        repeat(segments) {
            val quad = QuadRenderable(
                origin = transform.position,
                center = center.clone(),
                normal = Vector(0, 0, 1),
                up = Vector(0, 1, 0),
                width = 1f,
                height = 1f,
                thickness = 0.05f,
                material = material
            )

            quad.spawn(world)
            quads += quad
        }

        update()
    }

    override fun update() {

        val (axis, up, right) = transform.basis()

        val center = transform.position.clone()
            .add(axis.clone().multiply(length / 2.0))

        val quadWidth =
            (2.0 * radius * tan(Math.PI / segments)).toFloat()

        for ((i, quad) in quads.withIndex()) {

            val angle = 2.0 * Math.PI * i / segments

            val outward =
                right.clone()
                    .multiply(cos(angle))
                    .add(
                        up.clone()
                            .multiply(sin(angle))
                    )
                    .normalize()

            quad.origin = transform.position

            quad.center =
                center.clone()
                    .add(outward.clone().multiply(radius))

            quad.normal = outward
            quad.up = axis

            quad.width = quadWidth
            quad.height = length.toFloat()

            quad.material = material

            quad.update()
        }
    }

    override fun remove() {
        quads.forEach { it.remove() }
        quads.clear()
    }

}