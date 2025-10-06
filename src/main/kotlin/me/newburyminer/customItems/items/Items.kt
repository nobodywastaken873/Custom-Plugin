package me.newburyminer.customItems.items

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import io.papermc.paper.datacomponent.item.TooltipDisplay
import io.papermc.paper.datacomponent.item.UseCooldown
import me.newburyminer.customItems.Utils
import me.newburyminer.customItems.Utils.Companion.attr
import me.newburyminer.customItems.Utils.Companion.consumable
import me.newburyminer.customItems.Utils.Companion.customModel
import me.newburyminer.customItems.Utils.Companion.duraBroken
import me.newburyminer.customItems.Utils.Companion.food
import me.newburyminer.customItems.Utils.Companion.lore
import me.newburyminer.customItems.Utils.Companion.loreBlock
import me.newburyminer.customItems.Utils.Companion.name
import me.newburyminer.customItems.Utils.Companion.noNoiseEquippable
import me.newburyminer.customItems.Utils.Companion.potionColor
import me.newburyminer.customItems.Utils.Companion.round
import me.newburyminer.customItems.Utils.Companion.setTag
import me.newburyminer.customItems.Utils.Companion.tagTool
import me.newburyminer.customItems.Utils.Companion.text
import me.newburyminer.customItems.Utils.Companion.unb
import me.newburyminer.customItems.items.Items.Companion.readName
import me.newburyminer.customItems.items.Items.Companion.trimToString
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.translation.GlobalTranslator
import net.kyori.adventure.translation.Translator
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Tag
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.w3c.dom.Attr
import java.util.*

class Items {
    companion object {
        private val items: MutableList<ItemStack> = mutableListOf()

        private fun addItem(item: ItemStack) {
            val description = item.lore()?.toMutableList() ?: mutableListOf()
            if (item.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS)?.modifiers()?.size !in arrayOf(0, null) && !item.hasData(DataComponentTypes.EQUIPPABLE)) {
                val modifiers = item.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS)
                if (!modifiers!!.modifiers().any { it.modifier().slotGroup != EquipmentSlotGroup.MAINHAND }) {
                    val newLines = mutableListOf<Component>()
                    //newLines.add(Utils.text(""))
                    newLines.add(text("When in Main Hand: ", Utils.GRAY))
                    for (modifier in modifiers!!.modifiers()) {
                        val attribute = modifier.attribute()
                        val amount = modifier.modifier().amount.round(3)
                        val trimmedAmount = if (attribute == Attribute.ATTACK_SPEED) (amount + 4).trimToString() else amount.trimToString()
                        val modification = modifier.modifier().operation
                        val sign = if (modifier.modifier().amount > 0 || attribute == Attribute.ATTACK_SPEED) "+" else ""
                        val attrString = "$sign$trimmedAmount${if (modification != AttributeModifier.Operation.ADD_NUMBER) "%" else ""} ${attribute.readName()}"
                        newLines.add(text(attrString, Utils.BLUE))
                    }
                    description.add(text(""))
                    description.addAll(newLines)
                    item.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().addHiddenComponents(DataComponentTypes.ATTRIBUTE_MODIFIERS).build())
                    item.lore(description)
                } else {
                    item.lore(description)
                }
            } else {
                item.lore(description)
            }
            items.add(item)
        }

        private fun ItemStack(material: Material, id: CustomItem, name: Component, description: MutableList<Component> = mutableListOf()): ItemStack {
            val newItem = ItemStack(material)
            newItem.setTag("id", id.id)
            newItem.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addString(id.id.toString()))
            newItem.name(name.style(Style.style(name.color(), TextDecoration.BOLD).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
            newItem.setData(DataComponentTypes.USE_COOLDOWN, UseCooldown.useCooldown(0.05F).cooldownGroup(Key.key("customitems", id.name.lowercase())))
            newItem.lore(description)
            return newItem
        }
        private fun Attribute.readName(): String {
            val value = this.key.value()
            val words = value.split("_").toMutableList()
            var totalAttr = ""
            words.forEach {
                totalAttr += it.capitalize() + " "
            }
            return totalAttr
        }
        private fun Double.trimToString(): String {
            val initial = this.toString()
            var endIndex = (initial.length - 1).coerceAtMost(6)
            while (initial[endIndex] == "0".first() || initial[endIndex] == ".".first()) {
                if (endIndex == 0) break
                endIndex--
                if (initial[endIndex] == ".".first()) break
            }
            return initial.substring(0, endIndex + 1)
        }

        // potion related stuff maybe,
        // MORE ARMOR IS BADLY NEEDED, healing stuff too possibly, maybe food as well
        // consider looking at mine treasure for consumable ideas MELEE WEAPONS (just different speed weapons)
        // extra drops from mobs/souls for charge
        // look at: mcdw/armor, simply swords, medieval weapons (for reach/dmg combos), artifacts,
        fun init() {
            addItem(ItemStack(Material.COBWEB, CustomItem.ALL, text("test")))
            addItem(ItemStack(Material.EMERALD, CustomItem.JERRY_IDOL, text("Jerry Idol", arrayOf(28, 148, 54)), Utils.loreBlockToList(
                text("Gives all players in a 50 block radius 1 level of hero of the village per 3 stacks of emerald blocks deposited. " +
                           "Right click with a stack of emerald blocks to deposit, right click to place, with an empty hand to pickup.", Utils.GRAY)
            )))
            addItem(ItemStack(Material.SEA_PICKLE, CustomItem.VILLAGER_ATOMIZER, text("Villager Atomizer", arrayOf(115, 86, 50)), Utils.loreBlockToList(
                text("Right click a villager to pick it up and turn it into item form.", Utils.GRAY)
            )))
            addItem(ItemStack(Material.CLAY_BALL, CustomItem.VILLAGER, text("Villager", arrayOf(125, 95, 60))))
            addItem(ItemStack(Material.RAW_GOLD, CustomItem.GOLDEN_ZOMBIE, text("Golden Zombie", arrayOf(222, 205, 24)), Utils.loreBlockToList(
                text("Right click a zombie villager to cure it or a villager to zombify it.", Utils.GRAY),
            )))
            addItem(ItemStack(Material.QUARTZ, CustomItem.FANGED_STAFF, text("Fanged Staff", arrayOf(112, 121, 128)), Utils.loreBlockToList(
                text("Left click to summon fangs, 0.5 second cooldown. Hold right click for 4 seconds to gain an aura that damages anything within it.", Utils.GRAY)
            )))
            addItem(ItemStack(Material.GOLD_INGOT, CustomItem.TOTEM_CORE, text("Totem Core", arrayOf(255, 225, 0)), Utils.loreBlockToList(
                text("Material, does not work as a totem.", Utils.GRAY),
            )))
            addItem(ItemStack(Material.STICK, CustomItem.FLETCHER_UPGRADE, text("Fletcher Upgrade", arrayOf(145, 116, 57)), Utils.loreBlockToList(
                text("Right click on a master level fletcher to gain a random custom arrow trade. The possible arrows are: dripstone, ender pearl, llama spit, wither skull, and shulker bullet. They will cost diamonds and emeralds to buy.", Utils.GRAY),
            )))
            addItem(ItemStack(Material.TIPPED_ARROW, CustomItem.DRIPSTONE_ARROW, text("Dripstone Arrow", arrayOf(194, 167, 95)), Utils.loreBlockToList(
                text("Shoots a high-damage dripstone projectile.", Utils.GRAY),
            )).potionColor(Color.fromRGB(CustomItem.DRIPSTONE_ARROW.id)))
            addItem(ItemStack(Material.TIPPED_ARROW, CustomItem.ENDER_PEARL_ARROW, text("Ender Pearl Arrow", arrayOf(38, 118, 133)), Utils.loreBlockToList(
                text("Shoots an ender pearl.", Utils.GRAY),
            )).potionColor(Color.fromRGB(CustomItem.ENDER_PEARL_ARROW.id)))
            addItem(ItemStack(Material.TIPPED_ARROW, CustomItem.WITHER_SKULL_ARROW, text("Wither Skull Arrow", arrayOf(51, 41, 69)), Utils.loreBlockToList(
                text("Shoots a wither skull.", Utils.GRAY),
            )).potionColor(Color.fromRGB(CustomItem.WITHER_SKULL_ARROW.id)))
            addItem(ItemStack(Material.TIPPED_ARROW, CustomItem.LLAMA_SPIT_ARROW, text("Llama Spit Arrow", arrayOf(217, 201, 176)), Utils.loreBlockToList(
                text("Shoots a llama spit that does 1 true damage.", Utils.GRAY),
            )).potionColor(Color.fromRGB(CustomItem.LLAMA_SPIT_ARROW.id)))
            addItem(ItemStack(Material.TIPPED_ARROW, CustomItem.SHULKER_BULLET_ARROW, text("Shulker Bullet Arrow", arrayOf(184, 140, 209)), Utils.loreBlockToList(
                text("Shoots a shulker bullet.", Utils.GRAY),
            )).potionColor(Color.fromRGB(CustomItem.SHULKER_BULLET_ARROW.id)))
            addItem(ItemStack(Material.BOW, CustomItem.WIND_HOOK, text("Wind Hook", arrayOf(211, 195, 219)), Utils.loreBlockToList(
                text("Shoot to launch a hook that pulls you in upon landing.", Utils.GRAY),
            )))
            addItem(ItemStack(Material.STONE_BUTTON, CustomItem.INPUT_DEVICES, text("Input Devices", arrayOf(146, 145, 158)), Utils.loreBlockToList(
                text("Redstone Placer:", arrayOf(199, 19, 6)),
                text("Consumes materials from one red shulker box in your inventory to automatically craft and place redstone items. This can also decompact certain items such as oak logs and iron blocks.", Utils.GRAY),
                text(""),
                text("While sneaking, scroll forward or back through your hotbar to cycle through: wooden pressure plate, stone pressure plate, golden pressure plate, iron pressure plate, stone button, wooden button, lever.", Utils.GRAY),
            )).setTag("redstonegroup", 0).setTag("redstoneitem", 0))
            addItem(ItemStack(Material.MINECART, CustomItem.MINECART_MATERIALS, text("Minecart Materials", arrayOf(92, 85, 81)), Utils.loreBlockToList(
                text("Redstone Placer:", arrayOf(199, 19, 6)),
                text("Consumes materials from one red shulker box in your inventory to automatically craft and place redstone items. This can also decompact certain items such as oak logs and iron blocks.", Utils.GRAY),
                text(""),
                text("While sneaking, scroll forward or back through your hotbar to cycle through: minecart, detector rail, rail, powered rail, activator rail, hopper minecart, chest minecart, furnace minecart, tnt minecart.", Utils.GRAY),
            )).setTag("redstonegroup", 1).setTag("redstoneitem", 0))
            addItem(ItemStack(Material.REDSTONE, CustomItem.ACTUAL_REDSTONE, text("Actual Redstone", arrayOf(150, 23, 0)), Utils.loreBlockToList(
                text("Redstone Placer:", arrayOf(199, 19, 6)),
                text("Consumes materials from one red shulker box in your inventory to automatically craft and place redstone items. This can also decompact certain items such as oak logs and iron blocks.", Utils.GRAY),
                text(""),
                text("While sneaking, scroll forward or back through your hotbar to cycle through: redstone, redstone block, repeater, comparator, redstone torch, observer.", Utils.GRAY),
            )).setTag("redstonegroup", 2).setTag("redstoneitem", 0))
            addItem(ItemStack(Material.BARREL, CustomItem.CONTAINERS, text("Containers", arrayOf(135, 97, 77)), Utils.loreBlockToList(
                text("Redstone Placer:", arrayOf(199, 19, 6)),
                text("Consumes materials from one red shulker box in your inventory to automatically craft and place redstone items. This can also decompact certain items such as oak logs and iron blocks.", Utils.GRAY),
                text(""),
                text("While sneaking, scroll forward or back through your hotbar to cycle through: barrel, hopper, chest, crafter, dispenser, dropper, noteblock, piston, sticky piston, slime block.", Utils.GRAY),
            )).setTag("redstonegroup", 3).setTag("redstoneitem", 0))
            addItem(ItemStack(Material.REDSTONE_BLOCK, CustomItem.REDSTONE_AMALGAMATION, text("Redstone Amalgamation", arrayOf(163, 5, 5)), Utils.loreBlockToList(
                text("Redstone Placer:", arrayOf(199, 19, 6)),
                text("Consumes materials from one red shulker box in your inventory to automatically craft and place redstone items. This can also decompact certain items such as oak logs and iron blocks.", Utils.GRAY),
                text(""),
                text("While sneaking, swap hands to cycle between all of the other redstone placers.", Utils.GRAY),
                text(""),
                text("While sneaking, scroll forward or back through your hotbar to cycle through each of the items in each redstone placer.", Utils.GRAY),
            )).setTag("redstonegroup", 0).setTag("redstoneitem", 0).setTag("storedinner", arrayOf(0, 0, 0, 0).toIntArray()))
            addItem(ItemStack(Material.CROSSBOW, CustomItem.REDSTONE_REPEATER, text("Single Redstone Repeater - 0", arrayOf(125, 30, 30))).setTag("loading", false).setTag("arrowcount", 1).setTag("loadedarrows", 0))
            addItem(ItemStack(Material.IRON_NUGGET, CustomItem.POLARIZED_MAGNET, text("Polarized Magnet", arrayOf(255, 36, 36)), Utils.loreBlockToList(
                text("Hold right click to pull in all nearby entities. Left click while sneaking to toggle an item pull mode that pulls in all nearby items even when you are not holding it.", Utils.GRAY),
            )))
            addItem(ItemStack(Material.COPPER_INGOT, CustomItem.LAST_PRISM, text("Last Prism", arrayOf(243, 219, 255))))
            addItem(ItemStack(Material.CROSSBOW, CustomItem.MULTI_LOAD_CROSSBOW, text("Multi-load Shotgun", arrayOf(214, 125, 0))).setTag("loadedshot", 0).setTag("loading", true))
            addItem(ItemStack(Material.NETHERITE_PICKAXE, CustomItem.VEINY_PICKAXE, text("Veiny Pickaxe", arrayOf(150, 125, 0)), Utils.loreBlockToList(
                text("Mines up to 32 blocks of the same type connected to the block you break, with a 3 second cooldown.", Utils.GRAY),
            )))
            addItem(ItemStack(Material.GOLDEN_AXE, CustomItem.TREECAPITATOR, text("Treecapitator", arrayOf(117, 32, 8)), Utils.loreBlockToList(
                text("Mines up to 200 logs or leaves of the same type connected to the block you break.", Utils.GRAY),
            )).unb())
            addItem(ItemStack(Material.NETHERITE_PICKAXE, CustomItem.EXCAVATOR, text("Excavator", arrayOf(79, 66, 67)), Utils.loreBlockToList(
                text("Mines a 3x3x3 area around the block that you break.", Utils.GRAY),
            )))
            addItem(ItemStack(Material.POPPED_CHORUS_FRUIT, CustomItem.PEW_MATIC_HORN, text("Pew-matic Horn", arrayOf(179, 57, 75))))
            addItem(ItemStack(Material.TURTLE_SCUTE, CustomItem.TRADING_SCRAMBLER, text("Trading Scrambler", arrayOf(132, 207, 168)), Utils.loreBlockToList(
                text("Right click on a villager to reroll its trades. All trades from all levels will be rerolled.", Utils.GRAY),
            )))
            addItem(ItemStack(Material.TURTLE_SCUTE, CustomItem.EXPERIENCE_FLASK, text("Experience Flask", arrayOf(170, 242, 24)), Utils.loreBlockToList(
                text("Stored experience: 0", arrayOf(73, 209, 10)),
                text(""),
                text("Left click to retrieve all experience, left click while sneaking to deposit all experience. Right click to retrieve 30 levels, or sneak right click to retrieve 30 levels which will mend gear.", Utils.GRAY)
            )).setTag("storedexp", 0))
            addItem(ItemStack(Material.NETHERITE_PICKAXE, CustomItem.NETHERITE_MULTITOOL, text("Netherite Multitool", arrayOf(89, 14, 7)), Utils.loreBlockToList(
                text("Right click while sneaking to cycle through a netherite pickaxe, axe, shovel, and hoe.", Utils.GRAY),
            )).setTag("tool", 0))
            addItem(ItemStack(Material.NETHERITE_SHOVEL, CustomItem.HOEVEL, text("Hoevel", arrayOf(99, 45, 40)), Utils.loreBlockToList(
                text("Works as a shovel and hoe when breaking blocks.", Utils.GRAY),
            )).tagTool(Tag.MINEABLE_HOE, 9F).tagTool(Tag.MINEABLE_SHOVEL, 9F).duraBroken(1))
            addItem(ItemStack(Material.NETHERITE_PICKAXE, CustomItem.AXEPICK, text("Axepick", arrayOf(96, 99, 40)), Utils.loreBlockToList(
                text("Works as an axe and pickaxe when breaking blocks.", Utils.GRAY),
            )).tagTool(Tag.MINEABLE_AXE, 9F).tagTool(Tag.MINEABLE_PICKAXE, 9F).duraBroken(1))
            addItem(ItemStack(Material.NETHERITE_PICKAXE, CustomItem.NETHERITE_MATTOCK, text("Netherite Mattock", arrayOf(54, 35, 64)), Utils.loreBlockToList(
                text("Works as a axe, pickaxe, shovel, and hoe when breaking blocks.", Utils.GRAY),
            ))
                .tagTool(Tag.MINEABLE_HOE, 9F).tagTool(Tag.MINEABLE_AXE, 9F).tagTool(Tag.MINEABLE_SHOVEL, 9F).tagTool(Tag.MINEABLE_PICKAXE, 9F).duraBroken(1))
            addItem(ItemStack(Material.SHEARS, CustomItem.POCKETKNIFE_MULTITOOL, text("Pocketknife-multitool", arrayOf(166, 166, 166)), Utils.loreBlockToList(
                text("Right click while sneaking to cycle through shears, flint and steel, and a brush.", Utils.GRAY),
            )).setTag("tool", 0).unb())
            addItem(ItemStack(Material.NETHERITE_HOE, CustomItem.HOE, text("Hoe", arrayOf(37, 110, 41)), Utils.loreBlockToList(
                text("Turns all dirt-like tiles in a 3x3x3 area around the block you till into farmland.", Utils.GRAY),
            )))
            addItem(ItemStack(Material.NETHERITE_SWORD, CustomItem.HOOKED_CUTLASS, text("Hooked Cutlass", arrayOf(61, 77, 87))))
            addItem(ItemStack(Material.NETHERITE_AXE, CustomItem.AXE_OF_PEACE, text("Axe of Peace", arrayOf(117, 2, 4)), Utils.loreBlockToList(
                text("Heals you for 0.75 health on a fully charged hit.", Utils.GRAY)
            )).attr("ATS-3.2MA", "ATD+15MA", "ENI-0.4MA"))
            addItem(ItemStack(Material.NETHERITE_SWORD, CustomItem.ENDER_BLADE, text("Ender Blade", arrayOf(11, 79, 82)), Utils.loreBlockToList(
                text("Right click to teleport forward 12 blocks, with a 7 second cooldown. Right click while sneaking to teleport forward 12 blocks, and make all hits be critical for the next 5 seconds, with a 15 second cooldown.", Utils.GRAY)
            )).attr("ATS-2.2MA", "ENI+0.1MA", "ATD+8.5MA"))
            addItem(ItemStack(Material.NETHERITE_AXE, CustomItem.HEAVY_GREATHAMMER, text("Heavy Greathammer", arrayOf(87, 75, 62))).attr("ATS-3.5MA", "ATD+16.5MA").setTag("criticalcount", 0))
            addItem(ItemStack(Material.NETHERITE_SWORD, CustomItem.CRESTED_DAGGER, text("Crested Dagger", arrayOf(155, 165, 168))).attr("ATS-2.0MA", "ENI-1.4MA"))
            addItem(ItemStack(Material.MAGMA_CREAM, CustomItem.FIERY_SHARD, text("Fiery Shard", arrayOf(255, 117, 54))))
            addItem(ItemStack(Material.APPLE, CustomItem.MYSTICAL_GREEN_APPLE, text("Mystical Green Apple", arrayOf(35, 212, 0)), Utils.loreBlockToList(
                text("Increases the amount of your total experience that you keep on death by 25%. You can consume a maximum of 4.", Utils.GRAY),
            )).food(20, 20F, true))
            addItem(ItemStack(Material.ENDER_CHEST, CustomItem.ENDER_NODE, text("Ender Node", arrayOf(5, 105, 81)), Utils.loreBlockToList(
                text("Right click in your inventory to open up your ender chest.", Utils.GRAY)
            )))
            addItem(ItemStack(Material.NETHER_STAR, CustomItem.SOUL_CRYSTAL, text("Soul Crystal", arrayOf(189, 154, 219))))
            addItem(ItemStack(Material.NETHERITE_SCRAP, CustomItem.NETHERITE_COATING, text("Netherite Coating", arrayOf(74, 63, 56))))
            addItem(ItemStack(Material.IRON_INGOT, CustomItem.WITHER_COATING, text("Wither Coating", arrayOf(191, 242, 224))))
            addItem(ItemStack(Material.CHAIN, CustomItem.REINFORCING_STRUTS, text("Reinforcing Struts", arrayOf(154, 161, 158))))
            addItem(ItemStack(Material.CHORUS_FRUIT, CustomItem.SHULKER_FRUIT, text("Shulker Fruit", arrayOf(157, 3, 252)), Utils.loreBlockToList(
                text("Consume to permanently be able to open shulker boxes in your inventory with right click.", Utils.GRAY)
            )))
            addItem(ItemStack(Material.POPPED_CHORUS_FRUIT, CustomItem.WARDEN_SPAWNER, text("Warden Spawner", arrayOf(1, 69, 92)), Utils.loreBlockToList(
                text("Right click to consume this item and begin the custom warden boss. It will teleport players within 10 blocks of you as well. You cannot use this while being tracked, while in combat, or if someone else is fighting the boss already.", Utils.GRAY)
            )))
            addItem(ItemStack(Material.POPPED_CHORUS_FRUIT, CustomItem.WITHER_SPAWNER, text("Wither Spawner", arrayOf(74, 67, 67)), Utils.loreBlockToList(
                text("Right click to consume this item and begin the custom wither boss. It will teleport players within 10 blocks of you as well. You cannot use this while being tracked, while in combat, or if someone else is fighting the boss already.", Utils.GRAY)
            )))
            addItem(ItemStack(Material.POPPED_CHORUS_FRUIT, CustomItem.BASTION_SPAWNER, text("Bastion Spawner", arrayOf(237, 223, 21)), Utils.loreBlockToList(
                text("Right click to consume this item and begin the custom piglin boss. It will teleport players within 10 blocks of you as well. You cannot use this while being tracked, while in combat, or if someone else is fighting the boss already.", Utils.GRAY)
            )))
            addItem(ItemStack(Material.POPPED_CHORUS_FRUIT, CustomItem.MONUMENT_SPAWNER, text("Monument Spawner", arrayOf(117, 228, 230)), Utils.loreBlockToList(
                text("Right click to consume this item and begin the custom guardian boss. It will teleport players within 10 blocks of you as well. You cannot use this while being tracked, while in combat, or if someone else is fighting the boss already.", Utils.GRAY)
            )))
            addItem(ItemStack(Material.POPPED_CHORUS_FRUIT, CustomItem.DESERT_SPAWNER, text("Desert Spawner", arrayOf(230, 223, 106)), Utils.loreBlockToList(
                text("Right click to consume this item and begin the custom desert boss. It will teleport players within 10 blocks of you as well. You cannot use this while being tracked, while in combat, or if someone else is fighting the boss already.", Utils.GRAY)
            )))
            addItem(ItemStack(Material.CROSSBOW, CustomItem.SURFACE_TO_AIR_MISSILE, text("Surface to Air Missile Launcher", arrayOf(227, 134, 11)), Utils.loreBlockToList(
                text("Shoots a nearly instant homing projectile that homes into players who are flying with elytra. Upon hitting them, it disables their elytra for 25 seconds. This item has a 20 second cooldown.",
                    Utils.GRAY),
            )))
            addItem(ItemStack(Material.NETHERITE_SWORD, CustomItem.TRIPLE_SWIPE_SWORD, text("Triple Swipe Sword",
                arrayOf(230, 69, 77))))
            addItem(ItemStack(Material.CROSSBOW, CustomItem.WIND_CHARGE_CANNON, text("Wind Charge Cannon - Homing", arrayOf(201, 240, 238)), Utils.loreBlockToList(
                text("Shoot to launch a cluster of two wind charges, with a 7 second cooldown. Left click to cycle between homing mode and straight mode.",
                    Utils.GRAY),
            )).setTag("mode", 0))
            addItem(ItemStack(Material.CROSSBOW, CustomItem.SNIPER_RIFLE, text("Sniper Rifle", arrayOf(52, 69, 54))))
            addItem(ItemStack(Material.NETHERITE_AXE, CustomItem.GRAVITY_HAMMER, text("Gravity Hammer", arrayOf(55, 46, 66)), Utils.loreBlockToList(
                text("On a fully charged hit, increase your opponent's gravity significantly for 7 seconds, with a 20 second cooldown.", Utils.GRAY)
            )).attr("ATS-3.3MA", "ATD+15.0MA"))
            addItem(ItemStack(Material.CROSSBOW, CustomItem.RIDABLE_CROSSBOW, text("Ridable Crossbow", arrayOf(173, 94, 49)), Utils.loreBlockToList(
                text("Shoot to launch an arrow that you will ride on.", Utils.GRAY),
            )))
            addItem(ItemStack(Material.BOW, CustomItem.LANDMINE_LAUNCHER, text("Landmine Launcher", arrayOf(107, 80, 77))))
            addItem(ItemStack(Material.NETHERITE_BOOTS, CustomItem.MOON_BOOTS, text("Moon Boots", arrayOf(191, 218, 245))).attr("GRA-0.84%FE", "JUS0.05FE", "SAF20FE","ART+3.0FE","ARM+3.0FE","KNR+0.1FE"))
            addItem(ItemStack(Material.NETHERITE_HELMET, CustomItem.COWBOY_HAT, text("Cowboy Hat", arrayOf(219, 124, 77)), Utils.loreBlockToList(
                text("While wearing this, any horse you are riding will recieve Swiftness 3, Leaping 5, and will become invulnerable to damage.", Utils.GRAY),
            )))
            addItem(ItemStack(Material.NETHERITE_BOOTS, CustomItem.DOUBLE_JUMP_BOOTS, text("Double Jump Boots", arrayOf(171, 230, 245))).attr("ARM+3.0FE","ART+3.0FE","KNR+0.1FE","SAF+8.0FE"))
            addItem(ItemStack(Material.NETHERITE_CHESTPLATE, CustomItem.TURTLE_SHELL, text("Turtle Shell", arrayOf(45, 84, 50))).attr("ARM+12.0CH","ART+6.0CH","KNR+0.3CH","MOS-0.05CH","MAH+4.0CH"))
            addItem(ItemStack(Material.NETHERITE_HELMET, CustomItem.DRINKING_HAT, text("Drinking Cap", arrayOf(235, 170, 127)), Utils.loreBlockToList(
                text("While wearing this, all potions you use will give you double the duration.", Utils.GRAY),
            )).attr("ARM+4.0HE","ART+4.0HE", "KNR+0.1HE","MAH+2.0HE"))
            addItem(ItemStack(Material.NETHERITE_LEGGINGS, CustomItem.HERMESS_TROUSERS, text("Hermes's Trousers", arrayOf(145, 192, 219))).attr("ARM+7.0LE","ART+3.0LE","MOS+0.04LE","WAM+0.3LE","STH+1.0LE"))
            addItem(ItemStack(Material.NETHERITE_LEGGINGS, CustomItem.SHADOW_LEGS, text("Shadow Legs", arrayOf(44, 4, 108)), Utils.loreBlockToList(
                text("When your totem is popped, gain Speed 3, Strength 3, Resistance 2, and Regeneration 3 for 25 seconds.", Utils.GRAY),
            )).attr("ARM+7.0LE","ART+4.0LE","MOS+0.01LE","ATD+2.0LE","ATS+0.1LE","MAH+2.0LE"))
            addItem(ItemStack(Material.NETHERITE_CHESTPLATE, CustomItem.BERSERKER_CHESTPLATE, text("Berserker Chestplate", arrayOf(245, 136, 2))).attr("ARM+8.0CH","ART+4.0CH","KNR+0.1CH","MOS+0.01CH",
                "MAH+4.0CH","ENI+0.5CH","ATD+4.0CH"))
            addItem(ItemStack(Material.NETHERITE_HELMET, CustomItem.XRAY_GOGGLES, text("X-ray Goggles", arrayOf(44, 145, 22)), Utils.loreBlockToList(
                text("Sneak to give all entities within a 20 block radius glowing for 20 seconds, with a 20 second cooldown.", Utils.GRAY),
            )).attr("ARM+4.0HE","ART+4.0HE","KNR+0.1HE","MAH+2.0HE"))
            addItem(ItemStack(Material.NETHERITE_LEGGINGS, CustomItem.REPELLANT_PANTS, text("Repellant Pants", arrayOf(27, 2, 64))).attr("ARM+8.0LE","ART+4.0LE","ATD+1.0LE","MAH+2.0LE"))
            addItem(ItemStack(Material.NETHERITE_CHESTPLATE, CustomItem.MACE_SHIELDED_PLATING, text("Mace Shielded Plating", arrayOf(89, 84, 92))).attr("ARM+10.0CH","ART+4.0CH","KNR+0.4CH"))
            addItem(ItemStack(Material.NETHERITE_CHESTPLATE, CustomItem.MOLTEN_CHESTPLATE, text("Molten Chestplate", arrayOf(209, 103, 4))).attr("ARM+8.0CH","ART+4.0CH","KNR+0.1CH","BUT-1.0CH","MAH+4.0CH"))
            addItem(ItemStack(Material.NETHERITE_LEGGINGS, CustomItem.TOOLBELT, text("Toolbelt", arrayOf(125, 63, 5))).attr("ARM+6.0LE","ART+3.0LE","KNR+0.1LE","BLI+3.0LE"))
            addItem(ItemStack(Material.NETHERITE_BOOTS, CustomItem.CLOUD_BOOTS, text("Cloud Boots", arrayOf(193, 216, 227))).attr("ARM+5.0FE","ART+4.0FE","KNR+0.1FE","FAD-1.0FE","MAH+4.0FE","MOE+1.0FE","STH+0.5FE"))
            addItem(ItemStack(Material.NETHERITE_HELMET, CustomItem.INVISIBILITY_CLOAK, text("Invisibility Cloak", arrayOf(227, 231, 232))).attr("ARM+3.0HE","ART+3.0HE","KNR+0.1HE","MAH+4.0HE","ATD+4.0HE","ATS+0.2HE"))
            addItem(ItemStack(Material.NETHERITE_BOOTS, CustomItem.AQUEOUS_SANDALS, text("Aqueous Sandals", arrayOf(91, 130, 189)), Utils.loreBlockToList(
                text("Gain permanent water breathing and conduit power. Sneak to gain dolphin's grace for 5 seconds, with a 20 second cooldown.", Utils.GRAY)
            )).attr("ARM+5.0FE","ART+4.0FE","WAM+1.0FE","SUM+1.0FE","MOS+0.01FE"))
            addItem(ItemStack(Material.NETHERITE_LEGGINGS, CustomItem.ENCRUSTED_PANTS, text("Encrusted Pants", arrayOf(130, 61, 14))).attr("ARM+10.0LE","ART+5.0LE","KNR+0.3LE"))
            addItem(ItemStack(Material.NETHERITE_HELMET, CustomItem.WELDING_HELMET, text("Welding Helmet", arrayOf(94, 92, 71)), Utils.loreBlockToList(
                text("Gain permanent immunity to blindness, nausea, and darkness.", Utils.GRAY),
                text(""),
                text("Full Set Bonus (4 pieces): Immunity Set", Utils.GRAY),
                text("Upon receiving any negative effect, convert it into the corresponding positive effect with potency increased by 1 level and triple the duration.", Utils.GRAY)
            )).attr("ARM+5.0HE","ART+4.0HE","MAH+4.0HE","ATD+1.0HE"))
            addItem(ItemStack(Material.NETHERITE_CHESTPLATE, CustomItem.ANTI_VENOM_SHIRT, text("Anti-venom Shirt", arrayOf(43, 92, 48))).attr("ARM+10.0CH","ART+4.0CH","MAH+6.0CH","ATD+1.0CH"))
            addItem(ItemStack(Material.NETHERITE_LEGGINGS, CustomItem.ENERGY_RESTORING_PANTS, text("Energy-restoring Pants", arrayOf(224, 195, 2))).attr("ARM+8.0LE","ART+4.0LE","MAH+6.0LE","ATD+1.0LE"))
            addItem(ItemStack(Material.NETHERITE_BOOTS, CustomItem.STABILZING_SNEAKERS, text("Stabilizing Sneakers", arrayOf(102, 82, 64)), Utils.loreBlockToList(
                text("Gain permanent immunity to slow falling and levitation.", Utils.GRAY),
                text(""),
                text("Full Set Bonus (4 pieces): Immunity Set", Utils.GRAY),
                text("Upon receiving any negative effect, convert it into the corresponding positive effect with potency increased by 1 level and triple the duration.", Utils.GRAY)
            )).attr("ARM+5.0FE","ART+4.0FE","MAH+4.0FE","ATD+1.0FE"))
            addItem(ItemStack(Material.IRON_INGOT, CustomItem.JETPACK_CONTROLLER_SET, text("Jetpack + Controller Set", arrayOf(99, 75, 75)), Utils.loreBlockToList(
                text("Right click to recieve a jetpack and jetpack controller.", Utils.GRAY),
            )))
            addItem(ItemStack(Material.NETHERITE_CHESTPLATE, CustomItem.JETPACK, text("Jetpack", arrayOf(255, 158, 3)), Utils.loreBlockToList(
                text("Equip and use the jetpack controller to control your movement.", Utils.GRAY),
            )).noNoiseEquippable(EquipmentSlot.CHEST))
            addItem(ItemStack(Material.IRON_NUGGET, CustomItem.JETPACK_CONTROLLER, text("Jetpack Controller - OFF", arrayOf(148, 134, 111)), Utils.loreBlockToList(
                text("Left click to toggle the jetpack on and off. Hold right click to ascend, and sneak to descend.", Utils.GRAY),
            )).consumable(eatSeconds = 10000.0F))
            addItem(ItemStack(Material.NETHERITE_HELMET, CustomItem.HARD_HAT, text("Hard Hat", arrayOf(245, 218, 66))).attr("ARM+7.0HE","ART+4.0HE","KNR+0.2HE","MAH+2.0HE"))
            addItem(ItemStack(Material.NETHERITE_BOOTS, CustomItem.STEEL_TOED_BOOTS, text("Steel-toed Boots", arrayOf(99, 97, 85))).attr("ARM+7.0FE","ART+5.0FE","KNR+0.2FE","MAH+2.0FE"))
            addItem(ItemStack(Material.NETHERITE_HELMET, CustomItem.ASSASSINS_HOOD, text("Assassin's Hood", arrayOf(32, 2, 112)), Utils.loreBlockToList(
                text("Gain an additive 1/8 chance to dodge arrows.", Utils.GRAY),
                text(""),
                text("Full Set Bonus (4 pieces): Assassin's Set", Utils.GRAY),
                text("Gain permanent invisibility. Gain +4% movement speed, 0.6 attack damage, 0.02 attack speed, and -3% scale every second for up to 10 seconds. Resets upon taking damage.", Utils.GRAY),
            )).attr("ARM+2.0HE","ART+2.0HE","ATD+2.0HE","MOS+0.005HE","ATS+0.05HE"))
            addItem(ItemStack(Material.NETHERITE_CHESTPLATE, CustomItem.ASSASSINS_ROBE, text("Assassin's Robe", arrayOf(32, 2, 112)), Utils.loreBlockToList(
                text("Gain an additive 1/8 chance to dodge arrows.", Utils.GRAY),
                text(""),
                text("Full Set Bonus (4 pieces): Assassin's Set", Utils.GRAY),
                text("Gain permanent invisibility. Gain +4% movement speed, 0.6 attack damage, 0.02 attack speed, and -3% scale every second for up to 10 seconds. Resets upon taking damage.", Utils.GRAY),
            )).attr("ARM+6.0CH","ART+2.0CH","ATD+2.0CH","MOS+0.005CH","ATS+0.05CH"))
            addItem(ItemStack(Material.NETHERITE_LEGGINGS, CustomItem.ASSASSINS_LEGGINGS, text("Assassin's Leggings", arrayOf(32, 2, 112)), Utils.loreBlockToList(
                text("Gain an additive 1/8 chance to dodge arrows.", Utils.GRAY),
                text(""),
                text("Full Set Bonus (4 pieces): Assassin's Set", Utils.GRAY),
                text("Gain permanent invisibility. Gain +4% movement speed, 0.6 attack damage, 0.02 attack speed, and -3% scale every second for up to 10 seconds. Resets upon taking damage.", Utils.GRAY),
            )).attr("ARM+4.0LE","ART+2.0LE","ATD+2.0LE","MOS+0.005LE","ATS+0.05LE"))
            addItem(ItemStack(Material.NETHERITE_BOOTS, CustomItem.ASSASSINS_LOAFERS, text("Assassin's Loafers", arrayOf(32, 2, 112)), Utils.loreBlockToList(
                text("Gain an additive 1/8 chance to dodge arrows.", Utils.GRAY),
                text(""),
                text("Full Set Bonus (4 pieces): Assassin's Set", Utils.GRAY),
                text("Gain permanent invisibility. Gain +4% movement speed, 0.6 attack damage, 0.02 attack speed, and -3% scale every second for up to 10 seconds. Resets upon taking damage.", Utils.GRAY),
            )).attr("ARM+2.0FE","ART+2.0FE","ATD+2.0FE","MOS+0.005FE","ATS+0.05FE"))
            addItem(ItemStack(Material.NETHERITE_HELMET, CustomItem.WARRIOR_HELM, text("Warrior Helm", arrayOf(204, 116, 2))).attr("ARM+4.0HE","ART+4.0HE","KNR+0.15HE","MAH+1.5HE","ATD+1.0HE"))
            addItem(ItemStack(Material.NETHERITE_CHESTPLATE, CustomItem.WARRIOR_CHESTPLATE, text("Warrior Chestplate", arrayOf(204, 116, 2))).attr("ARM+9.0CH","ART+4.0CH","KNR+0.15CH","MAH+1.5CH","ATD+2.0CH"))
            addItem(ItemStack(Material.NETHERITE_LEGGINGS, CustomItem.WARRIOR_GREAVES, text("Warrior Greaves", arrayOf(204, 116, 2))).attr("ARM+7.0LE","ART+4.0LE","KNR+0.15LE","MAH+1.5LE","ATD+2.0LE"))
            addItem(ItemStack(Material.NETHERITE_BOOTS, CustomItem.WARRIOR_BOOTS, text("Warrior Boots", arrayOf(204, 116, 2))).attr("ARM+4.0FE","ART+4.0FE",
                "KNR+0.15FE","MAH+1.5FE","ATD+1.0FE"))
            addItem(ItemStack(Material.NETHERITE_HELMET, CustomItem.MINERS_HELM, text("Miner's Helm", arrayOf(122, 119, 69))).attr("ARM+4.0HE","ART+4.0HE","KNR+0.1HE","MIE+10.0HE"))
            addItem(ItemStack(Material.COMPASS, CustomItem.TRACKING_COMPASS, text("Tracking Compass", arrayOf(7, 121, 186)), Utils.loreBlockToList(
                text("Allows you to track other players, right click to open menu. Tracking a player will cost additional resources.", Utils.GRAY),
            )))
            addItem(ItemStack(Material.CHAIN, CustomItem.REINFORCED_CAGE, text("Reinforced Cage", arrayOf(105, 101, 100)), Utils.loreBlockToList(
                text("Type: NONE STORED", Utils.GRAY),
                text(""),
                text("Right click a non-boss, non-custom mob to pick it up and store it in this item. Right click again on the ground to place it down. You can only store one mob in this item at a time.", Utils.GRAY),
            )))
            addItem(ItemStack(Material.IRON_SWORD, CustomItem.DARK_STEEL_RAPIER, text("Dark Steel Rapier", arrayOf(74, 98, 125)), Utils.loreBlockToList(
                text("Cannot perform critical hits. Right click to gain 60% increased speed for 15 seconds and blind players within 10 blocks of you for 8 seconds, 40 second cooldown.", Utils.GRAY)
            )).attr("ATS-2.0MA", "ATD+9.0MA", "MOS+0.01MA"))
            addItem(ItemStack(Material.IRON_SWORD, CustomItem.FROZEN_SHARD, text("Frozen Shard", arrayOf(102, 193, 209)), Utils.loreBlockToList(
                text("On hit, prevent a player from walking, jumping, or taking knockback for 6 seconds, with a 60s cooldown. They can still pearl or use other movement items. This sword also has a 1/5 chance of inflicting Slowness 1 on hit for 5 seconds.", Utils.GRAY)
            )).attr("ATS-2.4MA", "ATD+9.0MA"))
            addItem(ItemStack(Material.NETHERITE_SWORD, CustomItem.BARBED_BLADE, text("Barbed Blade", arrayOf(107, 107, 140)), Utils.loreBlockToList(
                text("On hit, inflict a player with 4 decreased armor points for 4 seconds, with a 15 second cooldown. Does not stack with other players. Additionally, inflict Darkness on players for 5 seconds with a 1/5 chance.", Utils.GRAY)
            )).attr("ATS-2.4MA", "ATD+10.0MA"))
            addItem(ItemStack(Material.CROSSBOW, CustomItem.SONIC_CROSSBOW, text("Sonic Crossbow", arrayOf(2, 98, 117)), Utils.loreBlockToList(
                text("Shoots a piercing sonic boom that does true damage to players and high damage to mobs, with a 20 second cooldown.", Utils.GRAY),
            )))
            addItem(ItemStack(Material.CROSSBOW, CustomItem.DUAL_BARRELED_CROSSBOW, text("Dual-barreled Crossbow", arrayOf(117, 42, 2)), Utils.loreBlockToList(
                text("Shoots two arrows that can pierce through 5 mobs each. Does the same damage as a Power 5 bow.", Utils.GRAY),
            )))
            addItem(ItemStack(Material.ELYTRA, CustomItem.MECHANIZED_ELYTRA, text("Mechanized Elytra", arrayOf(103, 94, 110)), Utils.loreBlockToList(
                text("Sneak while wearing to activate a rocket boost with a 10 second cooldown.", Utils.GRAY),
            )).attr("ARM+6.0CH","JUS+0.21CH","SAF+1.0CH"))
            addItem(ItemStack(Material.CYAN_DYE, CustomItem.WARDEN_HEART, text("Warden Heart", arrayOf(1, 122, 133)), Utils.loreBlockToList(
                text("Rare drop from The Warden boss.", Utils.GRAY),
            )))
            addItem(ItemStack(Material.DISC_FRAGMENT_5, CustomItem.FRAGMENT_OF_SOUND, text("Fragment of Sound", arrayOf(1, 122, 133)), Utils.loreBlockToList(
                text("Drop from The Warden boss.", Utils.GRAY),
            )))
            addItem(ItemStack(Material.ECHO_SHARD, CustomItem.WARDEN_CARAPACE, text("Warden Carapace", arrayOf(1, 122, 133)), Utils.loreBlockToList(
                text("Crafted from The Warden drops.", Utils.GRAY),
            )))
            addItem(ItemStack(Material.MUSIC_DISC_5, CustomItem.SHADOW_DISC_CORE, text("Shadow Disc Core", arrayOf(1, 122, 133)), Utils.loreBlockToList(
                text("Crafted from music discs found in the ancient city.", Utils.GRAY),
            )))

            /*
            addItem(ItemStack(Material.DIAMOND, CustomItem.TEST2, Utils.text("test2")))
            addItem(ItemStack(Material.IRON_SWORD, CustomItem.REINFORCED_IRON_SWORD, Utils.text("Reinforced Iron Sword")).maxDura(1000))
            addItem(ItemStack(Material.DIAMOND_BLOCK, CustomItem.EATABLE_BLOCK, Utils.text("eat")).maxStack(99).ench("FA5").food(1, 1F, canAlwaysEat = true).attr("GRA200%"))
            addItem(ItemStack(Material.OAK_LOG, CustomItem.WOODCUTTER, Utils.text("Woodcutter")).tool(5F, Pair(Tag.LOGS, 50F)))
            addItem(ItemStack(Material.DIAMOND_CHESTPLATE, CustomItem.NETHERITE_CHESTPLATE, Utils.text("Netherite Chestplate"))
                .neth().lore(Utils.text("hello", arrayOf(0 ,255, 255)))
                .trim(ArmorTrim(TrimMaterial.DIAMOND, TrimPattern.TIDE)
            ))
            addItem(ItemStack(Material.ENCHANTED_BOOK, CustomItem.VERY_ENCHANT, Utils.text("Very Enchant")).storeEnch("FF10"))
            addItem(ItemStack(Material.SPLASH_POTION, CustomItem.POT, Utils.text("Pot")).potion(Color.fromRGB(0, 221, 123), "SLF1:40", "GLO3:300"))
            addItem(ItemStack(Material.FIREWORK_ROCKET, CustomItem.FIRE, Utils.text("Fire")).firework(10, FireworkEffect.builder().withColor(Color.RED).build()))
            addItem(ItemStack(Material.OMINOUS_BOTTLE, CustomItem.OMMY, Utils.text("Ommy")).omimous(4))*/
        }

        fun get(id: Int): ItemStack {
            return ItemStack(items[id])
        }
        fun get(item: CustomItem): ItemStack {
            return ItemStack(items[item.id])
        }
    }
}