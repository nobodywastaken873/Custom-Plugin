package me.newburyminer.customItems.helpers

import com.destroystokyo.paper.ParticleBuilder
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.inventory.ItemStack

enum class ParticleTheme(val settings: ParticleSettings) {

    BASIC_THEME(ParticleSettings(ParticleBuilder(Particle.ENCHANTED_HIT), 2)),
    COLD_OCEAN(ParticleSettings(
        Particle.ENCHANTED_HIT.builder(), 2, Particle.SPLASH.builder(), 3
    )),
    DESERT(ParticleSettings(
        Particle.ENTITY_EFFECT.builder().color(56, 41, 1), 4, Particle.BLOCK.builder().data(Material.SAND.createBlockData()), 2
    )),
    ROCKY(ParticleSettings(
        Particle.LARGE_SMOKE.builder(), 4, Particle.BLOCK.builder().data(Material.STONE.createBlockData()), 3
    )),
    BLACKSTONE(ParticleSettings(
        Particle.SOUL_FIRE_FLAME.builder(), 5, Particle.BLOCK.builder().data(Material.BASALT.createBlockData()), 3
    )),
    MYSTIC(ParticleSettings(
        Particle.ENTITY_EFFECT.builder().color(123, 21, 150), 4, Particle.EFFECT.builder().color(168, 119, 181), 4
    )),
    MILITARY(ParticleSettings(
        Particle.CRIT.builder(), 3, Particle.FIREWORK.builder(), 5
    )),
    WARM_OCEAN(ParticleSettings(
        Particle.ENTITY_EFFECT.builder().color(9, 132, 189), 4, Particle.SPLASH.builder(), 3
    )),
    SURFACE(ParticleSettings(
        Particle.BLOCK.builder().data(Material.DIRT.createBlockData()), 3, Particle.ITEM.builder().data(ItemStack.of(Material.MUD)), 3
    )),
    CAVES(ParticleSettings(
        Particle.BLOCK.builder().data(Material.BLACKSTONE.createBlockData()), 3, Particle.BLOCK.builder().data(Material.STONE.createBlockData()), 3
    ))

}