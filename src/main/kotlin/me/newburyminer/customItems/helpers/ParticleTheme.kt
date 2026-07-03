package me.newburyminer.customItems.helpers

import com.destroystokyo.paper.ParticleBuilder
import org.bukkit.Material
import org.bukkit.Particle

enum class ParticleTheme(val settings: ParticleSettings) {

    BASIC_THEME(ParticleSettings(ParticleBuilder(Particle.ENCHANTED_HIT), 2)),
    COLD_OCEAN(ParticleSettings(
        Particle.ENCHANTED_HIT.builder(), 2, Particle.SPLASH.builder(), 3
    )),
    DESERT(ParticleSettings(
        Particle.INSTANT_EFFECT.builder().color(56, 41, 1), 3, Particle.BLOCK.builder().data(Material.SAND.createBlockData()), 2
    )),
    ROCKY(ParticleSettings(
        Particle.LARGE_SMOKE.builder(), 4, Particle.BLOCK.builder().data(Material.STONE.createBlockData()), 3
    )),
    BLACKSTONE(ParticleSettings(
        Particle.SOUL_FIRE_FLAME.builder(), 5, Particle.BLOCK.builder().data(Material.BASALT.createBlockData()), 3
    )),
    MYSTIC(ParticleSettings(
        Particle.INSTANT_EFFECT.builder().color(123, 21, 150), 3, Particle.EFFECT.builder().color(168, 119, 181), 4
    )),
    MILITARY(ParticleSettings(
        Particle.CRIT.builder(), 3, Particle.FIREWORK.builder().color(255, 255, 255), 5
    )),
    WARM_OCEAN(ParticleSettings(
        Particle.INSTANT_EFFECT.builder().color(9, 132, 189), 3, Particle.SPLASH.builder(), 3
    ))

}