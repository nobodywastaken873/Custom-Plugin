package me.newburyminer.customItems.mobprovider

import me.newburyminer.customItems.entity.EntityComponent
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType

class MobBuilder(
    val entityType: EntityType,
) {
    var health: Double = 20.0
    var armor: Double = 0.0
    var movementSpeed: Double = 0.25

    val extraAttributes: MutableMap<Attribute, AttributeModifier> = mutableMapOf()

    val components: MutableList<EntityComponent> = mutableListOf()

    val equipment: EquipmentBuilder = EquipmentBuilder()

    val abilities: MutableList<MobAbility> = mutableListOf()

    fun health(value: Double) { health = value }
    fun armor(value: Double) { armor = value }
    fun movementSpeed(value: Double) { movementSpeed = value }

    fun attribute(attribute: Attribute, modifier: AttributeModifier) {
        extraAttributes[attribute] = modifier
    }

    fun equipment(block: EquipmentBuilder.() -> Unit) {
        equipment.block()
    }

    fun ability(ability: MobAbility) {
        abilities += ability
    }

    fun component(component: EntityComponent) {
        components.add(component)
    }
}