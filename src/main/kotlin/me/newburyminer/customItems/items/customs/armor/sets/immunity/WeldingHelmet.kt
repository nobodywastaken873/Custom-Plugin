package me.newburyminer.customItems.items.customs.armor.sets.immunity

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.setArmorSet
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import me.newburyminer.customItems.items.EventItemType
import me.newburyminer.customItems.items.armorsets.ArmorSet
import org.bukkit.Material
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType

class WeldingHelmet: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.WELDING_HELMET

    private val material = Material.NETHERITE_HELMET
    private val color = arrayOf(94, 92, 71)
    private val name = text("Welding Helmet", color)
    private val lore = Utils.loreBlockToList(
        text("Gain permanent immunity to blindness, nausea, and darkness.", Utils.GRAY),
        text(""),
        text("Full Set Bonus (4 pieces): Immunity Set", Utils.GRAY),
        text("Upon receiving any negative effect, convert it into the corresponding positive effect with potency increased by 1 level and triple the duration.", Utils.GRAY)
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .attr("ARM+5.0HE","ART+4.0HE","MAH+4.0HE","ATD+1.0HE")
        .setArmorSet(ArmorSet.IMMUNITY)

    override fun handle(ctx: EventContext) {
        when (val e = ctx.event) {

            is EntityPotionEffectEvent -> {
                if (ctx.itemType != EventItemType.HELMET) return
                val player = ctx.player ?: return
                if (e.action != EntityPotionEffectEvent.Action.ADDED && e.action != EntityPotionEffectEvent.Action.CHANGED) return
                if (e.newEffect!!.type in arrayOf(PotionEffectType.NAUSEA, PotionEffectType.BLINDNESS, PotionEffectType.DARKNESS))
                    e.isCancelled = true
            }

        }
    }

}
