package me.newburyminer.customItems.items.customs.armor.helmets

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import me.newburyminer.customItems.items.EventItemType
import org.bukkit.Material
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect

class DrinkingCap: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.DRINKING_HAT

    private val material = Material.NETHERITE_HELMET
    private val color = arrayOf(235, 170, 127)
    private val name = text("Drinking Cap", color)
    private val lore = Utils.loreBlockToList(
        text("While wearing this, all potions you use will give you double the duration.", Utils.GRAY),
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()
        .attr("ARM+4.0HE","ART+4.0HE", "KNR+0.1HE","MAH+2.0HE")

    override fun handle(ctx: EventContext) {
        when (val e = ctx.event) {

            is EntityPotionEffectEvent -> {
                if (ctx.itemType != EventItemType.HELMET) return
                val player = ctx.player ?: return
                if (e.cause !in arrayOf(
                        EntityPotionEffectEvent.Cause.POTION_SPLASH,
                        EntityPotionEffectEvent.Cause.POTION_DRINK,
                        EntityPotionEffectEvent.Cause.FOOD,
                        EntityPotionEffectEvent.Cause.TOTEM,
                    )) return
                if (e.action != EntityPotionEffectEvent.Action.ADDED && e.action != EntityPotionEffectEvent.Action.CHANGED) return
                val newEffect = e.newEffect ?: return
                e.isCancelled = true
                player.addPotionEffect(PotionEffect(newEffect.type, newEffect.duration * 2,
                    newEffect.amplifier, newEffect.isAmbient, newEffect.hasParticles()))
            }

        }
    }

}
