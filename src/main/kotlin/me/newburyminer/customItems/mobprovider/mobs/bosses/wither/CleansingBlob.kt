package me.newburyminer.customItems.mobprovider.mobs.bosses.wither

import me.newburyminer.customItems.entity.components.bosses.WitherBlobComponent
import me.newburyminer.customItems.entity.components.spells.HealthThresholdComponent
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
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
import me.newburyminer.customItems.mobprovider.ability.spell.MultiMissileAbility
import me.newburyminer.customItems.mobprovider.ability.spell.SummonerAbility
import me.newburyminer.customItems.mobprovider.mobs.blackstone.FireBombardier
import me.newburyminer.customItems.mobprovider.mobs.coldocean.Buccaneer
import me.newburyminer.customItems.mobprovider.mobs.coldocean.HoppingExplosives
import me.newburyminer.customItems.mobprovider.mobs.coldocean.PirateMachineGunner
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.entity.MagmaCube
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

object CleansingBlob : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.BLACKSTONE
    override val trim: TrimPattern = TrimPattern.EYE
	override val tier: MobTier = MobTier.MINIBOSS
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.MAGMA_CUBE) {

        component(
            WitherBlobComponent()
        )

        health(
            linear(200.0 to 300.0, ctx)
        )

        movementSpeed(
            linear(1.1 to 1.3, ctx)
        )

        scale(
            4.0
        )

        apply {
            (this as MagmaCube).size = 1
            this.setAI(false)
        }

    }

}