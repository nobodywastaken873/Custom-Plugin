package me.newburyminer.customItems.items.customs.armor.helmets

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Material
import org.bukkit.entity.AbstractHorse
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDismountEvent
import org.bukkit.event.entity.EntityMountEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class CowboyHat: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.COWBOY_HAT

    private val material = Material.NETHERITE_HELMET
    private val color = arrayOf(219, 124, 77)
    private val name = text("Cowboy Hat", color)
    private val lore = Utils.loreBlockToList(
        text("Removed.", Utils.FAILED_COLOR),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    /*init {
        register(EntityMountEvent::class, { e ->
            e.mount is AbstractHorse &&
            e.entity is Player &&
            slotMatches(e, EquipmentSlot.HEAD, custom)
        },
        {e ->
            (e.mount as AbstractHorse).addPotionEffects(mutableListOf(
                PotionEffect(PotionEffectType.RESISTANCE, PotionEffect.INFINITE_DURATION, 4, true, false),
                PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 2, true, false),
                PotionEffect(PotionEffectType.JUMP_BOOST, PotionEffect.INFINITE_DURATION, 4, true, false),
            ))
            e.mount.isInvulnerable = true
        })

        register(EntityDismountEvent::class, { e ->
            e.dismounted is AbstractHorse
        },
        {e ->
            e.dismounted.isInvulnerable = false
            if ((e.dismounted as AbstractHorse).hasPotionEffect(PotionEffectType.RESISTANCE))
                (e.dismounted as AbstractHorse).clearActivePotionEffects()
        })
    }*/

}