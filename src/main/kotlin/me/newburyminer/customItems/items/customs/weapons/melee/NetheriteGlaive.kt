package me.newburyminer.customItems.items.customs.weapons.melee

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.AttackRange
import io.papermc.paper.datacomponent.item.KineticWeapon
import io.papermc.paper.datacomponent.item.PiercingWeapon
import io.papermc.paper.datacomponent.item.SwingAnimation
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.effects.AttributeData
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.SimpleModifier
import org.bukkit.Material
import org.bukkit.Registry
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class NetheriteGlaive: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.NETHERITE_GLAIVE

    private val material = Material.NETHERITE_SPEAR
    private val color = arrayOf(10, 242, 234)
    private val name = text("Netherite Glaive", color)
    private val lore = Utils.loreBlockToList(
        text("Spear-type weapon. Slightly decreased range, less minimum velocity required compared to a normal spear. Increased damage multiplier.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_DAMAGE, 6.2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ATTACK_SPEED, -3.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
        )
        .setData(DataComponentTypes.KINETIC_WEAPON, KineticWeapon.kineticWeapon()
            .contactCooldownTicks(10)
            .damageConditions(KineticWeapon.condition(250, 0.5F, 0.25F))
            .knockbackConditions(KineticWeapon.condition(200, 0.5F, 0.25F))
            .dismountConditions(KineticWeapon.condition(200, 0.5F, 0.25F))
            .damageMultiplier(1.25F)
            .delayTicks(10)
            .forwardMovement(0.0F)
            .sound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_USE)!!)
            .hitSound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_HIT)!!)
            .build()
        )
        .setData(DataComponentTypes.ATTACK_RANGE,
            AttackRange.attackRange().hitboxMargin(0.32F).minReach(0.5F).maxReach(4.0F).build()
        )
        .setLore(lore)
        .build()

}