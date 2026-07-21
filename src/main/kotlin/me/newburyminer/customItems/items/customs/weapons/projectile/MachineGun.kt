package me.newburyminer.customItems.items.customs.weapons.projectile

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Consumable
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.projectiles.CustomDamageProjectile
import me.newburyminer.customItems.entity.components.projectiles.LandmineArrow
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import me.newburyminer.customItems.entity.hiteffects.effect.ProjectileKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Arrow
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.UUID

class MachineGun: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.MACHINE_GUN

    private val material = Material.NETHERITE_NAUTILUS_ARMOR
    private val color = arrayOf(128, 110, 83)
    private val name = text("Machine Gun", color)
    private val lore = Utils.loreBlockToList(
        text(
            "Charges up for 2 seconds then starts shooting 4 arrows/sec, at 11.5 damage each. Slows you down whilst shooting.",
            Utils.GRAY
        )
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setData(DataComponentTypes.CONSUMABLE, Consumable.consumable()
            .animation(ItemUseAnimation.BOW)
            .consumeSeconds(32000.0F)
            .hasConsumeParticles(false)
            .build()
        )
        .hideAttributes()
        .build()

    init {
        register(PlayerInteractEvent::class, { e ->
            e.item.isItem(custom)
        },
        {e ->
            if (e.player.activeItemUsedTime > 1) return@register
            CustomEffects.playSound(e.player.location, Sound.ITEM_CROSSBOW_LOADING_START, 1.0F, 0.6F)
        })
    }

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(5 to {player -> updateGun(player)})

    private fun updateGun(player: Player) {
        if (player.activeItemUsedTime < 40) return
        if (!player.activeItem.isItem(custom)) return

        CustomEffects.playSound(player.location, Sound.ENTITY_SKELETON_SHOOT, 1.0F, 1.3F)

        val direction = player.location.direction.normalize()
        player.launchProjectile(Arrow::class.java, direction.multiply(3.3)) {
            it.shooter = player
            EntityWrapperManager.getWrapperorNew(it)
                .addComponent(CustomDamageProjectile(
                    HitEffects(
                        CustomDamageApply(11.5, CustomDamageType.PROJECTILE_NO_CD),
                        ProjectileKnockbackApply(0.1)
                    )
                ))
        }

    }

}