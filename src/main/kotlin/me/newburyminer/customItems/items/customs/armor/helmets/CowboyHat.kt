package me.newburyminer.customItems.items.customs.armor.helmets

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.cleanAttributeLore
import me.newburyminer.customItems.Utils.Companion.customName
import me.newburyminer.customItems.Utils.Companion.loreList
import me.newburyminer.customItems.Utils.Companion.setCustomData
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.entity.Horse
import org.bukkit.event.entity.EntityDismountEvent
import org.bukkit.event.entity.EntityMountEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class CowboyHat: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.COWBOY_HAT

    private val material = Material.NETHERITE_HELMET
    private val color = arrayOf(219, 124, 77)
    private val name = text("Cowboy Hat", color)
    private val lore = Utils.loreBlockToList(
        text("While wearing this, any horse you are riding will recieve Swiftness 3, Leaping 5, and will become invulnerable to damage.", Utils.GRAY),
    )

    override val item: ItemStack = ItemStack(material)
        .setCustomData(custom)
        .customName(name)
        .loreList(lore)
        .cleanAttributeLore()

    override fun handle(ctx: EventContext) {
        when (val e = ctx.event) {

            is EntityMountEvent -> {
                if (e.mount !is Horse) return
                (e.mount as Horse).addPotionEffects(mutableListOf(
                    PotionEffect(PotionEffectType.RESISTANCE, PotionEffect.INFINITE_DURATION, 4, true, false),
                    PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 2, true, false),
                    PotionEffect(PotionEffectType.JUMP_BOOST, PotionEffect.INFINITE_DURATION, 4, true, false),
                ))
            }

            is EntityDismountEvent -> {
                if (e.dismounted !is Horse) return
                if ((e.dismounted as Horse).hasPotionEffect(PotionEffectType.RESISTANCE))
                    (e.dismounted as Horse).clearActivePotionEffects()
            }

        }
    }

}
