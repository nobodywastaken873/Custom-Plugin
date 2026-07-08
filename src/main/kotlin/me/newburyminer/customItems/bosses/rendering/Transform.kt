package me.newburyminer.customItems.bosses.rendering

import org.bukkit.util.Vector
import org.joml.Quaternionf
import org.joml.Quaternionfc
import org.joml.Vector3f

class Transform(
    var position: Vector = Vector(),
    rotation: Quaternionf = Quaternionf()
) {

    val rotation = Quaternionf(rotation)

    companion object {
        private val LOCAL_FORWARD = Vector3f(0f, 0f, 1f)
        private val LOCAL_UP = Vector3f(0f, 1f, 0f)
        private val LOCAL_RIGHT = Vector3f(1f, 0f, 0f)

        fun lookRotation(
            normal: Vector,
            up: Vector = Vector(0, 1, 0)
        ): Quaternionf {

            val forward = Vector3f(
                normal.x.toFloat(),
                normal.y.toFloat(),
                normal.z.toFloat()
            )

            val upVec = Vector3f(
                up.x.toFloat(),
                up.y.toFloat(),
                up.z.toFloat()
            )

            return Quaternionf()
                .lookAlong(forward.negate(), upVec)
                .invert()
        }
    }

    data class Basis(
        val forward: Vector,
        val up: Vector,
        val right: Vector
    )

    fun basis(): Basis =
        Basis(forward(), up(), right())

    fun forward(): Vector {
        val v = Vector3f(LOCAL_FORWARD)
        rotation.transform(v)
        return Vector(v.x.toDouble(), v.y.toDouble(), v.z.toDouble())
    }

    fun up(): Vector {
        val v = Vector3f(LOCAL_UP)
        rotation.transform(v)
        return Vector(v.x.toDouble(), v.y.toDouble(), v.z.toDouble())
    }

    fun right(): Vector {
        val v = Vector3f(LOCAL_RIGHT)
        rotation.transform(v)
        return Vector(v.x.toDouble(), v.y.toDouble(), v.z.toDouble())
    }

    fun rotate(rotation: Quaternionfc) {
        this.rotation.mul(rotation)
    }

    fun rotateLocalX(angle: Float) {
        rotation.rotateX(angle)
    }

    fun rotateLocalY(angle: Float) {
        rotation.rotateY(angle)
    }

    fun rotateLocalZ(angle: Float) {
        rotation.rotateZ(angle)
    }

    fun rotateWorldX(angle: Float) {
        rotation.premul(
            Quaternionf().rotateX(angle)
        )
    }

    fun rotateWorldY(angle: Float) {
        rotation.premul(
            Quaternionf().rotateY(angle)
        )
    }

    fun rotateWorldZ(angle: Float) {
        rotation.premul(
            Quaternionf().rotateZ(angle)
        )
    }

    fun translate(offset: Vector) {
        position.add(offset)
    }

    fun translate(x: Double, y: Double, z: Double) {
        position.add(Vector(x, y, z))
    }

    fun setRotation(forward: Vector, up: Vector) {
        val q = lookRotation(forward, up)
        rotation.set(q)
    }
}