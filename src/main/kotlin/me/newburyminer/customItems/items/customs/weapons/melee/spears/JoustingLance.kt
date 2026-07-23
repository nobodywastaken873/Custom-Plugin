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

class JoustingLance: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.JOUSTING_LANCE

    private val material = Material.NETHERITE_SPEAR
    private val color = arrayOf(164, 198, 235)
    private val name = text("Jousting Lance", color)
    private val lore = Utils.loreBlockToList(
        text("Spear-type weapon:", Utils.GRAY),
        text("Damage Multiplier: 1.80, Charge Delay: 8 ticks, Hitbox Margins: +0.1 blocks, Reach: 1.0-5.5 blocks", Utils.GRAY),
        text("Deals 15% increased damage if you are riding a mob.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_DAMAGE, 6.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ATTACK_SPEED, -3.2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
        )
        .setData(DataComponentTypes.KINETIC_WEAPON, KineticWeapon.kineticWeapon()
            .contactCooldownTicks(10)
            .damageConditions(KineticWeapon.condition(200, 1.0F, 0.5F))
            .knockbackConditions(KineticWeapon.condition(150, 1.0F, 0.5F))
            .dismountConditions(KineticWeapon.condition(150, 1.0F, 0.5F))
            .damageMultiplier(1.53F)
            .delayTicks(8)
            .forwardMovement(0.6F)
            .sound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_USE)!!)
            .hitSound(Registry.SOUNDS.getKey(Sound.ITEM_SPEAR_HIT)!!)
            .build()
        )
        .setData(DataComponentTypes.ATTACK_RANGE,
            AttackRange.attackRange().hitboxMargin(0.1F).minReach(1.0F).maxReach(5.5F).build()
        )
        .setLore(lore)
        .build()

    init {
        register(EntityDamageByEntityEvent::class, { e ->
            e.damager is Player &&
            slotMatches(e, EquipmentSlot.HAND, custom) &&
            e.damageSource.damageType == DamageType.SPEAR
        },
        {e ->
            val player = e.damager as Player
            val vehicle = player.vehicle ?: return@register
            if (vehicle !is LivingEntity) return@register

            e.damage *= 1.15
        })
    }

}