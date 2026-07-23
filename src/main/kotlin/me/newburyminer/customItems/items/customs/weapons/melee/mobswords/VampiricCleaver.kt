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
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class VampiricCleaver: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.VAMPIRIC_CLEAVER

    private val material = Material.DIAMOND_SWORD
    private val color = arrayOf(237, 74, 33)
    private val name = Utils.text("Vampiric Cleaver", color)
    private val lore = Utils.loreBlockToList(
        Utils.text("Piercing weapon type, sweeps to hit many mobs: ", Utils.GRAY),
        Utils.text("Hitbox Margins: +0.4 blocks, Range: 3.85 blocks.", Utils.GRAY),
        Utils.text("Deals 22% increased damage to Arid Lands mobs, heal 2 HP on mob kill.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_DAMAGE, 10.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
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
            AttackRange.attackRange().hitboxMargin(0.4F).minReach(0.0F).maxReach(3.85F).build()
        )
        .setLore(lore)
        .apply {
            this.addExtraSlayer(0.22)
        }
        .build()

    init {
        register(EntityDeathEvent::class, { e ->
            e.entity.killer is Player &&
            (e.entity.killer as Player).equipment.itemInMainHand.isItem(custom)
        },
        {e ->
            val wrapper = EntityWrapperManager.getWrapper(e.entity.uniqueId) ?: return@register
            if (!wrapper.hasComponent(DefaultEntityComponent::class)) return@register

            val player = e.entity.killer ?: return@register
            player.heal(2.0)

            CustomEffects.playSound(e.entity.location, Sound.ENTITY_GENERIC_DRINK, 0.4F, 1.5F)
        })
    }

}