package me.newburyminer.customItems.items.customs.weapons.magic

import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.util.*

class LastPrism: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.LAST_PRISM

    private val material = Material.COPPER_INGOT
    private val color = arrayOf(243, 219, 255)
    private val name = text("Last Prism", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is PlayerInteractEvent -> {
                val item = ctx.item ?: return
                if (!ctx.itemType.isHand()) return
                if (e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_BLOCK) {
                    if (!item.offCooldown(e.player, "Zap")) return
                    val facing = e.player.location.direction.normalize().clone().multiply(0.1)
                    val startingLocation = e.player.location.clone().add(Vector(0.0, 1.6, 0.0))
                    for (i in 0..800) {
                        CustomEffects.particle(Particle.END_ROD.builder(), startingLocation, 1)
                        if (i%5 == 0) {
                            for (entity in startingLocation.getNearbyEntities(3.0, 3.0, 3.0)) {
                                if (entity is LivingEntity && entity != e.player) {
                                    if (!entity.boundingBox.overlaps(
                                            startingLocation.clone().add(Vector(0.75, 0.75, 0.75)).toVector(),
                                            startingLocation.clone().add(Vector(-0.75, -0.75, -0.75)).toVector())) continue
                                    if (entity is Player) EffectManager.applyEffect(entity, CustomEffectType.LAST_PRISM_ZAP, EffectData(2 * 20, unique = true))
                                    else entity.damage(21.0, DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(e.player as Entity).withCausingEntity(e.player as Entity).build())
                                }
                            }
                        }
                        if (!startingLocation.add(facing).block.isPassable) {
                            break
                        }
                    }
                    item.setCooldown(e.player, 10.0, "Zap")
                    CustomEffects.playSound(e.player.location, Sound.ITEM_TRIDENT_HIT, 1F, 1.3F)
                } else if (e.action == Action.RIGHT_CLICK_BLOCK || e.action == Action.RIGHT_CLICK_AIR) {
                    if (!item.offCooldown(e.player, "Beam")) return
                    usedMap[e.player.uniqueId] = true
                }
            }

        }

    }

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(
            6 to {player -> lastPrismTick(player)},
            1 to {player -> lastPrismDamage(player)}
        )

    private val counterMap = mutableMapOf<UUID, Int>()
    private val usedMap = mutableMapOf<UUID, Boolean>()
    private fun lastPrismTick(player: Player) {
        val uuid = player.uniqueId
        if (usedMap[uuid] == true) {
            counterMap[uuid] = (counterMap[uuid] ?: 0) + 1
            val lastPrismCount = counterMap[uuid] ?: return
            CustomEffects.playSound(
                player.location,
                Sound.ITEM_TRIDENT_THUNDER,
                1F,
                (0.15 * (if (lastPrismCount > 12) 13 else lastPrismCount)).toFloat()
            )
        } else {
            if ((counterMap[uuid] ?: 0) != 0) {
                CustomEffects.playSound(player.location, Sound.BLOCK_ANVIL_PLACE, 1F, 1.2F)
                val totalTime = counterMap[uuid] ?: return
                if (totalTime > 12) {
                    player.setCooldown(CustomItem.LAST_PRISM, (0.3 * 4 * totalTime * 10).toInt() / 10.0, "Beam")
                }
            }
            counterMap[uuid] = 0
        }
        usedMap[uuid] = false
    }
    private fun lastPrismDamage(player: Player) {
        if ((counterMap[player.uniqueId] ?: 0) > 12) {
            val facing = player.location.direction.normalize().clone().multiply(0.1)
            val startingLocation = player.location.clone().add(Vector(0.0, 1.0, 0.0))
            for (i in 0..1200) {
                CustomEffects.particle(Particle.ELECTRIC_SPARK.builder(), startingLocation, 1)
                if (i % 5 == 0) {
                    for (entity in startingLocation.getNearbyEntities(3.0, 3.0, 3.0)) {
                        if (entity is LivingEntity && entity != player) {
                            if (!entity.boundingBox.overlaps(
                                    startingLocation.clone().add(Vector(0.75, 0.75, 0.75)).toVector(),
                                    startingLocation.clone().add(Vector(-0.75, -0.75, -0.75)).toVector()
                                )
                            ) continue
                            entity.damage(21.0,
                                DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(player as Entity)
                                    .withCausingEntity(player as Entity).build()
                            )
                        }
                    }
                }
                if (!startingLocation.add(facing).block.isPassable) {
                    break
                }
            }
        }
    }

}