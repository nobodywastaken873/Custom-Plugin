package me.newburyminer.customItems.items.customs.weapons.melee

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Weapon
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.reduceDura
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.SimpleModifier
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.damage.DamageType
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class MythrilHalberd: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.MYTHRIL_HALBERD

    private val material = Material.DIAMOND_SWORD
    private val color = arrayOf(134, 209, 179)
    private val name = text("Mythril Halberd", color)
    private val lore = Utils.loreBlockToList(
        text("Can break shields for 4 seconds. In addition, deals 1-2 extra durability damage to your opponent's armor.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_DAMAGE, 11.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ATTACK_SPEED, -2.6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
        )
        .setData(DataComponentTypes.WEAPON, Weapon.weapon()
            .disableBlockingForSeconds(4.0F)
            .build()
        )
        .setLore(lore)
        .build()

    init {
        register(EntityDamageByEntityEvent::class, { e ->
            slotMatches(e, EquipmentSlot.HAND, custom) &&
            e.damager is Player &&
            e.entity is Player
        },
        {e ->
            val hitPlayer = e.entity as Player

            val armor = hitPlayer.equipment.armorContents.filter {
                it != null && it.type != Material.AIR
            }

            armor.forEach {
                if (Math.random() < (1.0 / it.getEnchantmentLevel(Enchantment.UNBREAKING) + 1) && it.hasData(DataComponentTypes.DAMAGE)) {
                    it.reduceDura((1..2).random())
                }
            }
        })
    }

}