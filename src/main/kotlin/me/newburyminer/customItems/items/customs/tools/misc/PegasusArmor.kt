package me.newburyminer.customItems.items.customs.tools.misc

import io.papermc.paper.event.entity.EntityEquipmentChangedEvent
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.getTag
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.NonPickuppableComponent
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Vault
import org.bukkit.entity.AbstractHorse
import org.bukkit.entity.EntityType
import org.bukkit.entity.Horse
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EntityEquipment
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

class PegasusArmor: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.PEGASUS_ARMOR

    private val material = Material.NETHERITE_HORSE_ARMOR
    private val color = arrayOf(230, 170, 223)
    private val name = text("Pegasus Armor", color)
    private val lore = Utils.loreBlockToList(
        text("Gives the horse that wears this armor permanent Speed III, Jump Boost V, and invulnerability.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .build()

    init {
        register(EntityEquipmentChangedEvent::class, { e ->
            e.entity is AbstractHorse
        },
        {e ->

            val changes = e.equipmentChanges
            if (changes.any { it.value.newItem().isItem(custom) }) {
                (e.entity as AbstractHorse).addPotionEffects(mutableListOf(
                    PotionEffect(PotionEffectType.RESISTANCE, PotionEffect.INFINITE_DURATION, 4, true, false),
                    PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 2, true, false),
                    PotionEffect(PotionEffectType.JUMP_BOOST, PotionEffect.INFINITE_DURATION, 4, true, false),
                ))
                e.entity.isInvulnerable = true
            }

            if (changes.any { it.value.oldItem().isItem(custom) }) {
                e.entity.isInvulnerable = false
                (e.entity as AbstractHorse).clearActivePotionEffects()
            }

        })
    }

}