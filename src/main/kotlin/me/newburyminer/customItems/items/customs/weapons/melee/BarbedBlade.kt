package me.newburyminer.customItems.items.customs.weapons.melee

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.effects.AttributeData
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.items.*
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class BarbedBlade: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.BARBED_BLADE

    private val material = Material.NETHERITE_SWORD
    private val color = arrayOf(107, 107, 140)
    private val name = text("Barbed Blade", color)
    private val lore = Utils.loreBlockToList(
        text("On hit, inflict a player with 4 decreased armor points for 4 seconds, with a 15 second cooldown. Does not stack with other players. Additionally, inflict Darkness on players for 5 seconds with a 1/5 chance.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_SPEED, -2.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ATTACK_DAMAGE, 10.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
        )
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (ctx.itemType != EventItemType.MAINHAND) return
                val player = e.damager as? Player ?: return
                val damaged = e.entity as? LivingEntity ?: return
                if (Math.random() < 1.0 / 5.0 && e.entity is LivingEntity) {
                    damaged.addPotionEffect(PotionEffect(PotionEffectType.DARKNESS, 100, 0))
                }
                if (!player.inventory.itemInMainHand.offCooldown(e.damager as Player)) return
                EffectManager.applyEffect(damaged as? Player ?: return, CustomEffectType.ATTRIBUTE,
                    EffectData(4 * 20, attributeData = AttributeData(-4.0, Attribute.ARMOR, AttributeModifier.Operation.ADD_NUMBER)))
                player.inventory.itemInMainHand.setCooldown(e.damager as Player, 15.0)
            }

        }

    }

}