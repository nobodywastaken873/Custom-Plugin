package me.newburyminer.customItems.items.customs.weapons.melee.mobswords

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.AttackRange
import io.papermc.paper.datacomponent.item.PiercingWeapon
import io.papermc.paper.datacomponent.item.SwingAnimation
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addExtraSlayer
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.CustomItemBuilder
import me.newburyminer.customItems.items.CustomItemDefinition
import me.newburyminer.customItems.items.SimpleModifier
import org.bukkit.Material
import org.bukkit.Registry
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class BarbedCleaver: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.BARBED_CLEAVER

    private val material = Material.DIAMOND_SWORD
    private val color = arrayOf(237, 220, 33)
    private val name = Utils.text("Barbed Cleaver", color)
    private val lore = Utils.loreBlockToList(
        Utils.text("Piercing weapon type, sweeps to hit many mobs: ", Utils.GRAY),
        Utils.text("Hitbox Margins: +0.35 blocks, Range: 3.5 blocks.", Utils.GRAY),
        Utils.text("Deals 12% increased damage to Arid Lands mobs.", Utils.GRAY),
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_DAMAGE, 8.75, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
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
            AttackRange.attackRange().hitboxMargin(0.35F).minReach(0.0F).maxReach(3.5F).build()
        )
        .setLore(lore)
        .apply {
            this.addExtraSlayer(0.12)
        }
        .build()

}