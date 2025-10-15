package me.newburyminer.customItems.items.customs.weapons.arrows

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entities.CustomEntity
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import me.newburyminer.customItems.items.EventItemType
import org.bukkit.Material
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.LlamaSpit
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.inventory.ItemStack

class LlamaSpitArrow: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.LLAMA_SPIT_ARROW

    private val material = Material.ARROW
    private val color = arrayOf(217, 201, 176)
    private val name = text("Llama Spit Arrow", color)
    private val lore = Utils.loreBlockToList(
        text("Shoots a llama spit that does 1 true damage.", Utils.GRAY),
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
                val spit = e.entity.world.spawn(e.projectile.location, LlamaSpit::class.java)
                spit.velocity = e.projectile.velocity
                spit.shooter = player
                spit.setTag("id", CustomEntity.PLAYER_SHOT_PROJECTILE.id)
                spit.setTag("source", CustomItem.LLAMA_SPIT_ARROW.name)
                e.projectile.remove()
            }

            is ProjectileHitEvent -> {
                if (ctx.itemType != EventItemType.PROJECTILE) return
                if (e.hitEntity == null || e.hitEntity!! !is LivingEntity) return
                e.isCancelled = true
                val hit = e.hitEntity as LivingEntity
                hit.damage(1.5, DamageSource.builder(CustomDamageType.ALL_BYPASS).withDirectEntity(e.entity.shooter as Entity).withCausingEntity(e.entity.shooter as Entity).build())
                hit.noDamageTicks = 0
                e.entity.remove()
            }

        }

    }

}
