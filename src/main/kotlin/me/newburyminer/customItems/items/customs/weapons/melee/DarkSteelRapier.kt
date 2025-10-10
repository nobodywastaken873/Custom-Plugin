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
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import me.newburyminer.customItems.items.EventItemType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class DarkSteelRapier: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.DARK_STEEL_RAPIER

    private val material = Material.IRON_SWORD
    private val color = arrayOf(74, 98, 125)
    private val name = text("Dark Steel Rapier", color)
    private val lore = Utils.loreBlockToList(text("Cannot perform critical hits. Right click to gain 60% increased speed for 15 seconds and blind players within 10 blocks of you for 8 seconds, 40 second cooldown.", Utils.GRAY))

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .attr("ATS-2.0MA", "ATD+9.0MA", "MOS+0.01MA")
        .loreList(lore)
        .cleanAttributeLore()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (ctx.itemType != EventItemType.MAINHAND) return
                if (e.damager !is Player) return
                if (!(e.damager as Player).inventory.itemInMainHand.isItem(CustomItem.DARK_STEEL_RAPIER)) return
                if (!e.isCritical) return
                e.damage *= 0.66
            }

            is PlayerInteractEvent -> {
                if (!ctx.itemType.isHand()) return
                val item = ctx.item ?: return
                if (!item.offCooldown(e.player)) return
                if (e.action != Action.RIGHT_CLICK_BLOCK && e.action != Action.RIGHT_CLICK_AIR) return
                item.setCooldown(e.player, 40.0)
                for (player in e.player.location.getNearbyPlayers(10.0)) {
                    if (e.player == player) continue
                    player.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 160, 0))
                }
                e.player.tempAttribute(Attribute.MOVEMENT_SPEED, AttributeModifier(NamespacedKey(CustomItems.plugin, "abc"), 0.06, AttributeModifier.Operation.ADD_NUMBER), 15.0, "darksteelsword")
            }

        }

    }

}