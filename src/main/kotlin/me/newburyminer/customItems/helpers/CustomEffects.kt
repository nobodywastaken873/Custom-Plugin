package me.newburyminer.customItems.helpers

import com.destroystokyo.paper.ParticleBuilder
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.rotateToAxis
import me.newburyminer.customItems.bosses.rendering.shapes.Shape
import org.bukkit.FluidCollisionMode
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class CustomEffects {
    companion object {

        fun particleFullShape(world: World, particle: ParticleBuilder, shape: Shape, concentration: Double) {
            val receivers = shape.center.toLocation(world).getNearbyPlayers(60.0).toList()
            particleShape(world, particle, shape, concentration, receivers)
            particleShapeOutline(world, particle, shape, concentration, receivers)
        }

        fun particleShape(world: World, particle: ParticleBuilder, shape: Shape, concentration: Double, preReceivers: List<Player>? = null) {
            val receivers = preReceivers ?: shape.center.toLocation(world).getNearbyPlayers(60.0).toList()
            for (i in 0..(concentration * shape.area).toInt()) {
                particle(particle, shape.randomPoint().toLocation(world), 1, receivers = receivers)
            }
        }

        fun particleShapeOutline(world: World, particle: ParticleBuilder, shape: Shape, concentration: Double, preReceivers: List<Player>? = null) {
            val receivers = preReceivers ?: shape.center.toLocation(world).getNearbyPlayers(60.0).toList()
            for (point in shape.linePoints(2 * sqrt(concentration))) {
                particle(particle, point.toLocation(world), 1, receivers = receivers)
            }
        }

        fun particleBox(particle: ParticleBuilder, corner1: Location, corner2: Location, count: Int, receivers: List<Player>? = null) {
            val calcedReceivers = receivers ?: corner1.getNearbyPlayers(60.0)
            val center = corner1.toVector().add(corner2.toVector()).multiply(0.5)
            val size = corner1.toVector().subtract(corner2.toVector())
            particle.clone()
                .count(count)
                .location(center.toLocation(corner1.world))
                .offset(size.x / 2.0, size.y / 2.0, size.z / 2.0)
                .receivers(calcedReceivers)
                .spawn()
        }

        fun particle(particle: ParticleBuilder, loc: Location, count: Int, offset: Double = 0.0, extra: Double = 0.0, receivers: List<Player>? = null) {
            val calcedReceivers = receivers ?: loc.getNearbyPlayers(60.0)
            particle.clone()
                .count(count)
                .location(loc.clone())
                .offset(offset, offset, offset)
                .extra(extra)
                .receivers(calcedReceivers)
                .spawn()

            //loc.world.spawnParticle(particle, loc, count, offset, offset, offset, extra)
        }

        fun particleCircle(particle: ParticleBuilder, loc: Location, radius: Double, count: Int, offset: Double = 0.0, extra: Double = 0.0) {
            val receivers = loc.getNearbyPlayers(60.0)
            for (i in 1..count) {
                val rad = Math.random() * Math.PI * 2
                val newLoc = loc.clone().add(Vector(cos(rad) * radius, 0.0, sin(rad) * radius))
                particle.clone()
                    .location(newLoc)
                    .count(1)
                    .offset(offset, offset, offset)
                    .extra(extra)
                    .receivers(receivers)
                    .spawn()
                //newLoc.world.spawnParticle(particle, newLoc, 1, offset, offset, offset, extra)
            }
        }

        fun particleCircle(particle: ParticleBuilder, loc: Location, radius: Double, concentration: Double, offset: Double = 0.0, extra: Double = 0.0) {
            val count = 2 * Math.PI * radius * concentration
            particleCircle(particle, loc, radius, count.toInt(), offset, extra)
        }

        fun filledParticleCircle(particle: ParticleBuilder, loc: Location, radius: Double, concentration: Double, offset: Double = 0.0, extra: Double = 0.0) {
            val receivers = loc.getNearbyPlayers(60.0)
            for (i in 0..(radius.pow(2)*Math.PI*concentration).toInt()) {
                val theta = Math.random() * 2 * Math.PI
                val r = sqrt(Math.random()) * radius
                val newLoc = loc.clone().add(r * cos(theta), 0.0, r * sin(theta))
                particle.clone()
                    .location(newLoc)
                    .count(1)
                    .offset(offset, offset, offset)
                    .extra(extra)
                    .receivers(receivers)
                    .spawn()
            }
        }

        fun rotatedArc(particle: ParticleBuilder, loc: Location, radius: Double, totalAngleSpread: Double, count: Int, centerAxis: Vector, offset: Double = 0.0, extra: Double = 0.0) {
            val receivers = loc.getNearbyPlayers(60.0)
            for (i in 1..count) {
                //initial phi and theta will create a circle perpendicular to the x-axis
                //phi is offset from xz plane
                //theta is offset from x-axis on the horizontal plane
                val radians = totalAngleSpread / 2
                val theta = Utils.randomRange(-radians, radians)

                //phi += phiOffset
                //theta += thetaOffset
                val newRadius = sqrt(Math.random()) * radius

                //val baseVec = Vector(newRadius * cos(theta), 0.0, newRadius * sin(theta))
                //baseVec.checkFinite()
                //val rotated1 = baseVec.rotateToAxis(centerAxis)
                //rotated1.checkFinite()

                val newLoc = loc.clone().add(
                    Vector(newRadius * cos(theta), 0.0, newRadius * sin(theta))
                        .rotateToAxis(centerAxis)
                        //.rotateAroundAxis(centerAxis, extraRotation)
                )

                particle.clone()
                    .location(newLoc)
                    .count(1)
                    .offset(offset, offset, offset)
                    .extra(extra)
                    .receivers(receivers)
                    .spawn()
                //newLoc.world.spawnParticle(particle, newLoc, 1, offset, offset, offset, extra)
            }
        }

        fun rotatedArc(particle: ParticleBuilder, loc: Location, radius: Double, totalAngleSpread: Double, concentration: Double, centerAxis: Vector, offset: Double = 0.0, extra: Double = 0.0) {
            val count = radius.pow(2) * Math.PI * (totalAngleSpread / (2 * Math.PI)) * concentration * 4.0
            rotatedArc(particle, loc, radius, totalAngleSpread, count.toInt(), centerAxis, offset, extra)
        }

        fun rotatedCylinder(particle: ParticleBuilder, start: Location, end: Location, radius: Double, concentration: Double) {
            val receivers = midpoint(start, end).getNearbyPlayers(60.0).toList()
            val direction = end.clone().subtract(start)

            val newLoc = start.clone()
            val circleCount = (concentration * direction.length()).toInt()

            val separation = 1.0 / concentration
            val unit = direction.toVector().normalize().multiply(separation)

            for (i in 0..circleCount) {
                rotatedParticleCircle(particle, newLoc.clone(), radius, (6.28 * radius * concentration).toInt(), unit, preReceivers = receivers)
                newLoc.add(unit)
            }

        }

        fun rotatedParticleCircle(particle: ParticleBuilder, loc: Location, radius: Double, count: Int, centerAxis: Vector, offset: Double = 0.0, extra: Double = 0.0, preReceivers: List<Player>? = null) {
            val receivers = preReceivers ?: loc.getNearbyPlayers(60.0)
            for (i in 1..count) {
                //initial phi and theta will create a circle perpendicular to the x-axis
                //phi is offset from xz plane
                //theta is offset from x-axis on the horizontal plane
                val theta = Math.random() * Math.PI * 2

                val newLoc = loc.clone().add(
                    Vector(radius * cos(theta), 0.0, radius * sin(theta)).rotateAroundZ(Math.PI / 2)
                        .rotateToAxis(centerAxis)
                )
                particle.clone()
                    .location(newLoc)
                    .count(1)
                    .offset(offset, offset, offset)
                    .extra(extra)
                    .receivers(receivers)
                    .spawn()
                //newLoc.world.spawnParticle(particle, newLoc, 1, offset, offset, offset, extra)
            }

        }

        fun renderParticlePlane(
            particle: ParticleBuilder, center: Location, normal: Vector, up: Vector, width: Double, height: Double, concentration: Double = 4.0
        ) {

            val receivers = center.getNearbyPlayers(60.0)

            val n = normal.clone().normalize()
            val u = up.clone().normalize()

            val right = u.clone().crossProduct(n).normalize()
            val correctedUp = n.clone().crossProduct(right).normalize()

            val origin = center.clone()
                .subtract(right.clone().multiply(width / 2))
                .subtract(correctedUp.clone().multiply(height / 2))

            val count = (width * height * concentration).toInt()

            repeat(count) {

                val x = Math.random() * width
                val y = Math.random() * height

                val point = origin.clone()
                    .add(right.clone().multiply(x))
                    .add(correctedUp.clone().multiply(y))

                particle.clone()
                    .location(point)
                    .count(1)
                    .receivers(receivers)
                    .spawn()
            }
        }

        fun particleSphere(particle: ParticleBuilder, loc: Location, radius: Double, concentration: Double, offset: Double = 0.0, extra: Double = 0.0) {
            val receivers = loc.getNearbyPlayers(60.0)
            val surfaceArea = (4*Math.PI*radius.pow(2) * concentration).toInt()
            for (i in 0..surfaceArea) {

                val theta = Math.random() * Math.PI * 2
                val phi = Math.random() * Math.PI * 2

                val normalVector = Vector(cos(phi) * sin(theta), sin(phi) * sin(theta), cos(theta)).multiply(radius)
                val newLoc = loc.clone().add(normalVector)
                particle.clone()
                    .location(newLoc)
                    .count(1)
                    .offset(offset, offset, offset)
                    .extra(extra)
                    .receivers(receivers)
                    .spawn()
            }
        }

        fun raycastParticleLine(particle: ParticleBuilder, loc: Location, direction: Vector, distance: Double,
                                density: Double, collideEntity: Boolean = false, predicate: (Entity) -> Boolean = { true },
                                offset: Double = 0.0, extra: Double = 0.0) {
            val result =
                (if (collideEntity) {
                    loc.world.rayTrace(loc, direction, distance, FluidCollisionMode.NEVER, true, 0.1, predicate)
                } else {
                    loc.world.rayTraceBlocks(loc, direction, distance, FluidCollisionMode.NEVER, true,)
                })?.hitPosition ?: loc.clone().add(direction.normalize().multiply(distance)).toVector()

            val endLoc = result.toLocation(loc.world)
            particleLine(particle, loc, endLoc, density, offset, extra)

        }
        fun particleLine(particle: ParticleBuilder, startLoc: Location, endLoc: Location, density: Double, offset: Double = 0.0, extra: Double = 0.0) {

            val receivers = (
                startLoc.getNearbyPlayers(60.0) +
                endLoc.getNearbyPlayers(60.0) +
                midpoint(startLoc, endLoc).getNearbyPlayers(60.0)
            ).toSet()

            val totalDistance = endLoc.clone().subtract(startLoc)
            val newCount = (totalDistance.length() * density).toInt()
            val distBetween = totalDistance.clone().toVector().multiply(1.0 / newCount)

            val newStart = startLoc.clone()

            for (i in 1..newCount) {
                particle.clone()
                    .location(newStart.clone().add(distBetween.clone().multiply(i)))
                    .count(1)
                    .offset(offset, offset, offset)
                    .extra(extra)
                    .receivers(receivers)
                    .spawn()
                //startLoc.world.spawnParticle(particle, startLoc.add(distBetween.clone().multiply(i)), 1, offset, offset, offset, extra)
            }

        }

        fun particleCloud(particle: ParticleBuilder, loc: Location, count: Int, offset: Double, extra: Double) {

            val receivers = loc.getNearbyPlayers(60.0)

            particle.clone()
                .location(loc.clone())
                .count(count)
                .offset(offset, offset, offset)
                .extra(extra)
                .receivers(receivers)
                .spawn()
            //loc.world.spawnParticle(particle, loc, count, offset, offset, offset, extra)
        }

        fun playSound(loc: Location, sound: Sound, volume: Float, pitch: DoubleRange, random: Boolean = true, soundCategory: SoundCategory = SoundCategory.HOSTILE) {
            playSound(loc, sound, volume, pitch.random().toFloat(), random, soundCategory)
        }
        fun playSound(loc: Location, sound: Sound, volume: Float, pitch: Float, random: Boolean = true, soundCategory: SoundCategory = SoundCategory.HOSTILE) {
            if (random) loc.world.playSound(loc, sound, volume, pitch)
            else loc.world.playSound(loc, sound, soundCategory, volume, pitch, 1L)
        }
        fun playSoundToPlayer(player: Player, sound: Sound, volume: Float, pitch: Float, randomAmount: Float = 0.02F) {
            player.playSound(player.location, sound, volume, (pitch + randomAmount * (2 * Math.random() - 1)).toFloat())
        }

        private fun midpoint(start: Location, end: Location): Location {
            return start.clone().add(end.clone().subtract(start).toVector().multiply(0.5))
        }
    }

}