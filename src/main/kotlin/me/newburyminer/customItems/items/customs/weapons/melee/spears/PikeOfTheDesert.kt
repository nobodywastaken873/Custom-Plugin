package me.newburyminer.customItems.items.customs.weapons.melee.spears

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.AttackRange
import io.papermc.paper.datacomponent.item.KineticWeapon
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.SimpleModifier
import org.bukkit.Material
import org.bukkit.Registry
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class PikeOfTheDesert: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.PIKE_OF_THE_DESERT

    private val material = Material.NETHERITE_SPEAR
    private val color = arrayOf(245, 247, 134)
    private val name = text("Pike of the Desert", color)
    private val lore = Utils.loreBlockToList(
        text("Spear-type weapon:", Utils.GRAY),
        text("Damage Multiplier: 2.20, Charge Delay: 5 ticks, Hitbox Margins: +0.35 blocks, Reach: 0.8-4.8 blocks", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_DAMAGE, 7.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ATTACK_SPEED, -3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
        )
        .setData(DataComponentTypes.KINETIC_WEAPON, KineticWeapon.kineticWeapon()
            .contactCooldownTicks(10)
            .damageConditions(KineticWeapon.condition(200, 1.0F, 0.5F))
            .knockbackConditions(KineticWeapon.condition(150, 1.0F, 0.5F))
            .dismountConditions(KineticWeapon.condition(150, 1.0F, 0.5F))
            .damageMultiplier(1.87F)
            .delayTicks(5)
            .forwardMovement(0.2F)
            .sound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_USE)!!)
            .hitSound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_HIT)!!)
            .build()
        )
        .setData(DataComponentTypes.ATTACK_RANGE,
            AttackRange.attackRange().hitboxMargin(0.35F).minReach(0.8F).maxReach(4.8F).build()
        )
        .setLore(lore)
        .build()

}