package me.newburyminer.customItems.items.customs.armor.sets.assassin

import io.papermc.paper.event.entity.EntityMoveEvent
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.getCustom
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setArmorSet
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import me.newburyminer.customItems.items.armorsets.ArmorSet
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.entity.EntityDismountEvent
import org.bukkit.event.entity.EntityMountEvent
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class AssassinsHood: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ASSASSINS_HOOD

    private val material = Material.NETHERITE_HELMET
    private val color = arrayOf(32, 2, 112)
    private val name = text("Assassin's Hood", color)
    private val lore = Utils.loreBlockToList(
        text("Gain an additive 1/8 chance to dodge arrows.", Utils.GRAY),
        text(""),
        text("Full Set Bonus (4 pieces): Assassin's Set", Utils.GRAY),
        text("Gain permanent invisibility. Gain +4% movement speed, 0.6 attack damage, 0.02 attack speed, and -3% scale every second for up to 10 seconds. Resets upon taking damage.", Utils.GRAY),
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .attr("ARM+2.0HE","ART+2.0HE","ATD+2.0HE","MOS+0.005HE","ATS+0.05HE")
        .setArmorSet(ArmorSet.ASSASSIN)

    override fun handle(ctx: EventContext) {}

}
