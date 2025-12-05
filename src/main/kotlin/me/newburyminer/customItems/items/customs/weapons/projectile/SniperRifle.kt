package me.newburyminer.customItems.items.customs.weapons.projectile

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.CustomDamageProjectile
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.entity3.CustomEntity
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.Arrow
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.inventory.ItemStack

class SniperRifle: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.SNIPER_RIFLE
    private val material = Material.CROSSBOW
    private val color = arrayOf(52, 69, 54)
    private val name = text("Sniper Rifle", color)
    private val lore = Utils.loreBlockToList(
        text("Shoots an extremely high velocity arrow that goes almost exactly straight, and does 13 true damage on hit, with a 50 second cooldown.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is ProjectileLaunchEvent -> {
                val shooter = ctx.player ?: return
                var crossbow = ctx.item ?: return
                if (!shooter.offCooldown(CustomItem.SNIPER_RIFLE)) {e.isCancelled = true; return}

                e.entity.velocity = shooter.location.direction.normalize().multiply(50)
                shooter.setCooldown(CustomItem.SNIPER_RIFLE, 50.0)

                EntityWrapperManager.getWrapperorNew(e.entity).addComponent(CustomDamageProjectile(HitEffects(
                    CustomDamageApply(13.0, CustomDamageType.ALL_BYPASS, 0, overrideSource = shooter)
                )))
            }

        }

    }

}