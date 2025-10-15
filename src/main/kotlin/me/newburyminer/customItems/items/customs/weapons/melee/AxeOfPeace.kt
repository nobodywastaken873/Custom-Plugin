package me.newburyminer.customItems.items.customs.weapons.melee

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import me.newburyminer.customItems.items.EventItemType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.inventory.ItemStack

class AxeOfPeace: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.AXE_OF_PEACE

    private val material = Material.NETHERITE_AXE
    private val color = arrayOf(117, 2, 4)
    private val name = text("Axe of Peace", color)
    private val lore = Utils.loreBlockToList(
        text("Heals you for 0.75 health on a fully charged hit.", Utils.GRAY)
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .attr("ATS-3.2MA", "ATD+15MA", "ENI-0.4MA")
        .loreList(lore)
        .cleanAttributeLore()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (ctx.itemType != EventItemType.MAINHAND) return
                val damager = e.damager as? Player ?: return
                if (e.damage >= 15) damager.heal(1.5, EntityRegainHealthEvent.RegainReason.REGEN)
            }

        }

    }

}