package me.newburyminer.customItems.items.customs.armor.chestplates

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.fireworkBooster
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
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class MechanizedElytra: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.MECHANIZED_ELYTRA

    private val material = Material.ELYTRA
    private val color = arrayOf(103, 94, 110)
    private val name = text("Mechanized Elytra", color)
    private val lore = Utils.loreBlockToList(
        text("Sneak while wearing to activate a rocket boost with a 10 second cooldown.", Utils.GRAY),
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .attr("ARM+6.0CH","JUS+0.21CH","SAF+1.0CH")

    override fun handle(ctx: EventContext) {
        when (val e = ctx.event) {

            is PlayerToggleSneakEvent -> {
                if (ctx.itemType != EventItemType.CHESTPLATE) return
                val player = ctx.player ?: return
                if (!e.isSneaking) return
                if (!player.isGliding) return
                if (player.inventory.chestplate?.isItem(CustomItem.MECHANIZED_ELYTRA) != true) return
                if (!player.offCooldown(CustomItem.MECHANIZED_ELYTRA, "Boost")) return
                player.setCooldown(CustomItem.MECHANIZED_ELYTRA, 10.0, "Boost")
                player.fireworkBoost(ItemStack(Material.FIREWORK_ROCKET).fireworkBooster(1))
            }

        }
    }

}