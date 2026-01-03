package me.newburyminer.customItems.items.customs.armor.helmets

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.eventbus.EventRegistry
import me.newburyminer.customItems.eventbus.ListenerEntry
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.EventContext
import org.bukkit.Material
import org.bukkit.entity.AbstractHorse
import org.bukkit.entity.Horse
import org.bukkit.entity.Player
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

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        val onMount = ListenerEntry(
            EntityMountEvent::class,
            {isHorse(it)},
            {applyHorseEffects(it)},
        )
        val onDismount = ListenerEntry(
            EntityDismountEvent::class,
            {hasEffects(it)},
            {removeHorseEffects(it)},
        )
        EventRegistry.register(onMount)
        EventRegistry.register(onDismount)
    }

    private fun isHorse(e: EntityMountEvent): Boolean {
        return e.mount is AbstractHorse &&
                e.entity is Player &&
                (e.entity as Player).inventory.helmet.isItem(custom)
    }
    private fun applyHorseEffects(e: EntityMountEvent) {
        (e.mount as AbstractHorse).addPotionEffects(mutableListOf(
            PotionEffect(PotionEffectType.RESISTANCE, PotionEffect.INFINITE_DURATION, 4, true, false),
            PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 2, true, false),
            PotionEffect(PotionEffectType.JUMP_BOOST, PotionEffect.INFINITE_DURATION, 4, true, false),
        ))
        e.mount.isInvulnerable = true
    }

    private fun hasEffects(e: EntityDismountEvent): Boolean {
        return e.dismounted is AbstractHorse
    }
    private fun removeHorseEffects(e: EntityDismountEvent) {
        e.dismounted.isInvulnerable = false
        if ((e.dismounted as AbstractHorse).hasPotionEffect(PotionEffectType.RESISTANCE))
            (e.dismounted as AbstractHorse).clearActivePotionEffects()
    }

    /*override fun handle(ctx: EventContext) {
        when (val e = ctx.event) {

            is EntityMountEvent -> {
                if (e.mount !is Horse) return
                (e.mount as Horse).addPotionEffects(mutableListOf(
                    PotionEffect(PotionEffectType.RESISTANCE, PotionEffect.INFINITE_DURATION, 4, true, false),
                    PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 2, true, false),
                    PotionEffect(PotionEffectType.JUMP_BOOST, PotionEffect.INFINITE_DURATION, 4, true, false),
                ))
                e.mount.isInvulnerable = true
            }

            is EntityDismountEvent -> {
                if (e.dismounted !is Horse) return
                e.dismounted.isInvulnerable = false
                if ((e.dismounted as Horse).hasPotionEffect(PotionEffectType.RESISTANCE))
                    (e.dismounted as Horse).clearActivePotionEffects()
            }

        }
    }*/

}