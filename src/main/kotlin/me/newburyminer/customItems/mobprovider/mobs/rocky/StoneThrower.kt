package me.newburyminer.customItems.mobprovider.mobs.rocky

import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.DamageRadiusApply
import me.newburyminer.customItems.entity.hiteffects.effect.ProjectileKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.projectile.ProjectileEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.ArcingEffectAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object StoneThrower : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.ROCKY
    override val trim: TrimPattern = TrimPattern.BOLT
	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.STRAY) {

        ability(
            ProjectileEffectAbility(
                damage(linear(25.0 to 45.0, ctx), CustomDamageType.PROJECTILE_NO_CD),
                ProjectileKnockbackApply(0.6)
            )
        )

        ability(
            ArcingEffectAbility(
                linear(15.0 to 30.0, ctx),
                4.0,
                Material.STONE,
                linear(2 to 4, ctx),
                linear(300 to 150, ctx),
                DamageRadiusApply(
                    linear(2.0 to 3.5, ctx),
                    1.0,
                    HitEffects(
                        damage(linear(25.0 to 50.0, ctx), CustomDamageType.EXPLOSION_NO_CD),
                        VanillaKnockbackApply()
                    ),
                    ParticleTheme.ROCKY
                )
            )
        )

        health(
            linear(60.0 to 120.0, ctx)
        )

        movementSpeed(
            linear(0.9 to 1.3, ctx)
        )

        equipment {
            mainhand(Material.BOW)
            offhand(Material.GUNPOWDER)
            setArmor(colorTheme.color, trim)
        }

    }

}