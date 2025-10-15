package me.newburyminer.customItems.items.customs.armor.boots

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

class CloudBoots: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.CLOUD_BOOTS

    private val material = Material.NETHERITE_BOOTS
    private val color = arrayOf(193, 216, 227)
    private val name = text("Cloud Boots", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .attr("ARM+5.0FE","ART+4.0FE","KNR+0.1FE","FAD-1.0FE","MAH+4.0FE","MOE+1.0FE","STH+0.5FE")

    override fun handle(ctx: EventContext) {}

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(60 to {player -> runTask(player)})

    private fun runTask(player: Player) {
        if (player.inventory.boots?.isItem(CustomItem.CLOUD_BOOTS) == true)
            player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 65, 1, false, false))
    }

}