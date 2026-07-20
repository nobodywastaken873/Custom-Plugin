package me.newburyminer.customItems.entity.components.spells

import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.components.utils.AbstractSpellComponent
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.helpers.getValidSpawnLocs
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobRegistry
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
import me.newburyminer.customItems.structures.StructureReference
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Mob
import org.bukkit.util.Vector

class SummonerSpellComponent(
    private val count: Int,
    private val mob: MobDefinition,
    castTime: Int,
    baseCooldown: Int,
    private val particleTheme: ParticleTheme
): AbstractSpellComponent(baseCooldown, castTime) {

    override fun serialize(): Map<String, Any> {
        return mapOf(
            "count" to count,
            "mob" to mob.id,
            "castTime" to spellDuration,
            "baseCooldown" to baseCooldown,
            "particleTheme" to particleTheme.name
        )
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.SUMMONER_SPELL_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return SummonerSpellComponent(
                map["count"].asInt(),
                MobRegistry.getMob(map["mob"].asString()) ?: BasicZombie,
                map["castTime"].asInt(),
                map["baseCooldown"].asInt(),
                ParticleTheme.valueOf(map["particleTheme"].asString())
            )
        }
    }

    private val particleSettings = particleTheme.settings
    private var spawnLocs: MutableList<Location> = mutableListOf()

    override fun tick(wrapper: EntityWrapper) {
        val caster = wrapper.entity as? Mob ?: return
        reduceCooldown(1)

        if (caster.ticksLived % 3 == 0) {
            spawnLocs.forEach {
                CustomEffects.particleLine(
                    Particle.SOUL_FIRE_FLAME.builder(),
                    it,
                    it.clone().add(Vector(0.0, 2.0, 0.0)),
                    6.0
                )
            }
        }

        if (castingTicks > 0) {
            castingTicks -= 1

            if (castingTicks <= 0) {

                CustomEffects.playSound(caster.location, Sound.ENTITY_EVOKER_PREPARE_SUMMON, 3.0F, 1.4F)

                val center = caster.location
                for (loc in spawnLocs) {

                    val context = MobContext(center.length(), StructureReference.Difficulty.NORMAL, loc)
                    mob.build(context).createEntity(context)

                }

                applyCooldown(baseCooldown)
                spawnLocs.clear()
            }
        }

        if (offCooldown()) {

            val nearbyOfSummonedType = caster.location.getNearbyEntitiesByType(mob.getType().java, 12.0).size

            if (caster.target != null && nearbyOfSummonedType < 8 && caster.hasLineOfSight(caster.target ?: return) && startCasting(wrapper)) {
                val boundingBox = mob.getHitbox()

                spawnLocs.addAll(caster.location.getValidSpawnLocs(boundingBox, count))
                CustomEffects.playSound(caster.location, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 3.0F, 1.2F)
            }

        }
    }
}