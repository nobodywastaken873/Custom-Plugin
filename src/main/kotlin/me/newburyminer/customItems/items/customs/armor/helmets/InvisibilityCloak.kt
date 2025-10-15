package me.newburyminer.customItems.items.customs.armor.helmets

import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class InvisibilityCloak: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.INVISIBILITY_CLOAK

    private val material = Material.NETHERITE_HELMET
    private val color = arrayOf(227, 231, 232)
    private val name = text("Invisibility Cloak", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .attr("ARM+3.0HE","ART+3.0HE","KNR+0.1HE","MAH+4.0HE","ATD+4.0HE","ATS+0.2HE")

    override fun handle(ctx: EventContext) {}

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(60 to {player -> runTask(player)})

    private fun runTask(player: Player) {
        if (player.inventory.helmet?.isItem(CustomItem.INVISIBILITY_CLOAK) == true)
            player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 65, 0, false, false))
    }

}