package me.newburyminer.customItems.items.customs.weapons.melee

import io.papermc.paper.datacomponent.DataComponentTypes
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.effects.AttributeData
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.SimpleModifier
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class EtherealSaber: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ETHEREAL_SABER

    private val material = Material.DIAMOND_SWORD
    private val color = arrayOf(235, 136, 233)
    private val name = text("Ethereal Saber", color)
    private val lore = Utils.loreBlockToList(
        text("Every other hit from this weapon does magic damage. However, the magic damage hits only use the base damage value of this sword, not any modifiers.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_DAMAGE, 9.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ATTACK_SPEED, -2.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
        )
        .setLore(lore)
        .build()

    init {
        register(EntityDamageByEntityEvent::class, { e ->
            slotMatches(e, EquipmentSlot.HAND, custom) &&
            e.damager is Player
        },
        {e ->
            val player = e.damager as Player
            val sword = player.equipment.itemInMainHand
            val currentType = sword.getData(DataComponentTypes.DAMAGE_TYPE) ?: DamageType.PLAYER_ATTACK

            if (currentType == CustomDamageType.MAGIC) {
                player.playSound(player.location, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1F, 1.5F)
                val damage = 9.5 * (if (e.isCritical) 1.5 else 1.0)
                e.damage = damage
            }

            val newType = when (currentType) {
                DamageType.PLAYER_ATTACK -> CustomDamageType.MAGIC
                CustomDamageType.MAGIC -> DamageType.PLAYER_ATTACK
                else -> DamageType.PLAYER_ATTACK
            }
            sword.setData(DataComponentTypes.DAMAGE_TYPE, newType)
        })
    }

}