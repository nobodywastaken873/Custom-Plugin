package me.newburyminer.customItems.items.customs.weapons.projectile

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
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
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Arrow
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

class WindHook: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.WIND_HOOK

    private val material = Material.BOW
    private val color = arrayOf(211, 195, 219)
    private val name = text("Wind Hook", color)
    private val lore = Utils.loreBlockToList(
        text("Shoot to launch a hook that pulls you in upon landing.", Utils.GRAY)
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
                val item = ctx.item ?: return
                if (!shooter.offCooldown(CustomItem.WIND_HOOK)) {e.isCancelled = true; return}
                e.entity.setTag("id", CustomEntity.PLAYER_SHOT_PROJECTILE.id)
                e.entity.setTag("source", CustomItem.WIND_HOOK.name)
                (e.entity as Arrow).color = Color.fromRGB(211, 195, 219)
                shooter.setCooldown(CustomItem.WIND_HOOK, 15.0)
                shooter.stopSound(Sound.ENTITY_ARROW_SHOOT)
                CustomEffects.playSound(shooter.location, Sound.ENTITY_BREEZE_JUMP, 1F, 0.8F)
            }

            is ProjectileHitEvent -> {
                val arrow = e.entity as Arrow
                arrow.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
                val shooter = e.entity.shooter as Player
                shooter.setTag("windhookpullcoords", "${e.entity.location.x},${e.entity.location.y},${e.entity.location.z}")
                shooter.setTag("windhookpulltime", 50)
            }

        }

    }

}