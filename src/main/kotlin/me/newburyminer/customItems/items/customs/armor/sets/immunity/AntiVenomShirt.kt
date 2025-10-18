package me.newburyminer.customItems.items.customs.armor.sets.immunity

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.items.armorsets.ArmorSet
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType

class AntiVenomShirt: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ANTI_VENOM_SHIRT

    private val material = Material.NETHERITE_CHESTPLATE
    private val color = arrayOf(43, 92, 48)
    private val name = text("Anti-venom Shirt", color)
    private val lore = Utils.loreBlockToList(
        text("Gain permanent immunity to hunger, poison, and wither.", Utils.GRAY),
        text(""),
        text("Full Set Bonus (4 pieces): Immunity Set", Utils.GRAY),
        text("Upon receiving any negative effect, convert it into the corresponding positive effect with potency increased by 1 level and triple the duration.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 10.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
            SimpleModifier(Attribute.MAX_HEALTH, 6.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
            SimpleModifier(Attribute.ATTACK_DAMAGE, 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
        )
        .setArmorSet(ArmorSet.IMMUNITY)
        .build()

    override fun handle(ctx: EventContext) {
        when (val e = ctx.event) {

            is EntityPotionEffectEvent -> {
                if (ctx.itemType != EventItemType.CHESTPLATE) return
                val player = ctx.player ?: return
                if (e.action != EntityPotionEffectEvent.Action.ADDED && e.action != EntityPotionEffectEvent.Action.CHANGED) return
                if (e.newEffect!!.type in arrayOf(PotionEffectType.HUNGER, PotionEffectType.POISON, PotionEffectType.WITHER))
                    e.isCancelled = true
            }

        }
    }

}