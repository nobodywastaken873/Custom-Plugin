package me.newburyminer.customItems.items.customs.armor.boots

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
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
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class AqueousSandals: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.AQUEOUS_SANDALS

    private val material = Material.NETHERITE_BOOTS
    private val color = arrayOf(91, 130, 189)
    private val name = text("Aqueous Sandals", color)
    private val lore = Utils.loreBlockToList(
        text("Gain permanent water breathing and conduit power. Sneak to gain dolphin's grace for 5 seconds, with a 20 second cooldown.", Utils.GRAY)
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .attr("ARM+5.0FE","ART+4.0FE","WAM+1.0FE","SUM+1.0FE","MOS+0.01FE")

    override fun handle(ctx: EventContext) {
        when (val e = ctx.event) {

            is PlayerToggleSneakEvent -> {
                if (ctx.itemType != EventItemType.BOOTS) return
                val player = ctx.player ?: return
                if (!e.isSneaking) return
                if (!player.offCooldown(CustomItem.AQUEOUS_SANDALS)) return
                player.addPotionEffect(PotionEffect(PotionEffectType.DOLPHINS_GRACE, 100, 0, true, true, true))
                player.setCooldown(CustomItem.AQUEOUS_SANDALS, 20.0)
                CustomEffects.playSound(e.player.location, Sound.BLOCK_BEACON_ACTIVATE, 1.0F, 0.8F)
            }

        }
    }

    override val period: Int
        get() = 60
    override fun runTask(player: Player) {
        if (player.inventory.boots?.isItem(CustomItem.AQUEOUS_SANDALS) == true) {
            player.addPotionEffect(PotionEffect(PotionEffectType.WATER_BREATHING, 65, 0, false, false))
            player.addPotionEffect(PotionEffect(PotionEffectType.CONDUIT_POWER, 65, 0, false, false))
        }
    }

}