package me.newburyminer.customItems.items.customs.weapons.melee.swords

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.SimpleModifier
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class GaleBlade: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.GALE_BLADE

    private val material = Material.IRON_SWORD
    private val color = arrayOf(158, 207, 219)
    private val name = text("Gale Blade", color)
    private val lore = Utils.loreBlockToList(
        text("Right click to launch yourself forward, with a 8 second cooldown. Deals additional upwards knockback.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_DAMAGE, 8.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ATTACK_SPEED, -2.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
        )
        .setLore(lore)
        .build()

    init {
        register(PlayerInteractEvent::class, { e ->
            e.item.isItem(custom) &&
            e.player.offCooldown(custom) &&
            isRightClick(e)
        },
        {e ->
            e.item?.setCooldown(e.player, 8.0)
            e.player.swingHand(e.hand ?: return@register)
            e.player.velocity = e.player.velocity.add(e.player.location.direction.normalize().multiply(1.2))
            CustomEffects.playSound(e.player.location, Sound.ENTITY_BREEZE_SHOOT, 0.4F, 0.8F)
        })

        register(EntityDamageByEntityEvent::class, { e ->
            slotMatches(e, EquipmentSlot.HAND, custom)
        },
        {e ->
            val damaged = e.entity as? Player ?: return@register
            Bukkit.getScheduler().runTask(CustomItems.plugin, Runnable {
                damaged.velocity = damaged.velocity.add(Vector(0.0, 0.2, 0.0))
            })
        })
    }

}