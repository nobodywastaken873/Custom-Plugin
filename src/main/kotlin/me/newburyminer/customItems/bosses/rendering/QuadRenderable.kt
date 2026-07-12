package me.newburyminer.customItems.bosses.rendering

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.BlockDisplay
import org.bukkit.util.Transformation
import org.bukkit.util.Vector
import org.joml.Quaternionf
import org.joml.Vector3f

class QuadRenderable(
    var origin: Vector,
    var center: Vector,
    var normal: Vector,
    var up: Vector,
    var width: Float,
    var height: Float,
    var thickness: Float,
    var material: Material,
    val smooth: Boolean = true
) : DisplayRenderable() {

    private lateinit var display: DisplayWrapper

    override fun spawn(world: World) {

        display = DisplayWrapper(world.spawn(
            origin.toLocation(world),
            BlockDisplay::class.java
        ))

        display.block?.block = material.createBlockData()

        val duration = if (smooth) 1 else 0

        display.block?.interpolationDelay = 0
        display.block?.interpolationDuration = duration
        display.block?.teleportDuration = duration

        displays += display

        update()
        //display.teleport(center.toLocation(display.world))
    }

    override fun update() {

        if (display.block == null) return

        val n = normal.clone().normalize()
        val u = up.clone().normalize()

        val right = u.clone().crossProduct(n).normalize()
        val correctedUp = n.clone().crossProduct(right).normalize()

        val spawnPos = center.clone()
            .subtract(right.clone().multiply(width / 2.0))
            .subtract(correctedUp.clone().multiply(height / 2.0))
            .subtract(n.clone().multiply(thickness / 2.0))

        val offset = spawnPos.subtract(origin)

        if (display.block?.block?.material != material) display.block?.block = material.createBlockData()

        //val t = display.transformation

        //t.leftRotation.set(lookRotation(normal, correctedUp))
        //t.scale.set(Vector3f(width, height, thickness))
        //t.translation.set(Vector3f())
        //t.rightRotation.set(Quaternionf())

        display.block?.transformation = Transformation(
            offset.toVector3f(),
            Transform.lookRotation(normal, correctedUp),
            Vector3f(width, height, thickness),
            Quaternionf()
        )

        display.block?.teleport(origin.toLocation(display.block?.world ?: return))
    }

    fun activate() {
        // do something
    }
    private val hiddenLocation: Vector = Vector(0, -1024, 0)
    fun deactivate() {
        display.block?.transformation = Transformation(
            hiddenLocation.toVector3f(),
            Quaternionf(),
            Vector3f(),
            Quaternionf()
        )
    }

}