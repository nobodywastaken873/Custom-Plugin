package me.newburyminer.customItems.mobprovider.mobs.blackstone

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
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

object MagmaBrute : MobDefinition() {

    override val trim: ArmorTrim = ArmorTrim(TrimMaterial.DIAMOND, TrimPattern.SENTRY)
	override val tier: MobTier = MobTier.ELITE
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.WITHER_SKELETON) {

        ability(
            MeleeEffectAbility(
                damage(linear(32.0 to 64.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        ability(
            EffectMissileAbility(
                linear(30.0 to 40.0, ctx),
                0.05,
                linear(2.0 to 3.0, ctx),
                linear(30 to 20, ctx),
                linear(200 to 120, ctx),
                ParticleTheme.BLACKSTONE,
                attribute(Attribute.ATTACK_SPEED, -0.1, AttributeModifier.Operation.ADD_NUMBER, linear(70 to 120, ctx)),
                damage(linear(25.0 to 50.0, ctx), CustomDamageType.BURNING_NO_CD)
            )
        )

        ability(
            SummonerAbility(
                linear(1 to 3, ctx),
                DarkDuelist,
                linear(40 to 30, ctx),
                linear(250 to 200, ctx),
                ParticleTheme.BLACKSTONE,
            )
        )

        health(
            linear(250.0 to 500.0, ctx)
        )

        movementSpeed(
            linear(0.6 to 0.8, ctx)
        )

        scale(1.1)

        equipment {
            mainhand(Material.NETHERITE_AXE)
            offhand(Material.AIR)
            setArmor(arrayOf(40, 32, 48), trim)
        }

    }

}