package me.newburyminer.customItems.items.customs.weapons.melee.swords

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.SimpleModifier
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class HungeringKatana: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.HUNGERING_KATANA

    private val material = Material.IRON_SWORD
    private val color = arrayOf(15, 94, 7)
    private val name = text("Hungering Katana", color)
    private val lore = Utils.loreBlockToList(
        text("On a fully charged hit, steal 1 saturation point from your opponent. If they do not have saturation, their hunger will be reduced instead. Works on mobs.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_DAMAGE, 9.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ATTACK_SPEED, -2.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
        )
        .setLore(lore)
        .build()

    init {
        register(EntityDamageByEntityEvent::class, { e ->
            slotMatches(e, EquipmentSlot.HAND, custom) &&
            e.damage >= 10.0
        },
        {e ->
            val damager = e.damager as Player
            val damaged = e.entity

            if (damaged is Player) {
                damager.playSound(damager.location, Sound.ENTITY_GENERIC_EAT, 0.4F, 1.7F)

                if (damaged.saturation > 0.0) {
                    damaged.saturation = (damaged.saturation - 1.0).coerceAtLeast(0.0).toFloat()
                }
                else if (damaged.foodLevel >= 1) {
                    damaged.foodLevel -= 1
                }
            }

            damager.saturation = (damager.saturation + 1.0).coerceAtMost(20.0).toFloat()
        })
    }

}