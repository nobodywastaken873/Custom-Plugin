package me.newburyminer.customItems.items.customs.weapons.projectile

import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import me.newburyminer.customItems.Utils.Companion.clearCrossbowProj
import me.newburyminer.customItems.Utils.Companion.crossbowProj
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entities.CustomEntity
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.block.Action
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CrossbowMeta

class RedstoneRepeater: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.REDSTONE_REPEATER

    private val material = Material.CROSSBOW
    private val color = arrayOf(125, 30, 30)
    private val name = text("Single Redstone Repeater - 0", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setTag("loading", false)
        .setTag("arrowcount", 1)
        .setTag("loadedarrows", 0)
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is ProjectileLaunchEvent -> {
                val shooter = ctx.player ?: return
                val crossbow = ctx.item ?: return
                if (crossbow.getTag<Boolean>("loading")!!) {e.isCancelled = true; return}
                if (crossbow.getTag<Int>("loadedarrows")!! < crossbow.getTag<Int>("arrowcount")!!) {e.isCancelled = true; return}
                crossbow.crossbowProj(ItemStack(Material.ARROW))
                crossbow.setTag("loadedarrows", crossbow.getTag<Int>("loadedarrows")!!-1)
                if (crossbow.getTag<Int>("loadedarrows")!! < crossbow.getTag<Int>("arrowcount")!!) {crossbow.setTag("loading", true); crossbow.clearCrossbowProj()}
                crossbow.name(text((if (crossbow.getTag<Int>("arrowcount") == 1) "Single" else "Double") + " Redstone Repeater - "+crossbow.getTag<Int>("loadedarrows").toString(), arrayOf(125, 30, 30), bold = true))
                e.entity.setTag("id", CustomEntity.PLAYER_SHOT_PROJECTILE.id)
                e.entity.setTag("source", CustomItem.REDSTONE_REPEATER.name)
            }

            is ProjectileHitEvent -> {
                if (e.hitEntity == null || e.hitEntity!! !is LivingEntity) return
                e.isCancelled = true
                val hit = e.hitEntity as LivingEntity
                hit.damage(10.5, DamageSource.builder(DamageType.ARROW).withDirectEntity(e.entity.shooter as Entity).withCausingEntity(e.entity.shooter as Entity).build())
                hit.noDamageTicks = 0
                e.entity.remove()
            }

            is PlayerSwapHandItemsEvent -> {
                val crossbow = ctx.item ?: return
                if (!e.player.isSneaking) return
                e.isCancelled = true
                crossbow.setTag("loading", !crossbow.getTag<Boolean>("loading")!!)
                if (!crossbow.getTag<Boolean>("loading")!!) {
                    crossbow.crossbowProj(ItemStack(Material.ARROW), crossbow.getTag<Int>("arrowcount")!!)
                } else {
                    crossbow.clearCrossbowProj()
                }
                CustomEffects.playSound(e.player.location, Sound.ITEM_CROSSBOW_QUICK_CHARGE_2, 1.0F, 1.2F)
            }

            is EntityLoadCrossbowEvent -> {
                val shooter = ctx.player ?: return
                val crossbow = ctx.item ?: return
                e.isCancelled = true
                if (crossbow.getTag<Int>("subshot") == (20 * (1.25 - 0.25 * (crossbow.enchantments[Enchantment.QUICK_CHARGE] ?: 0))).toInt()) {
                    var loadedArrows = crossbow.getTag<Int>("loadedarrows")!!
                    if (loadedArrows >= 8) {
                        CustomEffects.playSound(e.entity.location, Sound.ITEM_CROSSBOW_LOADING_MIDDLE, 1F, 0.7F)
                        loadedArrows = 10
                    } else loadedArrows += 2
                    crossbow.setTag("loadedarrows", loadedArrows)
                    crossbow.name(
                        text(
                            (if (crossbow.getTag<Int>("arrowcount") == 1) "Single" else "Double") + " Redstone Repeater - " + crossbow.getTag<Int>(
                                "loadedarrows"
                            ).toString(), arrayOf(125, 30, 30), bold = true
                        )
                    )
                    crossbow.setTag("subshot", 0)
                } else {
                    val subshot = crossbow.getTag<Int>("subshot") ?: 0
                    crossbow.setTag("subshot", subshot + 1)
                }
            }

            is PlayerInteractEvent -> {
                val item = ctx.item ?: return
                if (e.action != Action.LEFT_CLICK_BLOCK && e.action != Action.LEFT_CLICK_AIR) return
                if (!e.player.offCooldown(CustomItem.REDSTONE_REPEATER)) return
                item.setTag("arrowcount", if (item.getTag<Int>("arrowcount") == 1) 2 else 1)
                item.name(text((if (item.getTag<Int>("arrowcount") == 1) "Single" else "Double") + " Redstone Repeater - "+item.getTag<Int>("loadedarrows").toString(), arrayOf(125, 30, 30), bold = true))
                if (!item.getTag<Boolean>("loading")!!) {
                    val newMeta = item.itemMeta as CrossbowMeta
                    newMeta.setChargedProjectiles(null)
                    item.itemMeta = newMeta
                    item.crossbowProj(ItemStack(Material.ARROW), e.item!!.getTag<Int>("arrowcount")!!)
                }
                CustomEffects.playSound(e.player.location, Sound.ITEM_CROSSBOW_LOADING_START, 1.0F, 1.5F)
                e.player.setCooldown(CustomItem.REDSTONE_REPEATER, 0.5)
            }

        }

    }

}