package me.newburyminer.customItems.mobprovider.mobs.mystic

import me.newburyminer.customItems.entity.components.projectileshooters.CustomWitchPotionShooter
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileHomingAbility
import me.newburyminer.customItems.mobprovider.ability.spell.HealerAbility
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object HealerMage : MobDefinition() {

	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.WITCH) {

        component(
            CustomWitchPotionShooter(
                listOf(
                    PotionEffect(PotionEffectType.SLOWNESS, linear(50 to 100, ctx), linear(1 to 3, ctx)),
                    PotionEffect(PotionEffectType.WITHER, linear(50 to 100, ctx), linear(1 to 3, ctx))
                )
            )
        )

        ability(
            ProjectileHomingAbility()
        )

        ability(
            HealerAbility(
                linear(15.0 to 25.0, ctx),
                linear(10.0 to 15.0, ctx),
                linear(2.0 to 4.0, ctx),
                linear(25 to 15, ctx),
                linear(90 to 60, ctx),
            )
        )

        health(
            linear(60.0 to 120.0, ctx)
        )

        movementSpeed(
            linear(0.6 to 0.9, ctx)
        )

    }

}