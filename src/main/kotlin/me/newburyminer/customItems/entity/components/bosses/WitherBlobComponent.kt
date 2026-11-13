package me.newburyminer.customItems.entity.components.bosses

import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.bosses.BossDifficulty
import me.newburyminer.customItems.bosses.definitions.warden.WardenInstance
import me.newburyminer.customItems.entity.*
import me.newburyminer.customItems.entity.components.projectiles.CustomDamageProjectile
import org.bukkit.Bukkit
import org.bukkit.damage.DamageType
import org.bukkit.entity.Creeper
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.util.Vector
import kotlin.math.pow

class WitherBlobComponent(): EntityComponent {

    override fun serialize(): Map<String, Any> {
        return mapOf()
    }
    companion object: DeserializationInterface {
        override val componentType: EntityComponentType = EntityComponentType.WITHER_BLOB_COMPONENT
        override fun deserialize(map: Map<String, Any>): EntityComponent {
            return WitherBlobComponent()
        }
    }

}