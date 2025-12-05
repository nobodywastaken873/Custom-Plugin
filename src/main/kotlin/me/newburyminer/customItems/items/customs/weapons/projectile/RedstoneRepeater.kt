package me.newburyminer.customItems.items.customs.weapons.projectile

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.clearCrossbowProj
import me.newburyminer.customItems.Utils.Companion.crossbowProj
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.name
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

class RedstoneRepeater: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.REDSTONE_REPEATER

    private val material = Material.CROSSBOW
    private val color = arrayOf(125, 30, 30)
    private val name = text("Single Redstone Repeater - 0", color)
    private val lore = Utils.loreBlockToList(
        text("Sneak and swap hands to swap between loading and shooting mode. In loading mode, hold right click to load arrows, which will load two at a time, Quick Charge will speed it up. When shooting, left click to cycle between 1 and 2 shots loaded at a time. All projectiles ignore immunity frames.", Utils.GRAY)
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

                val isLoading = crossbow.getTag<Boolean>("loading") ?: true
                var loadedArrows = crossbow.getTag<Int>("loadedarrows") ?: 0
                val arrowCount = crossbow.getTag<Int>("arrowcount") ?: 0

                if (isLoading) {e.isCancelled = true; return}
                if (loadedArrows < arrowCount) {e.isCancelled = true; return}

                if (crossbow.getData(DataComponentTypes.CHARGED_PROJECTILES)?.projectiles()?.size == 1)
                    crossbow.crossbowProj(ItemStack(Material.ARROW), 2)
                else
                    crossbow.crossbowProj(ItemStack(Material.ARROW))
                crossbow.setTag("loadedarrows", loadedArrows - 1)
                loadedArrows -= 1

                // updated logic, hopefully prevents negative arrow counts
                if (loadedArrows == 0) {
                    crossbow.setTag("loading", true)
                    crossbow.clearCrossbowProj()
                }

                crossbow.name(text((if (arrowCount == 1) "Single" else "Double") + " Redstone Repeater - " + loadedArrows.toString(), arrayOf(125, 30, 30), bold = true))

                EntityWrapperManager.getWrapperorNew(e.entity).addComponent(CustomDamageProjectile(HitEffects(
                    CustomDamageApply(10.5, DamageType.ARROW, 0, overrideSource = shooter),
                )))
            }

            is PlayerSwapHandItemsEvent -> {
                val crossbow = ctx.item ?: return
                if (!e.player.isSneaking) return
                e.isCancelled = true

                var isLoading = crossbow.getTag<Boolean>("loading") ?: true
                val arrowCount = crossbow.getTag<Int>("arrowcount") ?: 0

                crossbow.setTag("loading", !isLoading)
                isLoading = !isLoading

                if (!isLoading) {
                    crossbow.crossbowProj(ItemStack(Material.ARROW), arrowCount)
                } else {
                    crossbow.clearCrossbowProj()
                }
                CustomEffects.playSoundToPlayer(e.player, Sound.ITEM_CROSSBOW_QUICK_CHARGE_2, 1.0F, 1.2F)
            }

            is EntityLoadCrossbowEvent -> {
                val shooter = ctx.player ?: return
                val crossbow = ctx.item ?: return
                e.isCancelled = true
                if (crossbow.getTag<Int>("subshot") == (20 * (1.25 - 0.25 * (crossbow.enchantments[Enchantment.QUICK_CHARGE] ?: 0))).toInt()) {
                    var loadedArrows = crossbow.getTag<Int>("loadedarrows") ?: 0
                    if (loadedArrows >= 8) {
                        CustomEffects.playSoundToPlayer(shooter, Sound.ITEM_CROSSBOW_LOADING_MIDDLE, 1F, 0.7F)
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

                val isLoading = item.getTag<Boolean>("loading") ?: true
                val loadedArrows = item.getTag<Int>("loadedarrows") ?: 0
                var arrowCount = item.getTag<Int>("arrowcount") ?: 0

                item.setTag("arrowcount", if (arrowCount == 1) 2 else 1)
                arrowCount = if (arrowCount == 1) 2 else 1

                item.name(text((if (arrowCount == 1) "Single" else "Double") + " Redstone Repeater - " + loadedArrows.toString(), arrayOf(125, 30, 30), bold = true))
                if (!isLoading) {
                    item.clearCrossbowProj()
                    item.crossbowProj(ItemStack(Material.ARROW), arrowCount)
                }
                CustomEffects.playSoundToPlayer(e.player, Sound.ITEM_CROSSBOW_LOADING_START, 1.0F, 1.5F)
                e.player.setCooldown(CustomItem.REDSTONE_REPEATER, 0.5)
            }

        }

    }

}