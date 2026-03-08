package me.newburyminer.customItems.items.customs.weapons.projectile

import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.crossbowProj
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.CustomDamageProjectile
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.damage.DamageType
import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
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

    init {
        register(EntityLoadCrossbowEvent::class, { e ->
            e.crossbow.isItem(custom)
        }, 
        {e ->
            e.isCancelled = true
            e.crossbow.crossbowProj(ItemStack(Material.ARROW), 2)
        })

        register(ProjectileLaunchEvent::class, { e ->
            activeRangedMatches(e, custom)
        },
        {e ->
            val arrow = e.entity as Arrow
            arrow.pierceLevel = 6
            EntityWrapperManager.getWrapperorNew(arrow).addComponent(CustomDamageProjectile(HitEffects(
                CustomDamageApply(17.0, DamageType.ARROW, overrideSource = e.entity.shooter as Entity?),
            )))
        })
    }

}