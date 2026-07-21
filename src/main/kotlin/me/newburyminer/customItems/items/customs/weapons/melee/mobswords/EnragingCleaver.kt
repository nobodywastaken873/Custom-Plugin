package me.newburyminer.customItems.items.customs.weapons.melee.mobswords

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.AttackRange
import io.papermc.paper.datacomponent.item.PiercingWeapon
import io.papermc.paper.datacomponent.item.SwingAnimation
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addExtraSlayer
import me.newburyminer.customItems.Utils.Companion.isItem
import me.newburyminer.customItems.Utils.Companion.offCooldown
import me.newburyminer.customItems.Utils.Companion.setCooldown
import me.newburyminer.customItems.effects.AttributeData
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.effects.EffectManager
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.DefaultEntityComponent
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.SimpleModifier
import org.bukkit.Material
import org.bukkit.Registry
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class EnragingCleaver: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.ENRAGING_CLEAVER

    private val material = Material.DIAMOND_SWORD
    private val color = arrayOf(237, 98, 33)
    private val name = Utils.text("Enraging Cleaver", color)
    private val lore = Utils.loreBlockToList(
        Utils.text("Piercing weapon type, sweeps to hit many mobs: ", Utils.GRAY),
        Utils.text("Hitbox Margins: +0.45 blocks, Range: 4.0 blocks.", Utils.GRAY),
        Utils.text("Deals 25% increased damage to Arid Lands mobs. Right click to enrage for 8 seconds, gaining complete immunity to damage from Arid Lands mobs " +
                "and dealing an additional 25% damage to Arid Lands mobs. 30s cooldown.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_DAMAGE, 11.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ATTACK_SPEED, -2.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
        )
        .setData(
            DataComponentTypes.PIERCING_WEAPON,
            PiercingWeapon.piercingWeapon()
                .dismounts(true)
                .dealsKnockback(true)
                .sound(Registry.SOUNDS.getKey(Sound.ENTITY_PLAYER_ATTACK_SWEEP)!!).build()
        )
        .setData(
            DataComponentTypes.SWING_ANIMATION,
            SwingAnimation.swingAnimation().type(SwingAnimation.Animation.WHACK).duration(7).build()
        )
        .setData(DataComponentTypes.MINIMUM_ATTACK_CHARGE, 0.5F)
        .setData(
            DataComponentTypes.ATTACK_RANGE,
            AttackRange.attackRange().hitboxMargin(0.45F).minReach(0.0F).maxReach(4.0F).build()
        )
        .setLore(lore)
        .apply {
            this.addExtraSlayer(0.25)
        }
        .build()

    init {
        register(PlayerInteractEvent::class, { e ->
            e.item.isItem(custom) &&
            e.player.offCooldown(custom) &&
            isRightClick(e)
        },
        {e ->
            e.player.setCooldown(custom, 30.0)
            CustomEffects.playSound(e.player.location, Sound.ENTITY_WARDEN_ROAR, 1.0f, 0.8f)

            e.player.swingHand(e.hand ?: return@register)

            EffectManager.applyEffect(e.player, CustomEffectType.MOB_ENRAGED, 20 * 8)
        })

        register(EntityDamageByEntityEvent::class, { e ->
            e.entity is Player &&
            EffectManager.hasEffect(e.entity as Player, CustomEffectType.MOB_ENRAGED)
        },
        {e ->
            val wrapper = EntityWrapperManager.getWrapper(e.damager.uniqueId) ?: return@register
            if (!wrapper.hasComponent(DefaultEntityComponent::class)) return@register

            e.damage = 0.0
        })
    }

}