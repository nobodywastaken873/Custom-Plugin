package me.newburyminer.customItems.items.customs.weapons.projectile

import com.destroystokyo.paper.ParticleBuilder
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.applyDamage
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.crossbowProj
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.getCorners
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.pushOut
import me.newburyminer.customItems.Utils.Companion.reduceDura
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entities.CustomEntity
import me.newburyminer.customItems.helpers.AttributeManager.Companion.tempAttribute
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.damage.DamageSettings
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import me.newburyminer.customItems.items.EventItemType
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.*
import org.bukkit.event.block.Action
import org.bukkit.event.entity.*
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import kotlin.math.abs

class SonicCrossbow: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.SONIC_CROSSBOW

    private val material = Material.CROSSBOW
    private val color = arrayOf(2, 98, 117)
    private val name = text("Sonic Crossbow", color)
    private val lore = Utils.loreBlockToList(
        text("Shoots a piercing sonic boom that does true damage to players and high damage to mobs, with a 20 second cooldown.", Utils.GRAY),
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is ProjectileLaunchEvent -> {
                if (e.entity !is Arrow) return
                if (e.entity.shooter !is Player) return
                val shooter = e.entity.shooter as Player
                val crossbow = ctx.item ?: return

                val direction = shooter.location.direction.normalize().multiply(0.5)
                val current = shooter.location
                val toDamage = mutableListOf<LivingEntity>()
                for (i in 0..30) {
                    current.add(direction)
                    for (entity in current.getNearbyEntitiesByType(LivingEntity::class.java, 4.0, 4.0, 4.0)) {
                        var within = entity.boundingBox.getCorners(entity.world).any { it.subtract(current).length() < 1.0 }
                        if (within) {
                            toDamage.add(entity)
                        }
                    }
                }

                for (entity in toDamage) {
                    if (entity is Player) {
                        entity.applyDamage(
                            DamageSettings(
                            8.0, CustomDamageType.ALL_BYPASS, shooter)
                        )
                    } else {
                        entity.applyDamage(
                            DamageSettings(
                            35.0, DamageType.PLAYER_ATTACK, shooter)
                        )
                    }
                }

                CustomEffects.particleLine(ParticleBuilder(Particle.SONIC_BOOM), shooter.location, current, 15)
                CustomEffects.playSound(shooter.location, Sound.ENTITY_WARDEN_SONIC_BOOM, 1.0F, 1.0F)

                shooter.setCooldown(CustomItem.SONIC_CROSSBOW, 20.0)
                e.entity.remove()
            }

        }

    }

}