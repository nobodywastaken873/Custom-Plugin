package me.newburyminer.customItems.items.customs.weapons.projectile

import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entities.CustomEntity
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.damage.DamageSource
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
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is ProjectileLaunchEvent -> {
                val shooter = ctx.player ?: return
                var crossbow = ctx.item ?: return
                if (!shooter.offCooldown(CustomItem.SNIPER_RIFLE)) {e.isCancelled = true; return}

                e.entity.velocity = shooter.location.direction.normalize().multiply(50)
                shooter.setCooldown(CustomItem.SNIPER_RIFLE, 40.0)
                e.entity.setTag("id", CustomEntity.PLAYER_SHOT_PROJECTILE.id)
                e.entity.setTag("source", CustomItem.SNIPER_RIFLE.name)
            }

            is EntityDamageByEntityEvent -> {
                if (e.damager !is Arrow) return
                if (e.entity !is LivingEntity) return
                if (e.damageSource.damageType == CustomDamageType.ALL_BYPASS) return
                e.isCancelled = true
                val damage = 13.0
                (e.entity as LivingEntity).damage(damage,
                    DamageSource.builder(CustomDamageType.ALL_BYPASS)
                        .withDirectEntity(e.damageSource.directEntity ?: e.damageSource.causingEntity!!)
                        .withCausingEntity(e.damageSource.causingEntity ?: e.damageSource.directEntity!!).build()
                )
                e.damager.remove()
            }


        }

    }

}