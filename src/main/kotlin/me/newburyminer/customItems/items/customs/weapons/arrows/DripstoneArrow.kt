package me.newburyminer.customItems.items.customs.weapons.arrows

import me.newburyminer.customItems.Utils
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
import org.bukkit.entity.FallingBlock
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.inventory.ItemStack

class DripstoneArrow: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.DRIPSTONE_ARROW

    private val material = Material.ARROW
    private val color = arrayOf(194, 167, 95)
    private val name = text("Dripstone Arrow", color)
    private val lore = Utils.loreBlockToList(
        text("Shoots a high-damage dripstone projectile.", Utils.GRAY),
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is EntityShootBowEvent -> {
                if (ctx.itemType != EventItemType.PROJECTILE) return
                val player = ctx.player ?: return
                val item = ctx.item ?: return
                val dripstone = e.entity.world.spawn(e.projectile.location, FallingBlock::class.java)
                dripstone.blockData = Material.DRIPSTONE_BLOCK.createBlockData()
                dripstone.maxDamage = 40
                dripstone.damagePerBlock = 10F
                dripstone.setHurtEntities(true)
                dripstone.fallDistance = 100F
                dripstone.velocity = e.projectile.velocity
                e.projectile.remove()
            }

        }

    }

}
