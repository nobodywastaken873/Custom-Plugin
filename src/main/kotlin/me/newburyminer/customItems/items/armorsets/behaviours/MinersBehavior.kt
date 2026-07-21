package me.newburyminer.customItems.items.armorsets.behaviours

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils.Companion.getArmorSet
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.armorsets.ArmorSet
import me.newburyminer.customItems.items.armorsets.ArmorSetBehavior
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.damage.DamageType
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class MinersBehavior: ArmorSetBehavior {

    override val set: ArmorSet = ArmorSet.MINER

    override val period: Int
        get() = 20
    override fun runTask(player: Player) {

        val pieceCount = player.equipment.armorContents.count { it?.getArmorSet() == set }
        if (pieceCount != 3) return

        player.addPotionEffect(PotionEffect(PotionEffectType.HASTE, 40, 1, false, false))
    }

}