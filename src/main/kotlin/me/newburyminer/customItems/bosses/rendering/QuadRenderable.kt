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

    private lateinit var display: BlockDisplay

    override fun spawn(world: World) {

        display = world.spawn(
            origin.toLocation(world),
            BlockDisplay::class.java
        )

        display.block = material.createBlockData()

        val duration = if (smooth) 1 else 0

        display.interpolationDelay = 0
        display.interpolationDuration = duration
        display.teleportDuration = duration

        displays += display

        update()
        //display.teleport(center.toLocation(display.world))
    }

    override fun update() {

        val n = normal.clone().normalize()
        val u = up.clone().normalize()

        val right = u.clone().crossProduct(n).normalize()
        val correctedUp = n.clone().crossProduct(right).normalize()

        val spawnPos = center.clone()
            .subtract(right.clone().multiply(width / 2.0))
            .subtract(correctedUp.clone().multiply(height / 2.0))
            .subtract(n.clone().multiply(thickness / 2.0))

        val offset = spawnPos.subtract(origin)

        if (display.block.material != material) display.block = material.createBlockData()

        //val t = display.transformation

        //t.leftRotation.set(lookRotation(normal, correctedUp))
        //t.scale.set(Vector3f(width, height, thickness))
        //t.translation.set(Vector3f())
        //t.rightRotation.set(Quaternionf())

        display.transformation = Transformation(
            offset.toVector3f(),
            Transform.lookRotation(normal, correctedUp),
            Vector3f(width, height, thickness),
            Quaternionf()
        )

        display.teleport(origin.toLocation(display.world))
    }

    fun activate() {
        // do something
    }
    private val hiddenLocation: Vector = Vector(0, -1024, 0)
    fun deactivate() {
        display.transformation = Transformation(
            hiddenLocation.toVector3f(),
            Quaternionf(),
            Vector3f(),
            Quaternionf()
        )
    }

}