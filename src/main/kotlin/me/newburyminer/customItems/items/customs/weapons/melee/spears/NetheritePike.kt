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

class NetheritePike: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.NETHERITE_PIKE

    private val material = Material.NETHERITE_SPEAR
    private val color = arrayOf(10, 242, 234)
    private val name = text("Netherite Pike", color)
    private val lore = Utils.loreBlockToList(
        text("Spear-type weapon:", Utils.GRAY),
        text("Damage Multiplier: 1.76, Charge Delay: 10 ticks, Hitbox Margins: +0.2 blocks, Reach: 1.5-5.0 blocks", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_DAMAGE, 5.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ATTACK_SPEED, -3.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
        )
        .setData(DataComponentTypes.KINETIC_WEAPON, KineticWeapon.kineticWeapon()
            .contactCooldownTicks(10)
            .damageConditions(KineticWeapon.condition(200, 1.0F, 0.5F))
            .knockbackConditions(KineticWeapon.condition(150, 1.0F, 0.5F))
            .dismountConditions(KineticWeapon.condition(150, 1.0F, 0.5F))
            .damageMultiplier(1.5F)
            .delayTicks(10)
            .forwardMovement(1.1F)
            .sound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_USE)!!)
            .hitSound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_HIT)!!)
            .build()
        )
        .setData(DataComponentTypes.ATTACK_RANGE,
            AttackRange.attackRange().hitboxMargin(0.2F).minReach(1.5F).maxReach(5.0F).build()
        )
        .setLore(lore)
        .build()

}