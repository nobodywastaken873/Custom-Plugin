package me.newburyminer.customItems.items.customs.weapons.projectile

import com.destroystokyo.paper.ParticleBuilder
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.applyDamage
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.damage.DamageSettings
import me.newburyminer.customItems.helpers.rayTraceEntities
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.inventory.ItemStack

class SonicCrossbow: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.SONIC_CROSSBOW

    private val material = Material.CROSSBOW
    private val color = arrayOf(2, 98, 117)
    private val name = text("Sonic Crossbow", color)
    private val lore = Utils.loreBlockToList(
        text("Shoots a piercing sonic boom that does 8 true damage to players and high damage to mobs, with a 20 second cooldown.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(ProjectileLaunchEvent::class, { e ->
            activeRangedMatches(e, custom)
        },
        {e ->
            val shooter = e.entity.shooter as? Player ?: return@register

            val direction = shooter.location.direction.normalize().multiply(0.5)
            val current = shooter.location
            val hitEntities = shooter.world.rayTraceEntities(shooter.eyeLocation, direction, 15.0, ignore = shooter)

            for (entity in hitEntities) {
                if (entity !is LivingEntity) continue

                if (entity is Player)
                    entity.applyDamage(DamageSettings(8.0, CustomDamageType.ALL_BYPASS, shooter))
                else
                    entity.applyDamage(DamageSettings(35.0, DamageType.PLAYER_ATTACK, shooter))
            }

            CustomEffects.particleLine(ParticleBuilder(Particle.SONIC_BOOM), shooter.location,
                shooter.location.add(shooter.location.direction.normalize().multiply(15)), 15)
            CustomEffects.playSound(shooter.location, Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0F, 1.0F)

            shooter.setCooldown(CustomItem.SONIC_CROSSBOW, 20.0)
            e.entity.remove()
        })
    }

}