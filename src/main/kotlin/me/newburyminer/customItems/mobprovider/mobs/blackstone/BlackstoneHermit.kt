package me.newburyminer.customItems.mobprovider.mobs.blackstone

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectMissileAbility
import me.newburyminer.customItems.mobprovider.ability.spell.SummonerAbility
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object BlackstoneHermit : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.BLACKSTONE
    override val trim: TrimPattern = TrimPattern.SENTRY
	override val tier: MobTier = MobTier.STANDARD
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.SKELETON) {

        ability(
            MeleeEffectAbility(
                damage(linear(25.0 to 50.0, ctx), CustomDamageType.MELEE),
                VanillaKnockbackApply()
            )
        )

        ability(
            SummonerAbility(
                linear(3 to 5, ctx),
                BlackSilverfish,
                linear(40 to 30, ctx),
                linear(300 to 250, ctx),
                ParticleTheme.BLACKSTONE
            )
        )

        ability(
            EffectMissileAbility(
                linear(30.0 to 40.0, ctx),
                0.05,
                linear(2.0 to 3.0, ctx),
                linear(30 to 20, ctx),
                linear(70 to 40, ctx),
                ParticleTheme.BLACKSTONE,
                attribute(Attribute.SCALE, 0.1, AttributeModifier.Operation.ADD_NUMBER, linear(80 to 160, ctx))
            )
        )

        health(
            linear(70.0 to 140.0, ctx)
        )

        movementSpeed(
            linear(1.0 to 1.5, ctx)
        )

        equipment {
            mainhand(Material.BREEZE_ROD)
            offhand(Material.AIR)
            setArmor(colorTheme.color, trim)
        }

    }

}