package me.newburyminer.customItems.items.customs.armor.helmets

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.*
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class XrayGoggles: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.XRAY_GOGGLES

    private val material = Material.NETHERITE_HELMET
    private val color = arrayOf(44, 145, 22)
    private val name = text("X-ray Goggles", color)
    private val lore = Utils.loreBlockToList(
        text("Sneak to give all entities within a 20 block radius glowing for 20 seconds, with a 20 second cooldown.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD),
            SimpleModifier(Attribute.KNOCKBACK_RESISTANCE, 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD),
            SimpleModifier(Attribute.MAX_HEALTH, 2.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD),
        )
        .build()

    override fun handle(ctx: EventContext) {
        when (val e = ctx.event) {

            is PlayerToggleSneakEvent -> {
                if (ctx.itemType != EventItemType.HELMET) return
                val player = ctx.player ?: return
                if (!e.isSneaking) return
                if (!player.offCooldown(CustomItem.XRAY_GOGGLES)) return
                for (entity in player.location.getNearbyEntities(20.0, 20.0, 20.0)) {
                    if (e.player.location.subtract(entity.location).length() > 20.0) continue
                    if (entity !is LivingEntity) continue
                    if (entity == player) continue
                    entity.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 400, 0, true, false, false))
                }
                player.setCooldown(CustomItem.XRAY_GOGGLES, 20.0)
                CustomEffects.playSound(player.location, Sound.BLOCK_BEACON_ACTIVATE, 1.0F, 0.8F)
            }

        }
    }

}