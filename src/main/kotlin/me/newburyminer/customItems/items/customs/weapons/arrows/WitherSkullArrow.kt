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
import org.bukkit.entity.WitherSkull
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.inventory.ItemStack

class WitherSkullArrow: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.WITHER_SKULL_ARROW

    private val material = Material.ARROW
    private val color = arrayOf(51, 41, 69)
    private val name = text("Wither Skull Arrow", color)
    private val lore = Utils.loreBlockToList(
        text("Shoots a wither skull.", Utils.GRAY),
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
                val skull = e.entity.world.spawn(e.projectile.location, WitherSkull::class.java)
                skull.velocity = e.projectile.velocity
                skull.shooter = player
                e.projectile.remove()
            }

        }

    }

}
