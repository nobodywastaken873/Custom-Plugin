package me.newburyminer.customItems.mobprovider.mobs.warmocean

import me.newburyminer.customItems.entity.components.melee.SuicideBomberComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.EffectAuraApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.spell.EffectAuraAbility
import org.bukkit.entity.Drowned
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffectType

object BabyMerman : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.WARM_OCEAN
    override val trim: TrimPattern = TrimPattern.SENTRY
	override val tier: MobTier = MobTier.GRUNT
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.DROWNED) {

        component(
            SuicideBomberComponent(
                HitEffects(
                    EffectAuraApply(
                        linear(3.5 to 5.0, ctx),
                        1.0,
                        linear(60 to 100, ctx),
                        HitEffects(damage(linear(10.0 to 20.0, ctx), CustomDamageType.MAGIC_NO_CD)),
                        10,
                        ParticleTheme.WARM_OCEAN
                    )
                )
            )
        )

        health(
            linear(20.0 to 40.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.5, ctx)
        )

        apply {
            if (this !is Drowned) return@apply
            this.setBaby()
        }

    }

}