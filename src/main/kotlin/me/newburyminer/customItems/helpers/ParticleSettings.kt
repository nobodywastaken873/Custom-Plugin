package me.newburyminer.customItems.helpers

import com.destroystokyo.paper.ParticleBuilder

data class ParticleSettings(val particle: ParticleBuilder, val particleSeperation: Int, val preParticle: ParticleBuilder = particle, val preParticleSeparation: Int = particleSeperation)