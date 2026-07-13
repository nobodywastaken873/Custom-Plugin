package me.newburyminer.customItems.mobprovider.mobs.desert

import me.newburyminer.customItems.entity.components.spells.LeapComponent
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaEffectApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.BasicSlashAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectMissileAbility
import me.newburyminer.customItems.mobprovider.ability.spell.SummonerAbility
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffectType

object RobedSkirmisher : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.DESERT
    override val trim: TrimPattern = TrimPattern.SILENCE
	override val tier: MobTier = MobTier.MINIBOSS
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.PARCHED) {

        ability(
            MeleeEffectAbility(
                damage(linear(35.0 to 65.0, ctx), CustomDamageType.MELEE_NO_CD),
                VanillaKnockbackApply()
            )
        )

        ability(
            BasicSlashAbility(
                linear(2 to 4, ctx),
                linear(20.0 to 40.0, ctx),
                linear(100 to 80, ctx),
                ParticleTheme.DESERT,
                VanillaEffectApply(PotionEffectType.SLOWNESS, linear(40 to 80, ctx), 0),
            )
        )

        component(
            LeapComponent(
                linear(8.0 to 10.0, ctx),
                1.2,
                linear(100 to 80, ctx),
            )
        )

        ability(
            SummonerAbility(
                linear(3 to 6, ctx),
                ScarabBeetleBomber,
                linear(30 to 20, ctx),
                linear(300 to 250, ctx),
                ParticleTheme.DESERT
            )
        )

        ability(
            SummonerAbility(
                linear(2 to 4, ctx),
                RobedArcher,
                linear(30 to 20, ctx),
                linear(300 to 250, ctx),
                ParticleTheme.DESERT
            )
        )

        ability(
            EffectMissileAbility(
                linear(30.0 to 40.0, ctx),
                0.05,
                linear(1.0 to 1.5, ctx),
                linear(30 to 20, ctx),
                linear(150 to 120, ctx),
                ParticleTheme.DESERT,
                attribute(Attribute.ATTACK_DAMAGE, -3.0, AttributeModifier.Operation.ADD_NUMBER, linear(50 to 80, ctx))
            )
        )

        health(
            linear(800.0 to 1600.0, ctx)
        )

        movementSpeed(
            linear(1.2 to 1.5, ctx)
        )

        scale(1.3)

        equipment {
            mainhand(Material.GOLDEN_SWORD)
            offhand(Material.BREEZE_ROD)
            setArmor(colorTheme.color, trim)
        }

    }

}