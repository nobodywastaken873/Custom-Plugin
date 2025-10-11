package me.newburyminer.customItems.items.customs.armor.chestplates

import io.papermc.paper.event.entity.EntityMoveEvent
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.getCustom
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.*
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.damage.DamageType
import org.bukkit.entity.*
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDismountEvent
import org.bukkit.event.entity.EntityMountEvent
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class MaceShieldedPlating: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.MACE_SHIELDED_PLATING

    private val material = Material.NETHERITE_CHESTPLATE
    private val color = arrayOf(89, 84, 92)
    private val name = text("Mace Shielded Plating", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .attr("ARM+10.0CH","ART+4.0CH","KNR+0.4CH")

    override fun handle(ctx: EventContext) {
        when (val e = ctx.event) {

            is EntityDamageByEntityEvent -> {
                if (ctx.itemType != EventItemType.CHESTPLATE) return
                val player = ctx.player ?: return
                if (e.entity !is Player) return
                if (e.damager !is Player) return
                if (e.damageSource.damageType != DamageType.MACE_SMASH) return
                e.damage *= 0.4
            }

        }
    }

}