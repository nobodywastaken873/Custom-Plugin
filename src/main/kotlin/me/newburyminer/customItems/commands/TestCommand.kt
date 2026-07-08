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
import me.newburyminer.customItems.bosses.rendering.HollowCylinderRenderable
import me.newburyminer.customItems.bosses.rendering.QuadRenderable
import me.newburyminer.customItems.bosses.rendering.RectangularPrismRenderable
import me.newburyminer.customItems.bosses.rendering.RenderManager
import me.newburyminer.customItems.bosses.rendering.Transform
import me.newburyminer.customItems.bosses.rendering.combinator.CylinderCombinator
import me.newburyminer.customItems.bosses.rendering.combinator.FloorCombinator
import me.newburyminer.customItems.bosses.rendering.combinator.PrismCombinator
import me.newburyminer.customItems.bosses.rendering.floor.FloorPatternRenderer
import me.newburyminer.customItems.bosses.rendering.floor.GreedyShapeMesher
import me.newburyminer.customItems.bosses.rendering.shapes.Circle
import me.newburyminer.customItems.bosses.rendering.shapes.NegativeShape
import me.newburyminer.customItems.bosses.rendering.shapes.Polygon
import me.newburyminer.customItems.bosses.rendering.shapes.ShapeLayer
import me.newburyminer.customItems.entity.EntityWrapperManager
import me.newburyminer.customItems.entity.components.LavaOnDeath
import me.newburyminer.customItems.entity.components.melee.MeleeCustomHit
import me.newburyminer.customItems.entity.components.projectileshooters.CancelProjectiles
import me.newburyminer.customItems.entity.components.projectileshooters.ProjectileDamageShooter
import me.newburyminer.customItems.entity.components.spells.EffectAuraCaster
import me.newburyminer.customItems.entity.hiteffects.HitEffects
import me.newburyminer.customItems.entity.hiteffects.effect.CustomDamageApply
import me.newburyminer.customItems.entity.hiteffects.effect.VanillaKnockbackApply
import me.newburyminer.customItems.eventbus.EventRegistry
import me.newburyminer.customItems.helpers.CustomDamageType
import me.newburyminer.customItems.helpers.CustomEffects
import me.newburyminer.customItems.helpers.ParticleTheme
import me.newburyminer.customItems.mobprovider.MobContext
import me.newburyminer.customItems.mobprovider.mobs.military.AttackHound
import me.newburyminer.customItems.mobprovider.mobs.military.BattleMedic
import me.newburyminer.customItems.mobprovider.mobs.military.TraineeFighter
import me.newburyminer.customItems.mobprovider.mobs.military.WalkingExplosives
import net.kyori.adventure.key.Key
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.entity.Skeleton
import org.bukkit.entity.Zombie
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionType
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

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
            val definition = when (args[1].toInt()) {
                0 -> {
                    TraineeFighter
                }
                1 -> {
                    AttackHound
                }
                2 -> {
                    BattleMedic
                }
                3 -> {
                    WalkingExplosives
                }
                else -> {
                    return
                }
            }

            val ctx = MobContext(sender.location.length(), false, sender.location.add(0.0, 0.0, 2.0))
            definition.build(ctx).createEntity(ctx)
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
        } else if (args[0] == "spellcomponent") {
            val component = EffectAuraCaster(3.0, 0.5, 100, 40, 40,
                HitEffects(CustomDamageApply(10.0, CustomDamageType.PROJECTILE), VanillaKnockbackApply()),
                10, ParticleTheme.BASIC_THEME, 40, 200, 20.0)
                /*MagicMissileShooterComponent(30.0, 1.0, 0.25, 0.02, HomingSystem.Type.BOTH_SCALED,
                HitEffects(CustomDamageApply(10.0, CustomDamageType.PROJECTILE), VanillaKnockbackApply()),
                20, 200, ParticleTheme.BASIC_THEME)
                TrackingBeamComponent(30.0, 0.25, 0.01, 10,
                false, HitEffects(CustomDamageApply(10.0, CustomDamageType.PROJECTILE), VanillaKnockbackApply()),
                200, 0, ParticleTheme.BASIC_THEME)
                LaserBeamComponent(30.0, 0.25, true, false,
                HitEffects(CustomDamageApply(10.0, CustomDamageType.PROJECTILE), VanillaKnockbackApply()),
                40, 200, ParticleTheme.BASIC_THEME)*/
            sender.world.spawn(
                sender.location,
                Zombie::class.java
            ) {
                val wrapper = EntityWrapperManager.getWrapperorNew(it)
                wrapper.addComponent(component)
            }
        } else if (args[0] == "test_renderer") {

            val manager = RenderManager()
            Bukkit.getScheduler().runTaskTimer(CustomItems.plugin, Runnable {manager.tick()}, 1L, 1L)

            val cylinder = CylinderCombinator(
                Transform(
                    position = Vector(0.0, 100.0, 0.0),
                    Transform.lookRotation(Vector(0, 0, 1))
                ),
                radius = 1.0,
                length = 20.0,
                material = Material.LIGHT_BLUE_STAINED_GLASS,
                Particle.DUST.builder().color(255,0,0),
                0.5,
                sender.world
            )
            cylinder.spawn(Bukkit.getWorlds()[0])
            manager.add(
                cylinder
            )

            var material = 0
            Bukkit.getScheduler().runTaskTimer(CustomItems.plugin, Runnable {
                cylinder.transform.rotateWorldY(0.03F)
                //cylinder.length = abs(cos(angle) + 1) * 5.0
                //cylinder.radius = abs(sin(angle) + 2)

                val materialOrder = listOf(Material.RED_STAINED_GLASS, Material.ORANGE_STAINED_GLASS, Material.YELLOW_STAINED_GLASS, Material.LIME_STAINED_GLASS,
                    Material.GREEN_STAINED_GLASS, Material.CYAN_STAINED_GLASS, Material.BLUE_STAINED_GLASS, Material.PURPLE_STAINED_GLASS, Material.MAGENTA_STAINED_GLASS)

                if (Bukkit.getCurrentTick() % 5 == 0) {
                    cylinder.material = materialOrder[material]
                    material++
                    if (material !in materialOrder.indices) material = 0
                }

            }, 1L, 1L)

        } else if (args[0] == "display_block") {
            val manager = RenderManager()
            Bukkit.getScheduler().runTaskTimer(CustomItems.plugin, Runnable {manager.tick()}, 1L, 1L)

            val quad = QuadRenderable(
                origin = Vector(0, 100, 0),
                center = sender.location.toVector(),
                normal = Vector(0,1,1),
                up = Vector(0,1,0),
                width = 2.0F,
                height = 2.0F,
                thickness = 0.1F,
                material = Material.LIGHT_BLUE_STAINED_GLASS,
            )
            quad.spawn(sender.world)

            manager.add(
                quad
            )
        } else if (args[0] == "meshed_plane") {
            val manager = RenderManager()
            Bukkit.getScheduler().runTaskTimer(CustomItems.plugin, Runnable {manager.tick()}, 1L, 1L)

            val y = 100.0
            val circle = Circle(y, Vector(0, 100, 0), 6.0)
            val secondCircle = Circle(y, Vector(0, 100, 0), 6.0)
            val floorPattern = FloorCombinator(
                NegativeShape(
                    listOf(
                        ShapeLayer(Polygon(listOf(1 to 1, -1 to 1, 1 to -1, -1 to -1).map { Vector(it.first * 20.0, y, it.second * 20.0) }, y), ShapeLayer.Operation.ADD),
                        ShapeLayer(circle, ShapeLayer.Operation.SUBTRACT),
                        ShapeLayer(secondCircle, ShapeLayer.Operation.SUBTRACT)
                    ),
                    y
                ),
                0.5,
                Material.RED_CONCRETE,
                Particle.DUST.builder().color(255,0,0),
                0.5,
                sender.world
            )

            floorPattern.spawn(sender.world)
            manager.add(floorPattern)

            var angle = 0.0
            Bukkit.getScheduler().runTaskTimer(CustomItems.plugin, Runnable {
                angle += 0.03

                circle.circleCenter = Vector(10 * cos(angle), y, 10 * sin(angle))
                secondCircle.circleCenter = Vector(-10 * cos(angle), y, -10 * sin(angle))
            }, 1L, 1L)
        } else if (args[0] == "rectangular_prism") {
            val manager = RenderManager()
            Bukkit.getScheduler().runTaskTimer(CustomItems.plugin, Runnable {manager.tick()}, 1L, 1L)

            val rectangle = PrismCombinator(
                Transform(
                    position = Vector(0, 99, 0),
                    rotation = Transform.lookRotation(Vector(0, 0, 1), Vector(1, 1, 0))
                ),
                2.0F,
                2.0F,
                20.0F,
                Material.RED_CONCRETE,
                0.05F,
                Particle.DUST.builder().color(255,0,0),
                0.5,
                sender.world
            )

            val rectangle2 = PrismCombinator(
                Transform(
                    position = Vector(0, 99, 0),
                    rotation = Transform.lookRotation(Vector(0, 0, -1), Vector(-1, 1, 0))
                ),
                2.0F,
                2.0F,
                20.0F,
                Material.RED_CONCRETE,
                0.05F,
                Particle.DUST.builder().color(255,0,0),
                0.5,
                sender.world
            )

            rectangle.spawn(sender.world)
            rectangle2.spawn(sender.world)
            manager.add(rectangle)
            manager.add(rectangle2)

            Bukkit.getScheduler().runTaskTimer(CustomItems.plugin, Runnable {
                rectangle.transform.rotateWorldY(0.03F)
                rectangle2.transform.rotateWorldY(0.03F)
            }, 1L, 1L)
        }
    }
}