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
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class ArtificersBlade: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ARTIFICERS_BLADE

    private val material = Material.NETHERITE_SWORD
    private val color = arrayOf(217, 195, 28)
    private val name = text("Artificer's Blade", color)
    private val lore = Utils.loreBlockToList(
        text("Right click to drain 5 health but gain 10 absorption health, 25s cooldown, capped at 20 total absorption health. " +
                "In addition, there is a 1/5 chance to inflict Weakness I (5s) upon hitting a enemy.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_DAMAGE, 11.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ATTACK_SPEED, -2.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
        )
        .setLore(lore)
        .build()

    init {
        register(EntityDamageByEntityEvent::class, { e ->
            slotMatches(e, EquipmentSlot.HAND, custom)
        },
        {e ->
            if (Math.random() > 0.2) return@register

            val hit = e.entity as? LivingEntity ?: return@register
            hit.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, 100, 0))
        })

        register(PlayerInteractEvent::class, { e ->
            e.item.isItem(custom) &&
            e.player.offCooldown(custom) &&
            isRightClick(e)
        },
        {e ->
            e.player.setCooldown(custom, 25.0)
            CustomEffects.playSound(e.player.location, Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1.0f, 0.7f)

            e.player.swingHand(e.hand ?: return@register)

            if (e.player.health > 5.0) {
                e.player.health = (e.player.health - 5.0)
                e.player.absorptionAmount = (e.player.absorptionAmount + 10.0).coerceAtMost(20.0)
            }
            else {
                e.player.health = 0.1
                e.player.damage(10000.0, DamageSource.builder(DamageType.STARVE).build())
            }
        })
    }

}