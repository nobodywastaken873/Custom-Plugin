package me.newburyminer.customItems.items.customs.armor.boots

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.SimpleModifier
import net.kyori.adventure.text.Component
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class DoubleJumpBoots: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.DOUBLE_JUMP_BOOTS

    private val material = Material.NETHERITE_BOOTS
    private val color = arrayOf(171, 230, 245)
    private val name = text("Double Jump Boots", color)
    private val lore = Utils.loreBlockToList(
        text("Double press your spacebar in midair to double jump, with a 4s cooldown.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.ARMOR_TOUGHNESS, 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.KNOCKBACK_RESISTANCE, 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
            SimpleModifier(Attribute.SAFE_FALL_DISTANCE, 8.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET),
        )
        .build()

    init {
        register(PlayerToggleFlightEvent::class, { e ->
            slotMatches(e, EquipmentSlot.FEET, custom) &&
            e.player.gameMode !in arrayOf(GameMode.CREATIVE, GameMode.SPECTATOR)
        },
        {e ->
            e.isCancelled = true
            e.player.allowFlight = false
            if (!e.player.offCooldown(CustomItem.DOUBLE_JUMP_BOOTS)) return@register

            e.player.velocity = e.player.location.direction.multiply(1.0).setY(0.7)
            e.player.setCooldown(CustomItem.DOUBLE_JUMP_BOOTS, 4.0)
        })
    }

    override val extraTasks: Map<Int, (Player) -> Unit>
        get() = mapOf(4 to {player -> resetDoubleJump(player)})

    private fun resetDoubleJump(player: Player) {
        if (player.allowFlight) return
        if (!player.isOnGround) return
        if (!player.inventory.boots.isItem(CustomItem.DOUBLE_JUMP_BOOTS)) return
        if (!player.offCooldown(CustomItem.DOUBLE_JUMP_BOOTS)) return
        player.allowFlight = true
    }

}