package me.newburyminer.customItems.items.customs.armor.sets.tank

import io.papermc.paper.event.entity.EntityMoveEvent
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.getCustom
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setArmorSet
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.items.armorsets.ArmorSet
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.entity.EntityDismountEvent
import org.bukkit.event.entity.EntityMountEvent
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class EncrustedPants: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ENCRUSTED_PANTS

    private val material = Material.NETHERITE_LEGGINGS
    private val color = arrayOf(130, 61, 14)
    private val name = text("Encrusted Pants", color)
    private val lore = Utils.loreBlockToList(
        text("All mobs in a 40 block radius will aggro on you instead of any other player.", Utils.GRAY),
        text(""),
        text("Full Set Bonus (4 pieces): Tank Set", Utils.GRAY),
        text("Sneak to gain 10 absorption hearts, with a 60 second cooldown.", Utils.GRAY)
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .attr("ARM+10.0LE","ART+5.0LE","KNR+0.3LE")
        .setArmorSet(ArmorSet.TANK)

    override fun handle(ctx: EventContext) {
        when (val e = ctx.event) {

            is EntityTargetEvent -> {
                if (ctx.itemType != EventItemType.LEGGINGS) return
                val item = ctx.item ?: return
                val player = ctx.player ?: return
                e.target = player
            }

        }
    }

}
