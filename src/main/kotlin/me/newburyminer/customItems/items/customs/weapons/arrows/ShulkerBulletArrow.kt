package me.newburyminer.customItems.items.customs.weapons.arrows

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import org.bukkit.Material
import org.bukkit.entity.ShulkerBullet
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class ShulkerBulletArrow: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.SHULKER_BULLET_ARROW

    private val material = Material.ARROW
    private val color = arrayOf(184, 140, 209)
    private val name = text("Shulker Bullet Arrow", color)
    private val lore = Utils.loreBlockToList(
        text("Shoots a shulker bullet.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(EntityShootBowEvent::class, { e ->
            e.consumable.isItem(custom)
        },
        {e ->
            e.entity.world.spawn(e.projectile.location, ShulkerBullet::class.java) {
                it.velocity = e.projectile.velocity
                it.shooter = e.entity
            }
            e.projectile.remove()
        })
    }

    /*override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is EntityShootBowEvent -> {
                if (ctx.itemType != EventItemType.PROJECTILE) return
                val player = ctx.player ?: return
                val item = ctx.item ?: return
                val bullet = e.entity.world.spawn(e.projectile.location, ShulkerBullet::class.java)
                bullet.velocity = e.projectile.velocity
                bullet.shooter = player
                e.projectile.remove()
            }

        }

    }*/

}