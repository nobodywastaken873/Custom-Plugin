package me.newburyminer.customItems.mobprovider

import jdk.dynalink.Operation
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.entity.EntityComponent
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity

class MobBuilder(
    val entityType: EntityType,
) {
    var health: Double = 20.0
    var armor: Double = 0.0
    var movementSpeed: Double = 0.25
    var scale: Double = 1.0

    val extraAttributes: MutableMap<Attribute, AttributeModifier> = mutableMapOf()

    val components: MutableList<EntityComponent> = mutableListOf()

    val equipment: EquipmentBuilder = EquipmentBuilder()

    val abilities: MutableList<MobAbility> = mutableListOf()

    val extraApplications: MutableList<LivingEntity.() -> Unit> = mutableListOf()

    fun health(value: Double) { health = value }
    fun armor(value: Double) { armor = value }
    fun movementSpeed(value: Double) { movementSpeed = value * 0.25 }
    fun scale(value: Double) { scale = value }

    fun attribute(attribute: Attribute, amount: Double, operation: AttributeModifier.Operation) {
        extraAttributes[attribute] = AttributeModifier(NamespacedKey(CustomItems.plugin, "spawned"), amount, operation)
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

    fun apply(block: LivingEntity.() -> Unit) {
        extraApplications.add(block)
    }
}