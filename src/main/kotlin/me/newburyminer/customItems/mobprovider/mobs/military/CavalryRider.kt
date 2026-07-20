package me.newburyminer.customItems.mobprovider.mobs.military

import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.ColorTheme
import me.newburyminer.customItems.mobprovider.MobBuilder
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.MobDefinition
import me.newburyminer.customItems.mobprovider.MobTier
import me.newburyminer.customItems.mobprovider.ability.MeleeEffectAbility
import me.newburyminer.customItems.mobprovider.ability.spell.ExplosiveGrenadeAbility
import me.newburyminer.customItems.mobprovider.ability.spell.SummonerAbility
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack

object CavalryRider : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.MILITARY
	override val tier: MobTier = MobTier.MINIBOSS
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.VINDICATOR) {

        ability(
            MeleeEffectAbility(
                damage(linear(30.0 to 60.0, ctx), CustomDamageType.MELEE),
                VanillaKnockbackApply()
            )
        )

        ability(
            SummonerAbility(
                linear(3 to 6, ctx),
                Infantryman,
                linear(60 to 50, ctx),
                linear(400 to 350, ctx),
                ParticleTheme.MILITARY
            )
        )

        ability(
            SummonerAbility(
                linear(2 to 4, ctx),
                AttackHound,
                linear(60 to 50, ctx),
                linear(400 to 350, ctx),
                ParticleTheme.MILITARY
            )
        )

        ability(
            SummonerAbility(
                linear(2 to 4, ctx),
                ArmoredKnight,
                linear(60 to 50, ctx),
                linear(400 to 350, ctx),
                ParticleTheme.MILITARY
            )
        )

        ability(
            ExplosiveGrenadeAbility(
                linear(10.0 to 15.0, ctx),
                2.5,
                Material.GRAY_CONCRETE_POWDER,
                linear(4.5 to 7.5, ctx),
                linear(2 to 3, ctx),
                linear(200 to 150, ctx)
            )
        )

        health(
            linear(500.0 to 1000.0, ctx)
        )

        movementSpeed(
            linear(1.2 to 1.4, ctx)
        )

        equipment {
            mainhand(Material.NETHERITE_SPEAR)
        }

    }

}