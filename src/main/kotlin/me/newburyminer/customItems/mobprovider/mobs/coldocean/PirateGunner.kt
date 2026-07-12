package me.newburyminer.customItems.mobprovider.mobs.coldocean

import me.newburyminer.customItems.Utils.Companion.ench
import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

object PirateGunner : MobDefinition() {

	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.PILLAGER) {

        ability(
            ProjectileEffectAbility(
                damage(linear(30.0 to 55.0, ctx), CustomDamageType.PROJECTILE_NO_CD),
                VanillaEffectApply(PotionEffectType.SLOWNESS, linear(40 to 160, ctx), 1),
                CustomKnockbackApply(Vector(0, -2, 0))
            )
        )

        health(
            linear(65.0 to 130.0, ctx)
        )

        movementSpeed(
            linear(1.25 to 1.5, ctx)
        )

        equipment {
            mainhand(ItemStack(Material.CROSSBOW).ench("QC1"))
        }

    }

}