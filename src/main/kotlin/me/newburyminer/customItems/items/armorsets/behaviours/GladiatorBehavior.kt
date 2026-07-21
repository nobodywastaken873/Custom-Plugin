package me.newburyminer.customItems.items.armorsets.behaviours

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys
import me.newburyminer.customItems.Utils.Companion.getArmorSet
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.armorsets.ArmorSet
import me.newburyminer.customItems.items.armorsets.ArmorSetBehavior
import org.bukkit.Sound
import org.bukkit.damage.DamageType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerToggleSneakEvent

class GladiatorBehavior : ArmorSetBehavior {

    override val set: ArmorSet = ArmorSet.GLADIATOR

    init {
        register(PlayerInteractEvent::class, { e ->
            getPieces(e.player, set) == 4 &&
            e.item != null &&
            e.player.offCooldown(CustomItem.GLADIATORS_CHESTPLATE)
        },
        {e ->

            val item = e.item ?: return@register
            if (!item.hasData(DataComponentTypes.KINETIC_WEAPON)) return@register

            val player = e.player

            val velocityMult = if (player.equipment.itemInMainHand.isItem(CustomItem.GLADIATORS_SPEAR)) 1.25 else 1.0

            e.player.velocity = e.player.velocity.add(e.player.location.direction.normalize().multiply(1.4 * velocityMult))
            CustomEffects.playSound(e.player.location, Sound.ITEM_SPEAR_LUNGE_3, 1.0F, 1.0F)

            for (custom in arrayOf(CustomItem.GLADIATORS_HELM, CustomItem.GLADIATORS_CHESTPLATE, CustomItem.GLADIATORS_GREAVES, CustomItem.GLADIATORS_BOOTS)) {
                player.setCooldown(custom, 5.0)
            }
        })

        register(EntityDamageByEntityEvent::class, { e ->
            e.damager is Player &&
            (e.damager as Player).equipment.armorContents.any { it?.getArmorSet() == set } &&
            e.damageSource.damageType == DamageType.SPEAR
        },
        {e ->
            val pieceCount = (e.damager as Player).equipment.armorContents.count { it?.getArmorSet() == set }
            val multiplier = if (pieceCount == 4) 0.6 else pieceCount * 0.1

            e.damage *= 1.0 + multiplier
        })
    }

}