package me.newburyminer.customItems.items.customs.weapons.projectile

import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import me.newburyminer.customItems.Utils.Companion.clearCrossbowProj
import me.newburyminer.customItems.Utils.Companion.crossbowProj
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.name
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
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack

class MultiloadShotgun: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.MULTI_LOAD_CROSSBOW

    private val material = Material.CROSSBOW
    private val color = arrayOf(214, 125, 0)
    private val name = text("Multi-load Shotgun", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setTag("loadedshot", 0)
        .setTag("loading", true)
        .build()

    override fun handle(ctx: EventContext) {

        when (val e = ctx.event) {

            is ProjectileLaunchEvent -> {
                val shooter = ctx.player ?: return
                val crossbow = ctx.item ?: return
                crossbow.setTag("loadedshot", 0)
                crossbow.name(text("Multi-load Shotgun - "+crossbow.getTag<Int>("loadedshot").toString(), arrayOf(214, 125, 0), bold = true))
                crossbow.setTag("loading", true)
                e.entity.setTag("id", CustomEntity.PLAYER_SHOT_PROJECTILE.id)
                e.entity.setTag("source", CustomItem.MULTI_LOAD_CROSSBOW.name)
            }

            is ProjectileHitEvent -> {
                if (e.hitEntity == null || e.hitEntity!! !is LivingEntity) return
                e.isCancelled = true
                val hit = e.hitEntity as LivingEntity
                hit.damage(10.0, DamageSource.builder(DamageType.ARROW).withDirectEntity(e.entity.shooter as Entity).withCausingEntity(e.entity.shooter as Entity).build())
                hit.noDamageTicks = 0
                e.entity.remove()
            }

            is PlayerSwapHandItemsEvent -> {
                val crossbow = ctx.item ?: return
                if (!e.player.isSneaking) return
                e.isCancelled = true
                if (crossbow.getTag<Int>("loadedshot")!! > 0) crossbow.setTag("loading", !crossbow.getTag<Boolean>("loading")!!)
                else {crossbow.setTag("loading", true); CustomEffects.playSound(e.player.location, Sound.BLOCK_ANVIL_PLACE, 1.0F, 1.2F); return}
                if (!crossbow.getTag<Boolean>("loading")!!) {
                    crossbow.crossbowProj(ItemStack(Material.ARROW), crossbow.getTag<Int>("loadedshot")!!)
                } else {
                    crossbow.clearCrossbowProj()
                }
                CustomEffects.playSound(e.player.location, Sound.ITEM_CROSSBOW_QUICK_CHARGE_2, 1.0F, 1.2F)
            }

            is EntityLoadCrossbowEvent -> {
                val player = ctx.player ?: return
                val crossbow = ctx.item ?: return
                e.isCancelled = true
                if (crossbow.getTag<Int>("subshot") == (20 * (1.25 - 0.25 * (crossbow.enchantments[Enchantment.QUICK_CHARGE] ?: 0))).toInt()) {
                    var loadedArrows = crossbow.getTag<Int>("loadedshot")!!
                    if (loadedArrows >= 24) {
                        CustomEffects.playSound(e.entity.location, Sound.ITEM_CROSSBOW_LOADING_MIDDLE, 1F, 0.7F)
                        loadedArrows = 25
                    } else loadedArrows += 1
                    crossbow.setTag("loadedshot", loadedArrows)
                    crossbow.name(
                        text(
                            "Multi-load Shotgun - " + crossbow.getTag<Int>("loadedshot").toString(),
                            arrayOf(214, 125, 0), bold = true
                        )
                    )
                    crossbow.setTag("subshot", 0)
                } else {
                    val subshot = crossbow.getTag<Int>("subshot") ?: 0
                    crossbow.setTag("subshot", subshot + 1)
                }
                //shooter.sendMessage(Bukkit.getServer().currentTick.toString())
                /*
                e.entity.setTag("tempcrossbow", crossbow.clone())


                val slot = (e.entity as Player).inventory.heldItemSlot
                if (offhand) {
                    (e.entity as Player).inventory.setItemInOffHand(null)
                } else {
                    (e.entity as Player).inventory.setItem(slot, null)
                }

                Bukkit.getScheduler().runTaskLater(CustomItems.plugin, Runnable {
                    if (offhand && (e.entity as Player).inventory.itemInOffHand.type == Material.AIR) {
                        (e.entity as Player).inventory.setItemInOffHand(e.entity.getTag<ItemStack>("tempcrossbow"))
                    } else if ((e.entity as Player).inventory.getItem(slot) == null) {
                        (e.entity as Player).inventory.setItem(slot, e.entity.getTag<ItemStack>("tempcrossbow"))
                    } else {
                        (e.entity as Player).addItemorDrop(e.entity.getTag<ItemStack>("tempcrossbow")!!)
                    }
                }, 1L)*/
            }



        }

    }

}