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

class BerserkerChestplate: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.BERSERKER_CHESTPLATE

    private val material = Material.NETHERITE_CHESTPLATE
    private val color = arrayOf(245, 136, 2)
    private val name = text("Berserker Chestplate", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .attr("ARM+8.0CH","ART+4.0CH","KNR+0.1CH","MOS+0.01CH", "MAH+4.0CH","ENI+0.5CH","ATD+4.0CH")

    override fun handle(ctx: EventContext) {}

    override val period: Int
        get() = 60
    override fun runTask(player: Player) {
        if (player.inventory.chestplate?.isItem(CustomItem.BERSERKER_CHESTPLATE) == true)
            player.addPotionEffect(PotionEffect(PotionEffectType.STRENGTH, 65, 0, false, false))
    }

}