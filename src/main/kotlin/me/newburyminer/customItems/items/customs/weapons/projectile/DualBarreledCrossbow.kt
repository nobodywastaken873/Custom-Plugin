package me.newburyminer.customItems.items.customs.weapons.projectile

import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.crossbowProj
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entities.CustomEntity
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.entity.Arrow
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.inventory.ItemStack

class DualBarreledCrossbow: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.DUAL_BARRELED_CROSSBOW

    private val material = Material.CROSSBOW
    private val color = arrayOf(117, 42, 2)
    private val name = text("Dual-barreled Crossbow", color)
    private val lore = Utils.loreBlockToList(
        text("Shoots two arrows that can pierce through 5 mobs each. Does the same damage as a Power 5 bow.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is ProjectileLaunchEvent -> {
                val shooter = ctx.player ?: return
                val crossbow = ctx.item ?: return
                e.entity.setTag("id", CustomEntity.PLAYER_SHOT_PROJECTILE.id)
                e.entity.setTag("source", CustomItem.DUAL_BARRELED_CROSSBOW.name)
                val arrow = e.entity as Arrow
                arrow.pierceLevel = 6
            }

            is EntityDamageByEntityEvent -> {
                if (e.damager !is Arrow) return
                if (e.entity !is LivingEntity) return
                e.damage = 17.0
            }

            is EntityLoadCrossbowEvent -> {
                if (e.entity !is Player) return
                val shooter = e.entity as Player
                e.isCancelled = true
                e.crossbow.crossbowProj(ItemStack(Material.ARROW), 2)
            }

        }

    }

}