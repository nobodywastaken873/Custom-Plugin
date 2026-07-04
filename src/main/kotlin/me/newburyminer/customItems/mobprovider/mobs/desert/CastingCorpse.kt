package me.newburyminer.customItems.mobprovider.mobs.desert

import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.EffectAuraApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.defensive.LeapDodgeAbility
import me.newburyminer.customItems.mobprovider.ability.spell.ArcingEffectAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType

object CastingCorpse : MobDefinition() {

	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.HUSK) {

        ability(
            ArcingEffectAbility(
                linear(10.0 to 15.0, ctx),
                4.0,
                Material.SAND,
                linear(1 to 4, ctx),
                linear(300 to 250, ctx),
                EffectAuraApply(
                    linear(2.0 to 3.5, ctx),
                    1.0,
                    linear(120 to 200, ctx),
                    HitEffects(damage(linear(12.0 to 24.0, ctx), CustomDamageType.MAGIC_NO_CD)),
                    10,
                    ParticleTheme.DESERT
                )
            )
        )

        ability(
            MeleeEffectAbility(
                damage(linear(25.0 to 50.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        ability(
            LeapDodgeAbility(
                linear(0.15 to 0.4, ctx),
                linear(100 to 80, ctx),
                linear(0.5 to 1.0, ctx),
            )
        )

        health(
            linear(65.0 to 130.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.2, ctx)
        )

    }

}