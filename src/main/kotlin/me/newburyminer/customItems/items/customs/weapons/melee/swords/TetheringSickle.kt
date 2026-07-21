package me.newburyminer.customItems.items.customs.weapons.melee.swords

import me.newburyminer.customItems.Utils
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
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class TetheringSickle: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.TETHERING_SICKLE

    private val material = Material.IRON_SWORD
    private val color = arrayOf(204, 172, 137)
    private val name = text("Tethering Sickle", color)
    private val lore = Utils.loreBlockToList(
        text("On hit, inflict a player with +90% explosion knockback resistance for 12 seconds, which reduces knockback from all explosions, " +
                "including wind charges, end crystals, etc. 25s cooldown.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_DAMAGE, 8.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ATTACK_SPEED, -2.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
        )
        .setLore(lore)
        .build()

    init {
        register(EntityDamageByEntityEvent::class, { e ->
            slotMatches(e, EquipmentSlot.HAND, custom)
        },
        {e ->
            val player = e.damager as? Player ?: return@register
            val damaged = e.entity as? LivingEntity ?: return@register

            if (!player.offCooldown(custom)) return@register
            CustomEffects.playSound(player.location, Sound.ITEM_LEAD_TIED, 0.7f, 1.3f)

            EffectManager.applyEffect(damaged as? Player ?: return@register, CustomEffectType.ATTRIBUTE,
                EffectData(12 * 20, attributeData = AttributeData(0.9, Attribute.EXPLOSION_KNOCKBACK_RESISTANCE, AttributeModifier.Operation.ADD_NUMBER)))
            player.setCooldown(custom, 25.0)
        })
    }

}