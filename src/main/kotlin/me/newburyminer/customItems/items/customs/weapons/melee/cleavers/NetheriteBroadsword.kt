package me.newburyminer.customItems.items.customs.weapons.melee.cleavers

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.AttackRange
import io.papermc.paper.datacomponent.item.PiercingWeapon
import io.papermc.paper.datacomponent.item.SwingAnimation
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.text
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

class NetheriteBroadsword: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.NETHERITE_BROADSWORD

    private val material = Material.NETHERITE_SWORD
    private val color = arrayOf(10, 242, 234)
    private val name = text("Netherite Broadsword", color)
    private val lore = Utils.loreBlockToList(
        text("Piercing weapon type, like a spear jab attack but widened. Extended range of 3.2 blocks, sweeps to hit many mobs.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_DAMAGE, 8.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
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
        .setData(DataComponentTypes.ATTACK_RANGE,
            AttackRange.attackRange().hitboxMargin(0.25F).minReach(0.0F).maxReach(3.2F).build()
        )
        .setLore(lore)
        .build()

}