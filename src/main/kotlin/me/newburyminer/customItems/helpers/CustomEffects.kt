package me.newburyminer.customItems.helpers

import com.destroystokyo.paper.ParticleBuilder
import com.google.common.base.Predicate
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.rotateToAxis
import org.bukkit.FluidCollisionMode
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class CustomEffects {
    companion object {



        fun particle(particle: ParticleBuilder, loc: Location, count: Int, offset: Double = 0.0, extra: Double = 0.0) {
            particle.clone()
                .count(count)
                .location(loc.clone())
                .offset(offset, offset, offset)
                .extra(extra)
                .receivers(60)
                .spawn()

            //loc.world.spawnParticle(particle, loc, count, offset, offset, offset, extra)
        }

        fun particleCircle(particle: ParticleBuilder, loc: Location, radius: Double, count: Int, offset: Double = 0.0, extra: Double = 0.0) {

            for (i in 1..count) {
                val rad = Math.random() * Math.PI * 2
                val newLoc = loc.clone().add(Vector(cos(rad) * radius, 0.0, sin(rad) * radius))
                particle.clone()
                    .location(newLoc)
                    .count(1)
                    .offset(offset, offset, offset)
                    .extra(extra)
                    .receivers(60)
                    .spawn()
                //newLoc.world.spawnParticle(particle, newLoc, 1, offset, offset, offset, extra)
            }
        }

        fun filledParticleCircle(particle: ParticleBuilder, loc: Location, radius: Double, concentration: Double, offset: Double = 0.0, extra: Double = 0.0) {
            for (i in 0..(radius.pow(2)*Math.PI*concentration).toInt()) {
                val theta = Math.random() * 2 * Math.PI
                val r = sqrt(Math.random()) * radius
                val newLoc = loc.clone().add(r * cos(theta), 0.0, r * sin(theta))
                particle.clone()
                    .location(newLoc)
                    .count(1)
                    .offset(offset, offset, offset)
                    .extra(extra)
                    .receivers(60)
                    .spawn()
            }
        }

        fun rotatedArc(particle: ParticleBuilder, loc: Location, radius: Double, totalAngleSpread: Double, count: Int, centerAxis: Vector, extraRotation: Double  = 0.0, offset: Double = 0.0, extra: Double = 0.0) {

            for (i in 1..count) {
                //initial phi and theta will create a circle perpendicular to the x-axis
                //phi is offset from xz plane
                //theta is offset from x-axis on the horizontal plane
                val radians = Math.toRadians(totalAngleSpread) / 2
                val theta = Utils.randomRange(-radians, radians)

                //phi += phiOffset
                //theta += thetaOffset
                val newRadius = sqrt(Math.random()) * radius

                val newLoc = loc.clone().add(
                    Vector(newRadius * cos(theta), 0.0, newRadius * sin(theta))
                        .rotateToAxis(centerAxis)
                        .rotateAroundAxis(centerAxis, extraRotation)
                )
                particle.clone()
                    .location(newLoc)
                    .count(1)
                    .offset(offset, offset, offset)
                    .extra(extra)
                    .receivers(60)
                    .spawn()
                //newLoc.world.spawnParticle(particle, newLoc, 1, offset, offset, offset, extra)
            }
        }

        fun rotatedParticleCircle(particle: ParticleBuilder, loc: Location, radius: Double, count: Int, centerAxis: Vector, offset: Double = 0.0, extra: Double = 0.0) {
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
                    .receivers(60)
                    .spawn()
                //newLoc.world.spawnParticle(particle, newLoc, 1, offset, offset, offset, extra)
            }

        }

        fun particleSphere(particle: ParticleBuilder, loc: Location, radius: Double, concentration: Double, offset: Double = 0.0, extra: Double = 0.0) {

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
                    .receivers(60)
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
                    .receivers(60)
                    .spawn()
                //startLoc.world.spawnParticle(particle, startLoc.add(distBetween.clone().multiply(i)), 1, offset, offset, offset, extra)
            }

        }

        fun particleCloud(particle: ParticleBuilder, loc: Location, count: Int, offset: Double, extra: Double) {

            particle.clone()
                .location(loc.clone())
                .count(count)
                .offset(offset, offset, offset)
                .extra(extra)
                .receivers(60)
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
    }
}