package me.newburyminer.customItems.items.customs.armor.helmets

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect

class DrinkingCap: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.DRINKING_HAT

    private val material = Material.NETHERITE_HELMET
    private val color = arrayOf(235, 170, 127)
    private val name = text("Drinking Cap", color)
    private val lore = Utils.loreBlockToList(
        text("While wearing this, all potions you use will give you double the duration.", Utils.GRAY),
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

            is EntityPotionEffectEvent -> {
                if (ctx.itemType != EventItemType.HELMET) return
                val player = ctx.player ?: return
                if (e.cause !in arrayOf(
                        EntityPotionEffectEvent.Cause.POTION_SPLASH,
                        EntityPotionEffectEvent.Cause.POTION_DRINK,
                        EntityPotionEffectEvent.Cause.FOOD,
                        EntityPotionEffectEvent.Cause.TOTEM,
                    )) return
                if (e.action != EntityPotionEffectEvent.Action.ADDED && e.action != EntityPotionEffectEvent.Action.CHANGED) return
                val newEffect = e.newEffect ?: return
                e.isCancelled = true
                player.addPotionEffect(PotionEffect(newEffect.type, newEffect.duration * 2,
                    newEffect.amplifier, newEffect.isAmbient, newEffect.hasParticles()))
            }

        }
    }

}