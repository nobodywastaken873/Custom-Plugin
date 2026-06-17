package me.newburyminer.customItems.commands

import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.UseCooldown
import me.newburyminer.customItems.CustomItems
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.addElytraComponent
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.Utils.Companion.basePotion
import me.newburyminer.customItems.Utils.Companion.crossbowProj
import me.newburyminer.customItems.Utils.Companion.decodeToDoubleArray
import me.newburyminer.customItems.Utils.Companion.ench
import me.newburyminer.customItems.Utils.Companion.firework
import me.newburyminer.customItems.Utils.Companion.getDifficultyIndex
import me.newburyminer.customItems.Utils.Companion.horn
import me.newburyminer.customItems.Utils.Companion.maxDura
import me.newburyminer.customItems.Utils.Companion.maxStack
import me.newburyminer.customItems.Utils.Companion.omimous
import me.newburyminer.customItems.Utils.Companion.removeTag
import me.newburyminer.customItems.Utils.Companion.storeEnch
import me.newburyminer.customItems.Utils.Companion.toByteArray
import me.newburyminer.customItems.Utils.Companion.trim
import me.newburyminer.customItems.Utils.Companion.unb
import me.newburyminer.customItems.effects.AttributeData
import me.newburyminer.customItems.effects.CustomEffectType
import me.newburyminer.customItems.effects.EffectData
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.LavaOnDeath
import me.newburyminer.customItems.entity.components.MeleeCustomHit
import me.newburyminer.customItems.entity.components.creepers.ArrowBombCreeper
import me.newburyminer.customItems.entity.components.creepers.CustomExplosionCreeper
import me.newburyminer.customItems.entity.components.creepers.PreIgniteCreeper
import me.newburyminer.customItems.entity.components.creepers.TntHeadCreeper
import me.newburyminer.customItems.entity.components.projectiles.CustomDamageProjectile
import me.newburyminer.customItems.entity.components.projectiles.TntHeadTnt
import me.newburyminer.customItems.entity.components.projectileshooters.CancelProjectiles
import me.newburyminer.customItems.entity.components.projectileshooters.ElytraBreakerShooter
import me.newburyminer.customItems.entity.components.projectileshooters.ExplosiveProjectileShooter
import me.newburyminer.customItems.entity.components.projectileshooters.HomingProjectileShooter
import me.newburyminer.customItems.entity.components.projectileshooters.ProjectileDamageShooter
import me.newburyminer.customItems.entity.components.projectileshooters.SniperProjectileShooter
import me.newburyminer.customItems.entity.components.spells.LeapComponent
import me.newburyminer.customItems.entity.components.spells.TeleportBehindComponent
import me.newburyminer.customItems.entity.components.utils.ProjectileType
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import me.newburyminer.customItems.entity.hiteffects.effect.CustomEffectApply
import me.newburyminer.customItems.entity.hiteffects.effect.CustomKnockbackApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.eventbus.EventRegistry
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.CustomEffects
import net.kyori.adventure.key.Key
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.block.data.type.TNT
import org.bukkit.damage.DamageType
import org.bukkit.entity.Creeper
import org.bukkit.entity.Player
import org.bukkit.entity.Skeleton
import org.bukkit.entity.TNTPrimed
import org.bukkit.entity.Zombie
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionType
import org.bukkit.util.Vector

class TestCommand : BasicCommand {
    override fun execute(stack: CommandSourceStack, args: Array<out String>) {
        if (stack.sender !is Player) return
        val sender = stack.sender as Player
        if (!sender.isOp) {sender.sendMessage(
            Utils.text("You do not have permission to use this command.", arrayOf(255, 0, 0)))
            return
        }
        //if (args[0].isEmpty()) {sender.velocity = sender.velocity.add(Vector(0, 100, 0)); return false}
        //if (args.size != 1) return false
        if (args[0] == "sphere") {
            CustomEffects.particleSphere(Particle.ENCHANTED_HIT.builder(), sender.location, 3.0, 50.0)
        } else if (args[0] == "crossbow") {
            sender.inventory.addItem(ItemStack(Material.CROSSBOW).crossbowProj(ItemStack(Material.ARROW), args[1].toInt()))
        } else if (args[0] == "damage") {
            val newMeta = sender.inventory.itemInMainHand.itemMeta as Damageable
            newMeta.damage = 2030
            sender.inventory.itemInMainHand.itemMeta = newMeta
        } else if (args[0] == "newworld") {
            val world = WorldCreator("testworld")
            world.type(WorldType.FLAT)
            val newWorld = Bukkit.createWorld(world)
            sender.teleport(Location(newWorld, 0.0, 100.0, 0.0))
        } else if (args[0] == "convert") {
            sender.sendMessage(doubleArrayOf(0.0, 100.5, 123.41, 34978124.2413421).contentToString())
            sender.sendMessage(doubleArrayOf(0.0, 100.5, 123.41, 34978124.2413421).toByteArray().decodeToDoubleArray().contentToString())
        } else if (args[0] == "tagtest") {
            sender.removeTag("gravelist")
        } else if (args[0] == "crossy") {
            sender.addItemorDrop(ItemStack(Material.CROSSBOW).ench("MS100"))
        } else if (args[0] == "cooldown") {
            sender.setCooldown(sender.inventory.itemInMainHand, 20)
        } else if (args[0] == "difficulty") {
            sender.sendMessage(sender.world.getDifficultyIndex(sender.location).toString())
        } else if (args[0] == "tpme") {
            val newdimension = CustomItems.plugin.server.getWorld(Key.key("minecraft:new_dimension"))
            sender.teleport(Location(newdimension, sender.location.x, sender.location.y, sender.location.z))
        } else if (args[0] == "cd1") {
            val item1 = ItemStack(Material.LEATHER)
            item1.setData(DataComponentTypes.USE_COOLDOWN, UseCooldown.useCooldown(0.01F).cooldownGroup(Key.key("customitems", "leatherabcdefg")))
            sender.addItemorDrop(item1)
            val item2 = ItemStack(Material.IRON_INGOT)
            item2.setData(DataComponentTypes.USE_COOLDOWN, UseCooldown.useCooldown(0.01F).cooldownGroup(Key.key("customitems", "leatherabcdefg")))
            sender.addItemorDrop(item2)
        } else if (args[0] == "cd2") {
            sender.setCooldown(Key.key("customitems", "leatherabcdefg"), 100)
        } else if (args[0] == "trim") {
            val item = sender.inventory.itemInMainHand
            sender.sendMessage(item.getData(DataComponentTypes.TRIM).toString())
            sender.sendMessage(item.getData(DataComponentTypes.PROVIDES_TRIM_MATERIAL).toString())
        } else if (args[0] == "retest_utils") {
            sender.addItemorDrop(ItemStack(Material.DIAMOND_CHESTPLATE).maxDura(10000))
            sender.addItemorDrop(ItemStack(Material.DIAMOND_CHESTPLATE).unb())
            sender.addItemorDrop(ItemStack(Material.DIAMOND_CHESTPLATE, 5).maxStack(10))
            sender.addItemorDrop(ItemStack(Material.DIAMOND_CHESTPLATE).trim(ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.TIDE)))
            sender.addItemorDrop(ItemStack(Material.ENCHANTED_BOOK).storeEnch("EF5","UN3","SH2"))
            sender.addItemorDrop(ItemStack(Material.FIREWORK_ROCKET).firework(10, FireworkEffect.builder().withColor(Color.RED).build()))
            sender.addItemorDrop(ItemStack(Material.OMINOUS_BOTTLE).omimous(2))
            sender.addItemorDrop(ItemStack(Material.SPLASH_POTION).basePotion(PotionType.HEALING))
            sender.addItemorDrop(ItemStack(Material.GOAT_HORN).horn(MusicInstrument.DREAM_GOAT_HORN))
            sender.addItemorDrop(ItemStack(Material.DIAMOND_CHESTPLATE).addElytraComponent())
        } else if (args[0] == "launch_up") {
            sender.velocity = sender.velocity.add(Vector(0.0, 0.4, 0.0))
        } else if (args[0] == "vanilla_knockback") {
            val damageSettings = HitEffects(VanillaKnockbackApply())
            damageSettings.apply(sender, sender)
        } else if (args[0] == "lava_on_death") {
            val zombie = sender.world.spawn(sender.location, Zombie::class.java)
            EntityWrapperManager.getWrapperorNew(zombie).addComponent(LavaOnDeath())
            //damageSettings.apply(sender, sender)
        } else if (args[0] == "cancel_projectiles") {
            val zombie = sender.world.spawn(sender.location, Skeleton::class.java)
            EntityWrapperManager.getWrapperorNew(zombie).addComponent(CancelProjectiles())
        } else if (args[0] == "check_event_count") {
            println(EventRegistry.getAllRegisteredListeners()[EntityDeathEvent::class]?.size)
        } else if (args[0] == "summon_test") {
            val component = when (args[1].toInt()) {
                0 -> {
                    MeleeCustomHit(HitEffects(VanillaKnockbackApply(), CustomDamageApply(20.0, CustomDamageType.EXPLOSION)))
                }
                1 -> {
                    LeapComponent(8.0, 2.0, 5 * 20)
                }
                2 -> {
                    TeleportBehindComponent(10 * 20)
                }
                3 -> {
                    PreIgniteCreeper(8.0)
                }
                /*0 -> { TntHeadCreeper(sender.world.spawn(sender.location, TNTPrimed::class.java) { EntityWrapperManager.getWrapperorNew(it).addComponent(TntHeadTnt(2.0, 5.0F, true))}, 5.0F)  }
                1 -> { CancelProjectiles() }
                2 -> { ExplosiveProjectileShooter(5.0F, true) }
                3 -> { ElytraBreakerShooter(HitEffects(CustomDamageApply(20.0, CustomDamageType.EXPLOSION), CustomKnockbackApply(Vector(0.0, -10.0, 0.0))), 10*20, 20*20) }
                4 -> { HomingProjectileShooter(0.1) }
                5 -> { ProjectileDamageShooter(HitEffects(VanillaKnockbackApply(), CustomDamageApply(20.0,
                    CustomDamageType.EXPLOSION))) }
                6 -> { SniperProjectileShooter(10*20, ProjectileType.ARROW) }
                7 -> {
                    ArrowBombCreeper(
                        100, HitEffects(
                            CustomDamageApply(15.0, CustomDamageType.PROJECTILE_NO_CD, 0),
                            CustomEffectApply(CustomEffectType.ATTRIBUTE, EffectData(40,
                                AttributeData(-2.0, Attribute.ARMOR, AttributeModifier.Operation.ADD_NUMBER))
                            ),
                            VanillaKnockbackApply()))
                }*/
                /*0 -> {
                    ArrowBombCreeper(
                        100, HitEffects(
                            CustomDamageApply(15.0, DamageType.ARROW, 0),
                            CustomEffectApply(CustomEffectType.ATTRIBUTE, EffectData(40,
                                AttributeData(-2.0, Attribute.ARMOR, AttributeModifier.Operation.ADD_NUMBER))
                            )))
                }
                1 -> {
                    BreachingCreeper(5.0)
                }
                2 -> {
                    ChainExplosionCreeper()
                }
                3 -> {
                    CustomExplosionCreeper(5F, false, true)
                }
                4 -> {
                    FirebombCreeper(0.8)
                }
                5 -> {
                    FireworkCreeper(10, 30.0)
                }
                6 -> {
                    HoppingCreeper()
                }
                7 -> {
                    PotionExplosionCreeper(PotionEffectType.POISON, 40, 2)
                }
                8 -> {
                    PreIgniteCreeper(8.0)
                }*/
                else -> {
                    return
                }
            }

            sender.world.spawn(
                sender.location,
                Creeper::class.java
            ) {
                val wrapper = EntityWrapperManager.getWrapperorNew(it)
                wrapper.addComponent(component)
                wrapper.addComponent(CustomExplosionCreeper(3F, false, true))
            }
        } else if (args[0] == "eventbus") {
            println(EventRegistry.getAllRegisteredListeners())
        } else if (args[0] == "kbtest") {
            when (args[1].toInt()) {
                1 -> {
                    sender.world.spawn(
                        sender.location,
                        Zombie::class.java
                    ) {
                        val wrapper = EntityWrapperManager.getWrapperorNew(it)
                        wrapper.addComponent(MeleeCustomHit(HitEffects(CustomDamageApply(1.0, CustomDamageType.EXPLOSION_NO_CD), VanillaKnockbackApply())))
                    }
                }
                2 -> {
                    sender.world.spawn(
                        sender.location,
                        Skeleton::class.java
                    ) {
                        val wrapper = EntityWrapperManager.getWrapperorNew(it)
                        wrapper.addComponent(ProjectileDamageShooter(HitEffects(VanillaKnockbackApply(), CustomDamageApply(1.0, CustomDamageType.EXPLOSION_NO_CD))))
                    }
                }
            }
        } else if (args[0] == "damageapply") {
            val effects = HitEffects(CustomDamageApply(1.0, CustomDamageType.EXPLOSION_NO_CD), VanillaKnockbackApply())
            val hitter = sender.world.spawn(sender.location.add(Vector(0, 0, 5)), Zombie::class.java)
            effects.apply(sender, hitter)
        }
    }
}