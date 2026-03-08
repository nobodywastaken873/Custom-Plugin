package me.newburyminer.customItems.items.customs.weapons.arrows

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.CustomDamageProjectile
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.entity.LlamaSpit
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.inventory.ItemStack

class LlamaSpitArrow: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.LLAMA_SPIT_ARROW

    private val material = Material.ARROW
    private val color = arrayOf(217, 201, 176)
    private val name = text("Llama Spit Arrow", color)
    private val lore = Utils.loreBlockToList(
        text("Shoots a llama spit that does 1.5 true damage with no iframes.", Utils.GRAY),
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
            val spit = e.entity.world.spawn(e.projectile.location, LlamaSpit::class.java) {
                it.velocity = e.projectile.velocity
                it.shooter = e.entity
            }
            EntityWrapperManager.getWrapperorNew(spit).addComponent(CustomDamageProjectile(HitEffects(
                CustomDamageApply(1.5, CustomDamageType.ALL_BYPASS, 0, e.entity),
                VanillaKnockbackApply(0.1)
            )))
            e.projectile.remove()
        })
    }

}