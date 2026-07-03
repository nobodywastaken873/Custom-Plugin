package me.newburyminer.customItems.entity.components.spells

import me.newburyminer.customItems.Utils.Companion.getNearestPlayer
import me.newburyminer.customItems.entity.DeserializationInterface
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityComponentType
import me.newburyminer.customItems.entity.EntityWrapper
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.MagicMissileComponent
import me.newburyminer.customItems.entity.components.utils.AbstractSpellComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.velocity.VelocityProvider
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.HomingSystem
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.helpers.getIntersectingBlocks
import me.newburyminer.customItems.helpers.getUpperCenter
import me.newburyminer.customItems.helpers.getValidSpawnLocs
import me.newburyminer.customItems.helpers.hasIntersectingBlocks
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobFactory
import me.newburyminer.customItems.mobprovider.MobRegistry
import me.newburyminer.customItems.mobprovider.mobs.BasicZombie
import me.newburyminer.customItems.structures.StructureReference
import me.newburyminer.customItems.structures.structure.AbandonedShip
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Marker
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
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

        if (caster.ticksLived % particleSettings.preParticleSeparation == 0) {
            spawnLocs.forEach {
                CustomEffects.particleLine(
                    particleSettings.preParticle,
                    it,
                    it.clone().add(Vector(0.0, 2.0, 0.0)),
                    particleSettings.concentration
                )
            }
        }

        if (castingTicks > 0) {
            castingTicks -= 1

            if (castingTicks <= 0) {

                CustomEffects.playSound(caster.location, Sound.ENTITY_EVOKER_PREPARE_SUMMON, 3.0F, 1.4F)

                val center = caster.location
                for (loc in spawnLocs) {

                    val context = MobContext(center.length(), StructureReference.Difficulty.NORMAL, AbandonedShip, loc)
                    MobFactory.create(mob.build(context), context)

                }

                applyCooldown(baseCooldown)
                spawnLocs.clear()
            }
        }

        if (offCooldown()) {

            if (startCasting(wrapper)) {
                val ctx = MobContext(caster.location.length(), StructureReference.Difficulty.NORMAL, AbandonedShip, caster.location)
                val boundingBox = MobFactory.getHitbox(mob.build(ctx), ctx)

                spawnLocs.addAll(caster.location.world.getValidSpawnLocs(caster.location, boundingBox, 3, count))
                CustomEffects.playSound(caster.location, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 3.0F, 1.2F)
            }

        }
    }
}