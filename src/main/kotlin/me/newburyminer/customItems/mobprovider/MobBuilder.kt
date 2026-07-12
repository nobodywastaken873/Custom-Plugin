package me.newburyminer.customItems.mobprovider

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.entity.EntityComponent
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.DefaultEntityComponent
import me.newburyminer.customItems.entity.components.projectileshooters.CancelProjectiles
import me.newburyminer.customItems.entity.components.spells.SpellCasterComponent
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.util.BoundingBox
import kotlin.reflect.KClass

class MobBuilder(
    val entityType: EntityType,
    val tier: MobTier,
    val targetRange: Double,
    val trim: ArmorTrim,
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
    fun equipment(block: EquipmentBuilder.() -> Unit) { equipment.block() }
    fun ability(ability: MobAbility) { abilities += ability }
    fun component(component: EntityComponent) { components.add(component) }
    fun apply(block: LivingEntity.() -> Unit) { extraApplications.add(block) }

    fun createEntity(ctx: MobContext): LivingEntity {

        abilities.forEach {
            with (it) {
                applyAbility(ctx)
            }
        }

        component(
            DefaultEntityComponent(
                tier,
                targetRange,
            )
        )

        if (entityType == EntityType.EVOKER || entityType == EntityType.ILLUSIONER) {
            component(
                CancelProjectiles()
            )
        }

        removeDuplicateComponents(SpellCasterComponent::class)
        removeDuplicateComponents(CancelProjectiles::class)

        val entity = ctx.location.world.spawnEntity(ctx.location, entityType, false) as LivingEntity

        if (entity.equipment != null) {
            equipment.boots(
                when (tier) {
                    MobTier.GRUNT -> Material.COPPER_BOOTS
                    MobTier.STANDARD -> Material.IRON_BOOTS
                    MobTier.ELITE -> Material.GOLDEN_BOOTS
                    MobTier.MINIBOSS -> Material.DIAMOND_BOOTS
                },
                trim = trim
            )
        }

        entity.getAttribute(Attribute.MAX_ABSORPTION)?.baseValue = 2000.0
        entity.getAttribute(Attribute.MAX_HEALTH)?.baseValue = health
        entity.health = health
        entity.getAttribute(Attribute.MOVEMENT_SPEED)?.baseValue = movementSpeed
        entity.getAttribute(Attribute.ARMOR)?.baseValue = armor
        entity.getAttribute(Attribute.SCALE)?.baseValue = scale

        val wrapper = EntityWrapperManager.getWrapperorNew(entity)

        extraApplications.forEach { entity.it() }

        // double check for no duplicate components of certain types
        components.forEach { wrapper.addComponent(it) }

        extraAttributes.forEach { (attribute, modifier) ->
            entity.getAttribute(attribute)?.addModifier(modifier)
        }

        equipment.equipment.forEach { (slot, stack) ->
            entity.equipment?.setItem(slot, stack)
        }

        return entity
    }

    private fun <T : EntityComponent> removeDuplicateComponents(clazz: KClass<T>) {

        var found = false
        components.removeIf {
            if (clazz.isInstance(it)) {
                if (found) true
                else {
                    found = true
                    false
                }
            } else false
        }

    }

}