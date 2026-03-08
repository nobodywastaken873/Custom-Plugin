package me.newburyminer.customItems.items.customs.weapons.arrows

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
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

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(EntityShootBowEvent::class, {e ->
            e.consumable.isItem(custom)
        },
        {e ->
            e.entity.world.spawn(e.projectile.location, WitherSkull::class.java) {
                it.velocity = e.projectile.velocity
                it.shooter = e.entity
            }
            e.projectile.remove()
        })
    }

}