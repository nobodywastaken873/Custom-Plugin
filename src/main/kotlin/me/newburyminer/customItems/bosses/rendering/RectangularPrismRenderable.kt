package me.newburyminer.customItems.bosses.rendering

import org.bukkit.Material
import org.bukkit.World
import org.bukkit.util.Vector

class RectangularPrismRenderable(

    val transform: Transform,

    var width: Float,
    var height: Float,
    var length: Float,

    var material: Material,
    var thickness: Float = 0.05f

) : Renderable {

    private val quads = List(6) {
        QuadRenderable(
            origin = transform.position,
            center = Vector(),
            normal = Vector(0,1,0),
            up = Vector(0,0,-1),
            width = 1f,
            height = 1f,
            thickness = thickness,
            material = material
        )
    }

    private val front get() = quads[0]
    private val back get() = quads[1]
    private val left get() = quads[2]
    private val rightFace get() = quads[3]
    private val top get() = quads[4]
    private val bottom get() = quads[5]

    override fun spawn(world: World) {
        quads.forEach { it.spawn(world) }
        update()
    }

    override fun update() {

        val (f, u, r) = transform.basis()

        val center = transform.position.clone()
            .add(f.clone().multiply(length / 2.0))

        //----------------------------------------
        // Front
        //----------------------------------------

        front.origin = transform.position
        front.center = center.clone().add(f.clone().multiply(length / 2.0))
        front.normal = f
        front.up = u
        front.width = width
        front.height = height
        front.material = material

        //----------------------------------------
        // Back
        //----------------------------------------

        back.origin = transform.position
        back.center = center.clone().subtract(f.clone().multiply(length / 2.0))
        back.normal = f.clone().multiply(-1)
        back.up = u
        back.width = width
        back.height = height
        back.material = material

        //----------------------------------------
        // Left
        //----------------------------------------

        left.origin = transform.position
        left.center = center.clone().subtract(r.clone().multiply(width / 2.0))
        left.normal = r.clone().multiply(-1)
        left.up = u
        left.width = length
        left.height = height
        left.material = material

        //----------------------------------------
        // Right
        //----------------------------------------

        rightFace.origin = transform.position
        rightFace.center = center.clone().add(r.clone().multiply(width / 2.0))
        rightFace.normal = r
        rightFace.up = u
        rightFace.width = length
        rightFace.height = height
        rightFace.material = material

        //----------------------------------------
        // Top
        //----------------------------------------

        top.origin = transform.position
        top.center = center.clone().add(u.clone().multiply(height / 2.0))
        top.normal = u
        top.up = f.clone().multiply(-1)
        top.width = width
        top.height = length
        top.material = material

        //----------------------------------------
        // Bottom
        //----------------------------------------

        bottom.origin = transform.position
        bottom.center = center.clone().subtract(u.clone().multiply(height / 2.0))
        bottom.normal = u.clone().multiply(-1)
        bottom.up = f
        bottom.width = width
        bottom.height = length
        bottom.material = material

        quads.forEach { it.update() }
    }

    override fun remove() {
        quads.forEach { it.remove() }
    }

    fun scaleWidth(scale: Float) {
        width *= scale
    }

    fun scaleHeight(scale: Float) {
        height *= scale
    }

    fun scaleLength(scale: Float) {
        length *= scale
    }

    data class QuadFace(
        val center: Vector,
        val normal: Vector,
        val up: Vector,
        val width: Float,
        val height: Float,
    )

    fun getFaces(): List<QuadFace> {
        return quads.map {
            QuadFace(it.center, it.normal, it.up, it.width, it.height)
        }
    }

}