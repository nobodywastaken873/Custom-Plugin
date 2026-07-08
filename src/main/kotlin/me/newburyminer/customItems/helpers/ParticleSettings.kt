package me.newburyminer.customItems.helpers

import com.destroystokyo.paper.ParticleBuilder
import org.bukkit.Particle

data class ParticleSettings(
    val particle: ParticleBuilder,
    val particleSeperation: Int,
    val preParticle: ParticleBuilder = particle,
    val preParticleSeparation: Int = particleSeperation,
    val concentration: Double = 5.0,
    val preConcentration: Double = 5.0,
    val spread: Double = 1.0,
)