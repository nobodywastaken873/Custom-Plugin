package me.newburyminer.customItems.items.customs.weapons.melee

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.pushOut
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.*
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class EnderBlade: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ENDER_BLADE

    private val material = Material.NETHERITE_SWORD
    private val color = arrayOf(11, 79, 82)
    private val name = text("Ender Blade", color)
    private val lore = Utils.loreBlockToList(
        text("Right click to teleport forward 12 blocks, with a 7 second cooldown. Right click while sneaking to teleport forward 12 blocks, and make all hits be critical for the next 5 seconds, with a 15 second cooldown.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_SPEED, -2.2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ENTITY_INTERACTION_RANGE, 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ATTACK_DAMAGE, 8.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
        )
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (ctx.itemType != EventItemType.MAINHAND) return
                val damager = e.damager as? Player ?: return
                if (!EffectManager.hasEffect(damager, CustomEffectType.ENDER_CRIT)) return
                if (e.isCritical) return
                e.damage *= 1.5
                CustomEffects.playSound(e.entity.location, Sound.ENTITY_PLAYER_ATTACK_CRIT, 0.8F, 1.0F)
                CustomEffects.particle(Particle.CRIT.builder(), e.entity.location, 20, 0.5, 0.5)
            }

            is PlayerInteractEvent -> {
                if (!ctx.itemType.isHand()) return
                val item = ctx.item ?: return
                if (!item.offCooldown(e.player)) return
                if (e.action != Action.RIGHT_CLICK_BLOCK && e.action != Action.RIGHT_CLICK_AIR) return
                val startLoc = e.player.eyeLocation.clone()
                val toAdd = e.player.location.direction.clone().normalize().multiply(0.05)
                for (i in 1..120) {
                    startLoc.add(toAdd)
                    if (!startLoc.add(toAdd).block.isPassable || !startLoc.clone().add(Vector(0, 1, 0)).block.isPassable) {
                        startLoc.subtract(toAdd.multiply(2))
                        break
                    }
                }
                startLoc.pushOut(e.player.width)
                e.player.teleport(startLoc)
                CustomEffects.playSound(e.player.location, Sound.ENTITY_SHULKER_TELEPORT, 1.0F, 0.94F)
                if (e.player.isSneaking) {
                    item.setCooldown(e.player, 15.0)
                    EffectManager.applyEffect(e.player, CustomEffectType.ENDER_CRIT, 6 * 20)
                } else {
                    item.setCooldown(e.player, 6.5)
                }
            }

        }

    }

}