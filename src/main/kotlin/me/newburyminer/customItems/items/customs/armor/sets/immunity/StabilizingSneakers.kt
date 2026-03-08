package me.newburyminer.customItems.items.customs.armor.sets.immunity

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.SimpleModifier
import me.newburyminer.customItems.items.armorsets.ArmorSet
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType

class StabilizingSneakers: CustomItemDefinition, PotionEffectCancel {

    override val custom: CustomItem = CustomItem.STABILZING_SNEAKERS

    private val material = Material.NETHERITE_BOOTS
    private val color = arrayOf(102, 82, 64)
    private val name = text("Stabilizing Sneakers", color)
    private val lore = Utils.loreBlockToList(
        text("Gain permanent immunity to slow falling and levitation.", Utils.GRAY),
        text(""),
        text("Full Set Bonus (4 pieces): Immunity Set", Utils.GRAY),
        text("Upon receiving any negative effect, convert it into the corresponding positive effect with potency increased by 1 level (capped at 3) and triple the duration.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.MAX_HEALTH, 2.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.ATTACK_DAMAGE, 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
        )
        .setArmorSet(ArmorSet.IMMUNITY)
        .build()

    override val potionEffects: List<PotionEffectType> = listOf(PotionEffectType.SLOW_FALLING, PotionEffectType.LEVITATION)
    init {
        register(EntityPotionEffectEvent::class, { e ->
            potionEffectMatches(e, EquipmentSlot.FEET, custom)
        },
        {e ->
            cancelPotionEffect(e)
        })
    }

}