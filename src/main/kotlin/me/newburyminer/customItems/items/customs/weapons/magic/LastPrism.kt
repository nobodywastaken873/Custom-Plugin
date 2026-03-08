package me.newburyminer.customItems.items.customs.weapons.magic

import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.rayTraceEntities
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.behaviors.HeldActivation
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

    init {
        register(PlayerInteractEvent::class, { e ->
            e.item.isItem(custom)
        },
        {e ->
            val wand = e.item ?: return@register
            if ((e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_BLOCK) && wand.offCooldown(e.player, "Zap")) {

                CustomEffects.raycastParticleLine(Particle.END_ROD.builder(), e.player.eyeLocation, e.player.location.direction, 80.0, 400)
                val hitEntities = e.player.eyeLocation.world.rayTraceEntities(e.player.eyeLocation, e.player.location.direction, 80.0,
                    ignore = e.player, radius = 0.4)

                for (entity in hitEntities) {
                    if (entity !is LivingEntity) continue
                    if (entity is Player)
                        EffectManager.applyEffect(entity, CustomEffectType.LAST_PRISM_ZAP, EffectData(2 * 20, unique = true))
                    else
                        entity.damage(21.0, DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(e.player as Entity).withCausingEntity(e.player as Entity).build())
                }

                wand.setCooldown(e.player, 10.0, "Zap")
                CustomEffects.playSound(e.player.location, Sound.ITEM_TRIDENT_HIT, 1F, 1.3F)

            }
            else if ((e.action == Action.RIGHT_CLICK_BLOCK || e.action == Action.RIGHT_CLICK_AIR) && wand.offCooldown(e.player, "Beam")) {
                heldActivation.used(e.player)
            }
        })
    }

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(
            6 to {player -> lastPrismTick(player)},
            1 to {player -> lastPrismDamage(player)}
        )

    private val heldActivation = HeldActivation(
        activationTicks = 13
    )
    private val counterMap = mutableMapOf<UUID, Int>()
    private val usedMap = mutableMapOf<UUID, Boolean>()
    private fun lastPrismTick(player: Player) {
        when (val result = heldActivation.tick(player)) {
            is HeldActivation.ActivationResult.Cancelled -> {
                CustomEffects.playSound(player.location, Sound.BLOCK_ANVIL_PLACE, 0.7F, 1.2F)
                if (result.ticks >= 13) player.setCooldown(custom, (0.3 * 4 * result.ticks * 10).toInt() / 10.0, "Beam")
            }
            is HeldActivation.ActivationResult.Charging ->
                CustomEffects.playSound(player.location, Sound.ITEM_TRIDENT_THUNDER, 0.7F, (0.15 * result.ticks.coerceAtMost(13)).toFloat())
            is HeldActivation.ActivationResult.Activated ->
                CustomEffects.playSound(player.location, Sound.ITEM_TRIDENT_THUNDER, 0.7F, (0.15 * result.ticks.coerceAtMost(13)).toFloat())
            else -> {}
        }
    }
    private fun lastPrismDamage(player: Player) {
        val result = heldActivation.currentResult(player)
        if (result !is HeldActivation.ActivationResult.Activated) return

        val facing = player.location.direction.normalize()
        val startingLocation = player.location.clone().add(Vector(0.0, 1.0, 0.0))
        CustomEffects.raycastParticleLine(Particle.ELECTRIC_SPARK.builder(), startingLocation.clone(), facing, 120.0, 600)

        val hitEntities = player.world.rayTraceEntities(startingLocation, facing, 120.0, ignore = player)
        for (entity in hitEntities) {
            if (entity !is LivingEntity) continue
            entity.damage(21.0, DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(player as Entity).withCausingEntity(player as Entity).build())
        }
    }

}