package me.newburyminer.customItems.items.customs.armor.chestplates

import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.fireworkBooster
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.items.*
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class MechanizedElytra: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.MECHANIZED_ELYTRA

    private val material = Material.ELYTRA
    private val color = arrayOf(103, 94, 110)
    private val name = text("Mechanized Elytra", color)
    private val lore = Utils.loreBlockToList(
        text("Sneak while wearing to activate a rocket boost with a 10 second cooldown.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setLore(lore)
        .setAttributes(
            SimpleModifier(Attribute.ARMOR, 6.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
            SimpleModifier(Attribute.JUMP_STRENGTH, 0.21, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
            SimpleModifier(Attribute.SAFE_FALL_DISTANCE, 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST),
        )
        .build()

    override fun handle(ctx: EventContext) {
        when (val e = ctx.event) {

            is PlayerToggleSneakEvent -> {
                if (ctx.itemType != EventItemType.CHESTPLATE) return
                val player = ctx.player ?: return
                if (!e.isSneaking) return
                if (!player.isGliding) return
                if (player.inventory.chestplate?.isItem(CustomItem.MECHANIZED_ELYTRA) != true) return
                if (!player.offCooldown(CustomItem.MECHANIZED_ELYTRA, "Boost")) return
                player.setCooldown(CustomItem.MECHANIZED_ELYTRA, 10.0, "Boost")
                player.fireworkBoost(ItemStack(Material.FIREWORK_ROCKET).fireworkBooster(1))
            }

        }
    }

}