package me.newburyminer.customItems.items.customs.weapons.melee

import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.reduceDura
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.*
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class HeavyGreathammer: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.HEAVY_GREATHAMMER

    private val material = Material.NETHERITE_AXE
    private val color = arrayOf(87, 75, 62)
    private val name = text("Heavy Greathammer", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_SPEED, -3.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ATTACK_DAMAGE, 16.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
        )
        .setLore(lore)
        .setTag("criticalcount", 0)
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (ctx.itemType != EventItemType.MAINHAND) return
                val damager = e.damager as? Player ?: return
                val item = ctx.item ?: return
                if (!e.isCritical) return
                item.setTag("criticalcount", item.getTag<Int>("criticalcount")!!+1)
                if (item.getTag<Int>("criticalcount")!! % 3 != 0) return
                e.damage *= 2
                CustomEffects.playSound(e.entity.location, Sound.BLOCK_CALCITE_BREAK, 1.5F, 0.7F)
                CustomEffects.particle(Particle.CRIMSON_SPORE.builder(), e.entity.location, 20, 0.5, 0.5)
                if (e.entity !is Player) return
                for (armor in (e.entity as Player).inventory.armorContents) {
                    if (armor?.itemMeta?.isUnbreakable != true) armor?.reduceDura(10)
                }
            }

        }

    }

}