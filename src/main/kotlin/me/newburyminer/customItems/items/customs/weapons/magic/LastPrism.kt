package me.newburyminer.customItems.items.customs.weapons.magic

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Consumable
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreBlockToList
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.rayTraceManyEntities
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.behaviors.HeldActivation
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
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
    private val lore = loreBlockToList(
        text("Left click to shoot a zap spell, 10s cooldown. Hold right click for 6 seconds to begin shooting a laser beam that continues until you release right click.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setData(DataComponentTypes.CONSUMABLE, Consumable.consumable()
            .animation(ItemUseAnimation.TRIDENT)
            .consumeSeconds(32000.0F)
            .hasConsumeParticles(false)
            .build()
        )
        .build()

    init {
        register(PlayerInteractEvent::class, { e ->
            e.item.isItem(custom)
        },
        {e ->
            val wand = e.item ?: return@register
            if ((e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_BLOCK) && wand.offCooldown(e.player)) {

                CustomEffects.raycastParticleLine(Particle.END_ROD.builder(), e.player.eyeLocation, e.player.location.direction, 80.0, 5.0)
                val hitEntities = e.player.eyeLocation.world.rayTraceManyEntities(e.player.eyeLocation, e.player.location.direction, 80.0,
                    ignore = e.player, radius = 0.4)

                for (entity in hitEntities) {
                    if (entity !is LivingEntity) continue
                    if (entity is Player)
                        EffectManager.applyEffect(entity, CustomEffectType.LAST_PRISM_ZAP, EffectData(2 * 20, unique = true))
                    else
                        entity.damage(21.0, DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(e.player as Entity).withCausingEntity(e.player as Entity).build())
                }

                wand.setCooldown(e.player, 10.0)
                CustomEffects.playSound(e.player.location, Sound.ITEM_TRIDENT_HIT, 1F, 1.3F)

            }

            else if ((e.action == Action.RIGHT_CLICK_BLOCK || e.action == Action.RIGHT_CLICK_AIR) && e.player.offCooldown(custom)) {
                CustomEffects.playSound(e.player.location, Sound.ITEM_TRIDENT_THUNDER, 0.7F, 0.3F)
            }
        })
    }

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(
            2 to {player -> lastPrismTick(player)}
        )

    private fun lastPrismTick(player: Player) {
        if (!player.activeItem.isItem(custom)) return
        if (player.activeItemUsedTime < 120) {
            if (player.ticksLived % 5 == 0) CustomEffects.playSound(player.location, Sound.ITEM_TRIDENT_THUNDER, 0.7F, 0.3F)
            return
        }

        if (player.ticksLived % 5 == 0) {
            CustomEffects.playSound(player.location, Sound.ITEM_TRIDENT_THUNDER, 0.7F, 1.3F)
        }
        lastPrismDamage(player)
    }
    private fun lastPrismDamage(player: Player) {
        val facing = player.location.direction.normalize()
        val startingLocation = player.location.clone().add(Vector(0.0, 1.0, 0.0))
        CustomEffects.raycastParticleLine(Particle.ELECTRIC_SPARK.builder(), startingLocation.clone(), facing, 120.0, 4.0)

        val hitEntities = player.world.rayTraceManyEntities(startingLocation, facing, 120.0, ignore = player)
        for (entity in hitEntities) {
            if (entity !is LivingEntity) continue
            entity.damage(21.0, DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(player as Entity).withCausingEntity(player as Entity).build())
        }
    }

}