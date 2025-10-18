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
import me.newburyminer.customItems.helpers.CustomEffects
import net.kyori.adventure.key.Key
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionType

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
            CustomEffects.particleSphere(Particle.ENCHANTED_HIT.builder(), sender.location, 3.0, 50)
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
        }
    }
}