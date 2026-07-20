package me.newburyminer.customItems.items.customs.weapons.melee

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.effects.AttributeData
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.SimpleModifier
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class DarkSteelRapier: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.DARK_STEEL_RAPIER

    private val material = Material.IRON_SWORD
    private val color = arrayOf(74, 98, 125)
    private val name = text("Dark Steel Rapier", color)
    private val lore = Utils.loreBlockToList(text("Cannot perform critical hits. Right click to gain 60% increased speed for 15 seconds and blind players within 10 blocks of you for 8 seconds, 40 second cooldown.", Utils.GRAY))

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_DAMAGE, 9.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ATTACK_SPEED, -2.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.MOVEMENT_SPEED, 0.01, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
        )
        .setLore(lore)
        .build()

    init {
        register(EntityDamageByEntityEvent::class, { e ->
            slotMatches(e, EquipmentSlot.HAND, custom) &&
            e.isCritical
        },
        {e ->
            e.damage *= 0.66
        })

        register(PlayerInteractEvent::class, { e ->
            e.item.isItem(custom) &&
            e.player.offCooldown(custom) &&
            isRightClick(e)
        },
        {e ->
            e.player.setCooldown(custom, 40.0)
            CustomEffects.playSound(e.player.location, Sound.BLOCK_CONDUIT_DEACTIVATE, 1.0f, 0.5f)

            e.player.swingHand(e.hand ?: return@register)

            for (player in e.player.location.getNearbyPlayers(10.0)) {
                if (e.player == player) continue
                player.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 160, 0))
            }
            EffectManager.applyEffect(e.player, CustomEffectType.ATTRIBUTE,
                EffectData(15 * 20, attributeData = AttributeData(0.06, Attribute.MOVEMENT_SPEED, AttributeModifier.Operation.ADD_NUMBER)))
        })
    }

}