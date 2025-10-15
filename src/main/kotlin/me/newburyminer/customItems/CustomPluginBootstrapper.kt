package me.newburyminer.customItems

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.datapack.DatapackRegistrar
import io.papermc.paper.plugin.bootstrap.BootstrapContext
import io.papermc.paper.plugin.bootstrap.PluginBootstrap
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager
import io.papermc.paper.plugin.lifecycle.event.registrar.RegistrarEvent
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import io.papermc.paper.registry.data.EnchantmentRegistryEntry
import io.papermc.paper.registry.event.RegistryEvents
import io.papermc.paper.registry.event.RegistryFreezeEvent
import io.papermc.paper.registry.keys.EnchantmentKeys
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys
import me.newburyminer.customItems.Utils.Companion.addItemorDrop
import me.newburyminer.customItems.Utils.Companion.afkTime
import me.newburyminer.customItems.Utils.Companion.isAfk
import me.newburyminer.customItems.Utils.Companion.isInCombat
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.commands.*
import me.newburyminer.customItems.entities.CustomEntity
import me.newburyminer.customItems.items.CustomItem
import me.newburyminer.customItems.items.ItemRegistry
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.inventory.EquipmentSlotGroup
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import java.util.*


class CustomPluginBootstrapper: PluginBootstrap {
    override fun bootstrap(context: BootstrapContext) {
        val manager: LifecycleEventManager<BootstrapContext> = context.getLifecycleManager()
        manager.registerEventHandler(LifecycleEvents.COMMANDS) {
            event: ReloadableRegistrarEvent<Commands> ->
            val commands = event.registrar()
            commands.register(Commands.literal("custom").then(
                Commands.argument("itemargument", CustomItemArgument()).executes {
                ctx: CommandContext<CommandSourceStack> ->
                    customCommandExecutor(ctx.source.sender, ctx.getArgument("itemargument", CustomItem::class.java))
                    Command.SINGLE_SUCCESS
            }).build(),
                "Operator command used to get custom items.",
                listOf()
            )
            commands.register(Commands.literal("customentity").then(
                Commands.argument("entityargument", CustomEntityArgument()).executes {
                        ctx: CommandContext<CommandSourceStack> ->
                    customEntityExecutor(ctx.source.sender, ctx.getArgument("entityargument", CustomEntity::class.java))
                    Command.SINGLE_SUCCESS
                }).build(),
                "Operator command used to spawn custom mobs.",
                listOf()
            )
            commands.register(Commands.literal("afk").then(
                Commands.argument("afkoptions", AfkArgument()).executes {
                    ctx: CommandContext<CommandSourceStack> ->
                        afkCommandExecutor(ctx.source.sender, ctx.getArgument("afkoptions", AfkOptions::class.java))
                        Command.SINGLE_SUCCESS
            }).build(),
                "Command used to start/end afk sessions.",
                listOf()
            )
            commands.register("craft", "Use to open the custom crafting GUI.", Craft())
            commands.register("recipe", "Use to open the custom recipe GUI.", RecipeCommand())
            commands.register("test", "For testing purposes.", TestCommand())
            commands.register("graves", "Use to open a list of your graves.", GraveCommand())
            commands.register("restoregrave", "Use to restore a grave from the stored graves file.", RestoreGraveCommand())
            commands.register("info", "Use to view all info about what this plugin changes.", InfoCommand())
        }
        manager.registerEventHandler(RegistryEvents.ENCHANTMENT.freeze().newHandler {
            event: RegistryFreezeEvent<Enchantment, EnchantmentRegistryEntry.Builder> ->
            event.registry().register( // The key of the registry
                // Plugins should use their own namespace instead of minecraft or papermc
                EnchantmentKeys.create(Key.key("customitems:autosmelt")))
            { b: EnchantmentRegistryEntry.Builder ->
                b.description(Component.text("Autosmelt"))
                    .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.PICKAXES))
                    .anvilCost(1)
                    .maxLevel(1)
                    .weight(1024)
                    .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
                    .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(3, 1))
                    .activeSlots(EquipmentSlotGroup.MAINHAND)
            }
            event.registry().register( // The key of the registry
                // Plugins should use their own namespace instead of minecraft or papermc
                EnchantmentKeys.create(Key.key("customitems:soulbound")))
            { b: EnchantmentRegistryEntry.Builder ->
                b.description(Component.text("Soulbound"))
                    .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.DIRT))
                    .anvilCost(1)
                    .maxLevel(1)
                    .weight(1024)
                    .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
                    .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(3, 1))
                    .activeSlots(EquipmentSlotGroup.ANY)
            }
            event.registry().register( // The key of the registry
                // Plugins should use their own namespace instead of minecraft or papermc
                EnchantmentKeys.create(Key.key("customitems:fireproof")))
            { b: EnchantmentRegistryEntry.Builder ->
                b.description(Component.text("Fireproof"))
                    .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.DIRT))
                    .anvilCost(1)
                    .maxLevel(1)
                    .weight(1024)
                    .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
                    .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(3, 1))
                    .activeSlots(EquipmentSlotGroup.ANY)
            }
            event.registry().register( // The key of the registry
                // Plugins should use their own namespace instead of minecraft or papermc
                EnchantmentKeys.create(Key.key("customitems:blast_resistant")))
            { b: EnchantmentRegistryEntry.Builder ->
                b.description(Component.text("Blast Resistant"))
                    .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.DIRT))
                    .anvilCost(1)
                    .maxLevel(1)
                    .weight(1024)
                    .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
                    .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(3, 1))
                    .activeSlots(EquipmentSlotGroup.ANY)
            }
            event.registry().register( // The key of the registry
                // Plugins should use their own namespace instead of minecraft or papermc
                EnchantmentKeys.create(Key.key("customitems:reinforced")))
            { b: EnchantmentRegistryEntry.Builder ->
                b.description(Component.text("Reinforced"))
                    .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.DIRT))
                    .anvilCost(1)
                    .maxLevel(5)
                    .weight(1024)
                    .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
                    .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(3, 1))
                    .activeSlots(EquipmentSlotGroup.ANY)
            }
            event.registry().register( // The key of the registry
                // Plugins should use their own namespace instead of minecraft or papermc
                EnchantmentKeys.create(Key.key("customitems:duplicate")))
            { b: EnchantmentRegistryEntry.Builder ->
                b.description(Component.text("Duplicate"))
                    .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.DIRT))
                    .anvilCost(1)
                    .maxLevel(1)
                    .weight(1024)
                    .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
                    .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
                    .activeSlots(EquipmentSlotGroup.ANY)
            }

        })
        manager.registerEventHandler<RegistrarEvent<DatapackRegistrar>>(
            LifecycleEvents.DATAPACK_DISCOVERY
        ) { event: RegistrarEvent<DatapackRegistrar> ->
            val registrar = event.registrar()
            try {
                val uri: URI = Objects.requireNonNull(
                    CustomItems::class.java.getResource("/pack/CustomWorldgen")
                ).toURI()
                registrar.discoverPack(uri, "customworld")
            } catch (e: URISyntaxException) {
                throw RuntimeException(e)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }

    private fun afkCommandExecutor(sender: CommandSender, afkOptions: AfkOptions) {
        if (sender !is Player) return
        when (afkOptions) {
            AfkOptions.START -> {
                if (sender.isInCombat()) {
                    sender.sendMessage(Utils.text("You cannot AFK while in combat.", Utils.FAILED_COLOR))
                    return
                }
                if (sender.world == CustomItems.bossWorld || sender.world == CustomItems.aridWorld) {
                    sender.sendMessage(Utils.text("You cannot AFK here.", Utils.FAILED_COLOR))
                    return
                }
                if (!sender.isOnGround) {
                    sender.sendMessage(Utils.text("You are not on the ground.", Utils.FAILED_COLOR))
                    return
                }
                if (sender.isAfk()) {
                    sender.sendMessage(Utils.text("You are already AFK.", Utils.FAILED_COLOR))
                    return
                }
                sender.setTag("isafk", true)
                sender.setTag("afklocation", sender.location)
                sender.playSound(sender, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0F, 1.0F)
                sender.sendMessage(Utils.text("You are now AFK.", Utils.SUCCESS_COLOR))
            }
            AfkOptions.END -> {
                if (!sender.isAfk()) {
                    sender.sendMessage(Utils.text("You are not AFK.", Utils.FAILED_COLOR))
                    return
                }
                sender.setTag("isafk", false)
                sender.setTag("afktime", 0)
                sender.playSound(sender, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0F, 1.0F)
                sender.sendMessage(Utils.text("You are not AFK anymore.", Utils.SUCCESS_COLOR))
            }
            AfkOptions.TIME -> {
                if (!sender.isAfk()) {
                    sender.sendMessage(Utils.text("You are not AFK.", Utils.FAILED_COLOR))
                    return
                }
                sender.sendMessage(Utils.text("You have been AFK for ${sender.afkTime() / 20 / 60} minutes, ${sender.afkTime() / 20 % 60} seconds.", Utils.SUCCESS_COLOR))
            }
            AfkOptions.CHECKAFK -> {
                for (player in Bukkit.getServer().onlinePlayers) {
                    if (player.isAfk()) {
                        sender.sendMessage(Utils.text("${player.name} - AFK", arrayOf(156, 154, 152)))
                    } else {
                        sender.sendMessage(Utils.text("${player.name} - ONLINE", Utils.SUCCESS_COLOR))
                    }
                }
            }

        }
    }
    private fun customCommandExecutor(sender: CommandSender, item: CustomItem) {
        if (sender !is Player) return
        if (!sender.isOp) {
            sender.sendMessage(Utils.text("You do not have permission to use this command.", arrayOf(255, 0, 0)))
            return
        }
        if (item == CustomItem.ALL) {
            for (customItem in CustomItem.entries) {
                sender.addItemorDrop(ItemRegistry.get(customItem))
            }
        } else {
            sender.addItemorDrop(ItemRegistry.get(item))
        }
    }
    private fun customEntityExecutor(sender: CommandSender, entity: CustomEntity) { // add another difficulty argument
        if (sender !is Player) return
        if (!sender.isOp) {
            sender.sendMessage(Utils.text("You do not have permission to use this command.", arrayOf(255, 0, 0)))
            return
        }
        val type = when (entity) {
             in arrayOf(
                 CustomEntity.LEAPING_CREEPER, CustomEntity.FIREBOMB_CREEPER, CustomEntity.BREACHING_CREEPER, CustomEntity.FIREWORK_CREEPER, CustomEntity.ARROWBOMB_CREEPER,
                        CustomEntity.CHAIN_REACTION_CREEPER, CustomEntity.TNTHEAD_CREEPER, CustomEntity.SHIELD_BREAKER_CREEPER, CustomEntity.NUCLEAR_CREEPER, CustomEntity.PREIGNITION_CREEPER,
                        CustomEntity.HOPPING_CREEPER, CustomEntity.MINI_BREACHING_CREEPER, CustomEntity.BABY_CREEPER)
                -> Creeper::class.java
            in arrayOf(
                CustomEntity.HOMING_SKELETON, CustomEntity.EXPLOSIVE_SKELETON, CustomEntity.SWORDSMAN_SKELETON,
                CustomEntity.SNIPER_SKELETON, CustomEntity.ENERGY_SHIELD_SKELETON, CustomEntity.MACHINE_GUN_SKELETON,
                       CustomEntity.SHIELD_BREAKER_SKELETON, CustomEntity.BABY_SKELETON, CustomEntity.ELYTRA_BREAKER_SKELETON, CustomEntity.SWARMER_SKELETON)
                -> Skeleton::class.java
            in arrayOf(
                CustomEntity.JUMPING_ZOMBIE, CustomEntity.INFECTIOUS_ZOMBIE, CustomEntity.SHADOW_ASSASSIN_ZOMBIE,
                CustomEntity.TANK_ZOMBIE, CustomEntity.SWARMER_ZOMBIE
            )
                -> Zombie::class.java
            in arrayOf(
                CustomEntity.CAVE_BROODMOTHER_SPIDER, CustomEntity.BROODMOTHER_SPIDER, CustomEntity.SWARMER_SPIDER,
                CustomEntity.TARANTULA_SPIDER, CustomEntity.LEAPING_SPIDER, CustomEntity.WEAVER_SPIDER
            )
                -> Spider::class.java
            in arrayOf(
                CustomEntity.SWARMER_SLIME, CustomEntity.LEAPING_SLIME, CustomEntity.REPLICATING_SLIME, CustomEntity.LAUNCHING_SLIME
            )
                -> Slime::class.java
            in arrayOf(
                CustomEntity.LAUNCHING_CUBE, CustomEntity.LAVA_CUBE, CustomEntity.SWARMER_CUBE, CustomEntity.LEAPING_CUBE
            )
                -> MagmaCube::class.java
            in arrayOf(
                CustomEntity.ENDER_WITCH, CustomEntity.SNIPER_WITCH, CustomEntity.BIOWEAPON_WITCH, CustomEntity.CLERIC_WITCH, CustomEntity.COLONIEL_WITCH
            )
                -> Witch::class.java
            else -> Zombie::class.java
        }
        CustomEntity.convert(sender.location.world.spawn(sender.location, type), entity)
        //sender.addItemorDrop(Items.get(item))
    }
}