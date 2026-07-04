package me.newburyminer.customItems.mobprovider.mobs.desert

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectAuraAbility
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffectType

object MaskedMummy : MobDefinition() {

	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.HUSK) {

        ability(
            EffectAuraAbility(
                linear(2.5 to 3.0, ctx),
                1.0,
                ParticleTheme.DESERT,
                20,
                VanillaEffectApply(PotionEffectType.BLINDNESS, 40, 0)
            )
        )

        ability(
            MeleeEffectAbility(
                damage(linear(15.0 to 25.0, ctx), CustomDamageType.MAGIC_NO_CD),
                VanillaKnockbackApply()
            )
        )

        health(
            linear(30.0 to 60.0, ctx)
        )

        movementSpeed(
            linear(0.8 to 1.0, ctx)
        )

    }

}