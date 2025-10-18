package me.newburyminer.customItems.items.customs.armor.leggings

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityResurrectEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class ShadowLegs: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.SHADOW_LEGS

    private val material = Material.NETHERITE_LEGGINGS
    private val color = arrayOf(44, 4, 108)
    private val name = text("Shadow Legs", color)
    private val lore = Utils.loreBlockToList(
        text("When your totem is popped, gain Speed 3, Strength 3, Resistance 2, and Regeneration 3 for 25 seconds.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 7.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.MOVEMENT_SPEED, 0.01, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.ATTACK_DAMAGE, 2.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.ATTACK_SPEED, 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
            SimpleModifier(Attribute.MAX_HEALTH, 2.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS),
        )
        .build()

    override fun handle(ctx: EventContext) {
        when (val e = ctx.event) {

            is EntityResurrectEvent -> {
                if (ctx.itemType != EventItemType.LEGGINGS) return
                val player = ctx.player ?: return
                if (e.isCancelled) return
                if (!player.offCooldown(CustomItem.SHADOW_LEGS)) return
                player.setCooldown(CustomItem.SHADOW_LEGS, 60.0)
                val duration = if (player.inventory.helmet?.isItem(CustomItem.DRINKING_HAT) == true) 1000 else 500
                Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                    (e.entity as Player).addPotionEffects(mutableListOf(
                        PotionEffect(PotionEffectType.RESISTANCE, duration, 1),
                        PotionEffect(PotionEffectType.STRENGTH, duration, 2),
                        PotionEffect(PotionEffectType.SPEED, duration, 2),
                        PotionEffect(PotionEffectType.REGENERATION, duration, 2)
                    ))
                })
            }

        }
    }

}