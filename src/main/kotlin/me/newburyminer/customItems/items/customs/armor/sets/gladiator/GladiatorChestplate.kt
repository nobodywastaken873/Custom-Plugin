package me.newburyminer.customItems.items.customs.armor.sets.gladiator

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.SimpleModifier
import me.newburyminer.customItems.items.armorsets.ArmorSet
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.damage.DamageType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class GladiatorChestplate: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.GLADIATORS_CHESTPLATE

    private val material = Material.NETHERITE_CHESTPLATE
    private val color = arrayOf(204, 116, 2)
    private val name = text("Gladiator Chestplate", color)
    private val lore = Utils.loreBlockToList(
        text("Deal an additional 10% damage with spears. Take 20% less damage from maces.", Utils.GRAY),
        text(""),
        text("Full Set Bonus (4 pieces): Gladiator's Set", Utils.GRAY),
        text("Right click with a spear to charge forward, with a 7s cooldown. Gain 20% additional damage with spears.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 9.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
            SimpleModifier(Attribute.KNOCKBACK_RESISTANCE, 0.15, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
            SimpleModifier(Attribute.MOVEMENT_SPEED, 0.005, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
            SimpleModifier(Attribute.ATTACK_DAMAGE, 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
        )
        .setArmorSet(ArmorSet.GLADIATOR)
        .build()

    init {
        register(EntityDamageByEntityEvent::class, { e ->
            e.entity is Player &&
                    (e.entity as Player).inventory.chestplate.isItem(custom) &&
                    e.damager is Player &&
                    e.damageSource.damageType == DamageType.MACE_SMASH
        },
        {e ->
            e.damage *= 0.8
        })
    }
}