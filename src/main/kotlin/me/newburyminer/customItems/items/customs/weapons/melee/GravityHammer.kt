package me.newburyminer.customItems.items.customs.weapons.melee

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.AttributeManager.Companion.tempAttribute
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import me.newburyminer.customItems.items.EventItemType
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class GravityHammer: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.GRAVITY_HAMMER

    private val material = Material.NETHERITE_AXE
    private val color = arrayOf(55, 46, 66)
    private val name = text("Gravity Hammer", color)
    private val lore = Utils.loreBlockToList(
        text("On a fully charged hit, increase your opponent's gravity significantly for 7 seconds, with a 20 second cooldown.", Utils.GRAY)
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .attr("ATS-3.3MA", "ATD+15.0MA")
        .loreList(lore)
        .cleanAttributeLore()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (ctx.itemType != EventItemType.MAINHAND) return
                val damager = e.damager as? Player ?: return
                val damaged = e.entity as? Player ?: return
                if (!damager.offCooldown(CustomItem.GRAVITY_HAMMER)) return
                if (damager.attackCooldown.toDouble() != 1.0) return
                CustomEffects.playSound(damaged.location, Sound.ITEM_MACE_SMASH_AIR, 1.0F, 1.2F)
                damaged.tempAttribute(Attribute.GRAVITY, AttributeModifier(NamespacedKey(CustomItems.plugin, "abc"), 2.0, AttributeModifier.Operation.MULTIPLY_SCALAR_1), 7.0, "a")
                damager.setCooldown(CustomItem.GRAVITY_HAMMER, 20.0)
            }

        }

    }

}