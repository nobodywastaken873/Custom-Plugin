package me.newburyminer.customItems.items.customs.weapons.melee

import io.papermc.paper.datacomponent.DataComponentTypes
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.isItem
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
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class HeartWrenchingDagger: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.HEART_WRENCHING_DAGGER

    private val material = Material.NETHERITE_SWORD
    private val color = arrayOf(191, 27, 2)
    private val name = text("Heart-wrenching Dagger", color)
    private val lore = Utils.loreBlockToList(
        text("Right click to supercharge your next attack, draining 10 of your health, which will make it do true damage, ignoring all effects, " +
                "but capped at 1.1x this weapon's base damage (50s cooldown).", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_DAMAGE, 10.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ATTACK_SPEED, -2.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
        )
        .setLore(lore)
        .build()

    init {
        register(EntityDamageByEntityEvent::class, { e ->
            slotMatches(e, EquipmentSlot.HAND, custom) &&
            (e.damager as Player).equipment.itemInMainHand?.getData(DataComponentTypes.DAMAGE_TYPE) == CustomDamageType.ALL_BYPASS
        },
        {e ->
            e.damage = e.damage.coerceAtMost(10.0 * 1.1)

            val sword = (e.damager as Player).equipment.itemInMainHand
            sword.setData(DataComponentTypes.DAMAGE_TYPE, DamageType.PLAYER_ATTACK)
        })

        register(PlayerInteractEvent::class, { e ->
            e.item.isItem(custom) &&
            e.player.offCooldown(custom) &&
            isRightClick(e)
        },
        {e ->
            e.player.setCooldown(custom, 50.0)
            CustomEffects.playSound(e.player.location, Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1.0f, 0.7f)

            e.player.swingHand(e.hand ?: return@register)
            e.item?.setData(DataComponentTypes.DAMAGE_TYPE, CustomDamageType.ALL_BYPASS)

            if (e.player.health > 10.0) {
                e.player.health = (e.player.health - 10.0)
            }
            else {
                e.player.health = 0.1
                e.player.damage(10000.0, DamageSource.builder(DamageType.STARVE).build())
            }
        })
    }

}