package me.newburyminer.customItems.items.customs.weapons.projectile

import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.crossbowProj
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.pushOut
import me.newburyminer.customItems.Utils.Companion.reduceDura
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entities.CustomEntity
import me.newburyminer.customItems.helpers.AttributeManager.Companion.tempAttribute
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import me.newburyminer.customItems.items.EventItemType
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.*
import org.bukkit.event.block.Action
import org.bukkit.event.entity.*
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import kotlin.math.abs

class DualBarreledCrossbow: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.DUAL_BARRELED_CROSSBOW

    private val material = Material.CROSSBOW
    private val color = arrayOf(117, 42, 2)
    private val name = text("Dual-barreled Crossbow", color)
    private val lore = Utils.loreBlockToList(
        text("Shoots two arrows that can pierce through 5 mobs each. Does the same damage as a Power 5 bow.", Utils.GRAY),
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()

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