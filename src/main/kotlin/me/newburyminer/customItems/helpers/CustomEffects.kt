package me.newburyminer.customItems.helpers

import com.destroystokyo.paper.ParticleBuilder
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.rotateToAxis
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class CustomEffects {
    companion object {



        fun particle(particle: ParticleBuilder, loc: Location, count: Int, offset: Double = 0.0, extra: Double = 0.0) {
            particle.clone().count(count).location(loc.clone()).offset(offset, offset, offset).extra(extra).allPlayers().spawn()
            //loc.world.spawnParticle(particle, loc, count, offset, offset, offset, extra)
        }

        fun particleCircle(particle: ParticleBuilder, loc: Location, radius: Double, count: Int, offset: Double = 0.0, extra: Double = 0.0) {
            for (i in 1..count) {
                val rad = Math.random() * Math.PI * 2
                val newLoc = loc.clone().add(Vector(cos(rad)*radius, 0.0, sin(rad)*radius))
                particle.clone().location(newLoc).count(1).offset(offset, offset, offset).extra(extra).allPlayers().spawn()
                //newLoc.world.spawnParticle(particle, newLoc, 1, offset, offset, offset, extra)
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

                val newLoc = loc.clone().add(Vector(newRadius * cos(theta), 0.0, newRadius * sin(theta))
                    .rotateToAxis(centerAxis)
                    .rotateAroundAxis(centerAxis, extraRotation))
                particle.clone().location(newLoc).count(1).offset(offset, offset, offset).extra(extra).allPlayers().spawn()
                //newLoc.world.spawnParticle(particle, newLoc, 1, offset, offset, offset, extra)
            }
        }

        fun rotatedParticleCircle(particle: ParticleBuilder, loc: Location, radius: Double, count: Int, centerAxis: Vector, offset: Double = 0.0, extra: Double = 0.0) {
            for (i in 1..count) {
                //initial phi and theta will create a circle perpendicular to the x-axis
                //phi is offset from xz plane
                //theta is offset from x-axis on the horizontal plane
                val theta = Math.random() * Math.PI * 2

                val newLoc = loc.clone().add(Vector(radius * cos(theta), 0.0, radius * sin(theta)).rotateAroundZ(Math.PI/2).rotateToAxis(centerAxis))
                particle.clone().location(newLoc).count(1).offset(offset, offset, offset).extra(extra).allPlayers().spawn()
                //newLoc.world.spawnParticle(particle, newLoc, 1, offset, offset, offset, extra)
            }
        }

        fun particleSphere(particle: ParticleBuilder, loc: Location, radius: Double, count: Int, offset: Double = 0.0, extra: Double = 0.0) {
            for (i in -(radius*5).toInt()..(radius*5).toInt()) {
                val newRadius = sqrt(radius*radius - (i * 0.2) * (i * 0.2))
                particleCircle(particle, loc.clone().add(Vector(0.0, i*0.2, 0.0)), newRadius, count, offset, extra)
            }
        }

        fun particleLine(particle: ParticleBuilder, startLoc: Location, endLoc: Location, count: Int, offset: Double = 0.0, extra: Double = 0.0) {
            val distBetween = endLoc.clone().subtract(startLoc).toVector().multiply(1.0/count)
            val newStart = startLoc.clone()
            for (i in 1..count) {
                particle.clone().location(newStart.add(distBetween)).count(1).offset(offset, offset, offset).extra(extra).allPlayers().spawn()
                //startLoc.world.spawnParticle(particle, startLoc.add(distBetween.clone().multiply(i)), 1, offset, offset, offset, extra)
            }
        }

        fun particleCloud(particle: ParticleBuilder, loc: Location, count: Int, offset: Double, extra: Double) {
            particle.clone().location(loc.clone()).count(count).offset(offset, offset, offset).extra(extra)
            //loc.world.spawnParticle(particle, loc, count, offset, offset, offset, extra)
        }

        fun playSound(loc: Location, sound: Sound, volume: Float, pitch: Float, random: Boolean = true, soundCategory: SoundCategory = SoundCategory.HOSTILE) {
            if (random) loc.world.playSound(loc, sound, volume, pitch)
            else loc.world.playSound(loc, sound, soundCategory, volume, pitch, 1L)
        }
    }
}