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

class NetheriteClaymore: CustomItemDefinition {

    override val custom: CustomItem = CustomItem.NETHERITE_CLAYMORE

    private val material = Material.NETHERITE_SWORD
    private val color = arrayOf(10, 242, 234)
    private val name = text("Netherite Claymore", color)
    private val lore = Utils.loreBlockToList(
        text("Piercing weapon type, like a spear jab attack but widened. Extended range of 3.8 blocks, sweeps to hit many mobs.", Utils.GRAY)
    )

    override val item: ItemStack = CustomItemBuilder(material, custom)
        .setName(name, false)
        .setAttributes(
            SimpleModifier(Attribute.ATTACK_DAMAGE, 10.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
            SimpleModifier(Attribute.ATTACK_SPEED, -2.9, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND),
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
            SwingAnimation.swingAnimation().type(SwingAnimation.Animation.WHACK).duration(10).build()
        )
        .setData(DataComponentTypes.MINIMUM_ATTACK_CHARGE, 0.5F)
        .setData(DataComponentTypes.ATTACK_RANGE,
            AttackRange.attackRange().hitboxMargin(0.4F).minReach(0.0F).maxReach(3.8F).build()
        )
        .setLore(lore)
        .build()

}