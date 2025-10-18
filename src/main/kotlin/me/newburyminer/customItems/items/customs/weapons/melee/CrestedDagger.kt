package me.newburyminer.customItems.items.customs.weapons.melee

import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class CrestedDagger: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.CRESTED_DAGGER

    private val material = Material.NETHERITE_SWORD
    private val color = arrayOf(155, 165, 168)
    private val name = text("Crested Dagger", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_SPEED, -2.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ENTITY_INTERACTION_RANGE, -1.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
        )
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (ctx.itemType != EventItemType.MAINHAND) return
                val damager = e.damager as? Player ?: return
                val damaged = e.entity as? LivingEntity ?: return
                // prevent infinite looping with damage
                if (e.damageSource.damageType == DamageType.STARVE) return
                e.isCancelled = true
                val damage = 20.0 / 13 * (e.damager as Player).attackCooldown
                damaged.damage(damage, DamageSource.builder(DamageType.STARVE).withDirectEntity(e.damager).withCausingEntity(e.damager).build())
                damaged.noDamageTicks = 5
            }

        }

    }

}