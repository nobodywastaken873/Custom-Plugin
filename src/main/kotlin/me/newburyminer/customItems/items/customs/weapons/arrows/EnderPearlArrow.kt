package me.newburyminer.customItems.items.customs.weapons.arrows

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.entity.EnderPearl
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.inventory.ItemStack

class EnderPearlArrow: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ENDER_PEARL_ARROW

    private val material = Material.ARROW
    private val color = arrayOf(38, 118, 133)
    private val name = text("Ender Pearl Arrow", color)
    private val lore = Utils.loreBlockToList(
        text("Shoots an ender pearl.", Utils.GRAY),
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
            e.entity.world.spawn(e.projectile.location, EnderPearl::class.java) {
                it.velocity = e.projectile.velocity
                it.shooter = e.entity
            }
            e.projectile.remove()
        })
    }

}