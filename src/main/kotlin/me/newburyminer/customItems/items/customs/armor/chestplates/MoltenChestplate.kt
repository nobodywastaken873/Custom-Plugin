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

class MoltenChestplate: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.MOLTEN_CHESTPLATE

    private val material = Material.NETHERITE_CHESTPLATE
    private val color = arrayOf(209, 103, 4)
    private val name = text("Molten Chestplate", color)
    private val lore = mutableListOf<Component>()

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .attr("ARM+8.0CH","ART+4.0CH","KNR+0.1CH","BUT-1.0CH","MAH+4.0CH")

    override fun handle(ctx: EventContext) {}

    override val period: Int
        get() = 60
    override fun runTask(player: Player) {
        if (player.inventory.chestplate?.isItem(CustomItem.MOLTEN_CHESTPLATE) == true)
            player.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 65, 0, false, false))
    }

}