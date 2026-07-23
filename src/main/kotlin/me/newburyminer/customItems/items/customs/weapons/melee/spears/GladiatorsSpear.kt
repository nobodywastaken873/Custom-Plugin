package me.newburyminer.customItems.items.customs.weapons.melee.spears

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.AttackRange
import io.papermc.paper.datacomponent.item.KineticWeapon
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getArmorSet
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
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class GladiatorsSpear: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.GLADIATORS_SPEAR

    private val material = Material.NETHERITE_SPEAR
    private val color = arrayOf(204, 116, 2)
    private val name = text("Gladiator's Spear", color)
    private val lore = Utils.loreBlockToList(
        text("Spear-type weapon:", Utils.GRAY),
        text("Damage Multiplier: 1.85, Charge Delay: 6 ticks, Hitbox Margins: +0.4 blocks, Reach: 0.5-4.5 blocks", Utils.GRAY),
        text("Adds an additional 25% velocity to the Gladiator Set dash ability.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_DAMAGE, 6.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ATTACK_SPEED, -3.2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
        )
        .setData(DataComponentTypes.KINETIC_WEAPON, KineticWeapon.kineticWeapon()
            .contactCooldownTicks(10)
            .damageConditions(KineticWeapon.condition(200, 1.0F, 0.5F))
            .knockbackConditions(KineticWeapon.condition(150, 1.0F, 0.5F))
            .dismountConditions(KineticWeapon.condition(150, 1.0F, 0.5F))
            .damageMultiplier(1.57F)
            .delayTicks(6)
            .forwardMovement(0.0F)
            .sound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_USE)!!)
            .hitSound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_HIT)!!)
            .build()
        )
        .setData(DataComponentTypes.ATTACK_RANGE,
            AttackRange.attackRange().hitboxMargin(0.4F).minReach(0.5F).maxReach(4.5F).build()
        )
        .setLore(lore)
        .build()

}