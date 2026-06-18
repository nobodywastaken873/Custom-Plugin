package me.newburyminer.customItems.helpers

import com.destroystokyo.paper.ParticleBuilder
import org.bukkit.Particle

enum class ParticleTheme(val settings: ParticleSettings) {

    BASIC_THEME(ParticleSettings(ParticleBuilder(Particle.ENCHANTED_HIT), 2))

}