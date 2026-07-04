package me.newburyminer.customItems.mobprovider.ability.spell

import me.newburyminer.customItems.entity.components.projectiles.ArcingEffectProjectile
import me.newburyminer.customItems.entity.components.spells.ArcingProjectileShooterComponent
import me.newburyminer.customItems.entity.components.spells.SpellCasterComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffect
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.ExplosionApply
import me.newburyminer.customItems.mobprovider.MobAbility
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import org.bukkit.Material

class ArcingEffectAbility(
    val range: Double,
    val height: Double,
    val material: Material,
    val count: Int,
    val cooldown: Int,
    vararg val effects: HitEffect
): MobAbility {

    override fun MobBuilder.applyAbility(ctx: MobContext) {

        component(
            ArcingProjectileShooterComponent(
                range,
                height,
                material,
                HitEffects(*effects),
                count,
                12,
                12 * count + 30,
                cooldown
            )
        )

        component(
            SpellCasterComponent(-0.2)
        )

    }

}