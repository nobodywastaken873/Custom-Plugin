package me.newburyminer.customItems.mobprovider.mobs.blackstone

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
import me.newburyminer.customItems.mobprovider.ability.defensive.BasicDodgeAbility
import me.newburyminer.customItems.mobprovider.ability.spell.EffectAuraAbility
import me.newburyminer.customItems.mobprovider.ability.spell.HealerAbility
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.entity.EntityType
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffectType

object SoulMage : MobDefinition() {

    override val colorTheme: ColorTheme = ColorTheme.BLACKSTONE
    override val trim: TrimPattern = TrimPattern.DUNE
	override val tier: MobTier = MobTier.ELITE
    override fun build(ctx: MobContext): MobBuilder = mob(EntityType.HUSK) {

        ability(
            MeleeEffectAbility(
                damage(linear(23.0 to 46.0, ctx), CustomDamageType.MELEE_NO_CD),
                attribute(Attribute.MAX_HEALTH, -1.0, Operation.ADD_NUMBER, linear(40 to 80, ctx)),
                VanillaKnockbackApply()
            )
        )

        ability(
            HealerAbility(
                linear(20.0 to 40.0, ctx),
                linear(5.0 to 10.0, ctx),
                0.0,
                linear(40 to 30, ctx),
                linear(200 to 150, ctx),
                attribute(Attribute.ARMOR, 10.0, Operation.ADD_NUMBER, 200)
            )
        )

        ability(
            BasicDodgeAbility(
                linear(0.2 to 0.4, ctx),
            )
        )

        ability(
            EffectAuraAbility(
                linear(3.0 to 4.5, ctx),
                1.0,
                ParticleTheme.BLACKSTONE,
                20,
                VanillaEffectApply(PotionEffectType.WEAKNESS, linear(40 to 80, ctx), 0)
            )
        )

        health(
            linear(300.0 to 600.0, ctx)
        )

        movementSpeed(
            linear(0.6 to 0.8, ctx)
        )

        equipment {
            mainhand(Material.NETHERITE_SWORD)
            offhand(Material.GOLDEN_APPLE)
            setArmor(colorTheme.color, trim)
        }
        
    }

}