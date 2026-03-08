package me.newburyminer.customItems.items.customs.weapons.arrows

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
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

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(EntityShootBowEvent::class, {e ->
            e.consumable.isItem(custom)
        },
        {e ->
            e.entity.world.spawn(e.projectile.location, FallingBlock::class.java) {
                it.blockData = Material.DRIPSTONE_BLOCK.createBlockData()
                it.maxDamage = 40
                it.damagePerBlock = 10F
                it.setHurtEntities(true)
                it.fallDistance = 100F
                it.velocity = e.projectile.velocity
            }
            e.projectile.remove()
        })
    }

}