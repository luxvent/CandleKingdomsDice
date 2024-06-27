package candle.dice.commands

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import net.silkmc.silk.commands.ArgumentCommandBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.silkmc.silk.commands.command
import net.silkmc.silk.core.text.broadcastText
import kotlin.random.Random


val diceCommand = command("dice") {
    runs {
        source.sendError(Text.translatable("text.candle-kingdoms-dice.dice_throw.no_dice"))
    }
    argument("dice", StringArgumentType.string()) { diceArg ->
        suggestList { listOf("D2", "D4", "D6", "D8", "D10", "D12", "D20") }

        runs {
            val diceString = StringArgumentType.getString(this, "dice")
            throwDice(this, diceString, 1)
        }

        argument<Int>("throw count") { diceCount ->
            suggestList { listOf("1") }

            runs {
                val diceString = StringArgumentType.getString(this, "dice")
                val countArg : Int = IntegerArgumentType.getInteger(this, "throw count")

                throwDice(this, diceString, countArg)
//            suggestList { listOf("-", "<description>") }
//        argument("description", StringArgumentType.greedyString()) { description ->
//            runs {
//                val diceString = StringArgumentType.getString(this, "dice")
//                val descriptionString = StringArgumentType.getString(this, "description")
//                if (descriptionString != "-") {
//                    val descriptionText = Text.literal(". ($descriptionString)")
//                    throwDice(this, diceString, 1, descriptionText)
//                } else {
//                    throwDice(this, diceString, 1)
//                }
//            }
            }

            argument("description", StringArgumentType.greedyString()) { description ->

                    runs {
                        val descriptionString = StringArgumentType.getString(this, "description")
//                        val diceString = StringArgumentType.getString(this, "dice")
//                        val descriptionString = StringArgumentType.getString(this, "description")
                        val diceString = StringArgumentType.getString(this, "dice")
                        val countArg : Int = IntegerArgumentType.getInteger(this, "throw count")
                        val descriptionText = Text.literal(" ($descriptionString)")
                        throwDice(this, diceString, countArg, descriptionText)
                    }
            }
        }
    }
}

private fun throwDice (context: CommandContext<ServerCommandSource>, stringArg: String, throwCount: Int = 1, description: Text = Text.literal("")): Int {
    val resultList = mutableListOf<Int>()
    var max = 0
    val diceLogo = Text.literal("{\uD83C\uDFB2}").setStyle(Style.EMPTY.withColor(Formatting.WHITE))
    for (i in 1..throwCount) {
        var result = 0
        when (stringArg) {
            "D2" -> { result = Random.nextInt(1, 2 + 1)
                max = 2 }
            "D4" -> { result = Random.nextInt(1, 4 + 1)
                max = 4 }
            "D6" -> { result = Random.nextInt(1, 6 + 1)
                max = 6 }
            "D8" -> { result = Random.nextInt(1, 8 + 1)
                max = 8 }
            "D10" -> { result = Random.nextInt(1, 10 + 1)
                max = 10 }
            "D12" -> { result = Random.nextInt(1, 12 + 1)
                max = 12 }
            "D20" -> { result = Random.nextInt(1, 20 + 1)
                max = 20 }
            else -> context.source.sendError(Text.translatable("text.candle-kingdoms-dice.dice_throw.invalid_dice"))
        }
        resultList.add(result)
    }

    val serverCmdSrc: ServerCommandSource = context.source as ServerCommandSource

    val player: ServerPlayerEntity = serverCmdSrc.entity as ServerPlayerEntity

    val playerIterator = serverCmdSrc.server.playerManager.playerList.iterator()

    val playerNameo = player.displayName



    if (throwCount == 1) {
        if (resultList[0] == max) {     // wth? Critical succes from bg3?
            val msg = Text.translatable(
            "text.candle-kingdoms-dice.dice_throw.one",
                diceLogo,
                playerNameo?.copy()?.setStyle(Style.EMPTY.withBold(true)),
                Text.literal(stringArg).setStyle(Style.EMPTY.withColor(16755200).withBold(true)),
                Text.literal("${resultList[0]}").setStyle(Style.EMPTY.withColor(16755200).withBold(true)),
                description
            ).setStyle(Style.EMPTY.withColor(16772528).withBold(false))

            context.source.server.broadcastText(msg)
        } else if (resultList[0] == 1 && stringArg != "D2") {       // Critical failure
            val msg = Text.translatable(
                "text.candle-kingdoms-dice.dice_throw.one",
                diceLogo,
                playerNameo?.copy()?.setStyle(Style.EMPTY.withBold(true)),
                Text.literal(stringArg).setStyle(Style.EMPTY.withColor(16755200).withBold(true)),
                Text.literal("${resultList[0]}").setStyle(Style.EMPTY.withColor(	16733525).withBold(true)),
                description
            ).setStyle(Style.EMPTY.withColor(16772528).withBold(false))

            context.source.server.broadcastText(msg)
        } else {
            val msg = Text.translatable(
                "text.candle-kingdoms-dice.dice_throw.one",
                diceLogo,
                playerNameo?.copy()?.setStyle(Style.EMPTY.withBold(true)),
                Text.literal(stringArg).setStyle(Style.EMPTY.withColor(16755200).withBold(true)),
                Text.literal("${resultList[0]}").setStyle(Style.EMPTY.withColor(16777045).withBold(true)),
                description
            ).setStyle(Style.EMPTY.withColor(16772528).withBold(false))

            context.source.server.broadcastText(msg)
        }


    } else {
        val rolls = resultList.joinToString(prefix = "(", postfix = ")", separator = ", ")
        val msg = Text.translatable(
            "text.candle-kingdoms-dice.dice_throw.multiple",
            diceLogo,
            playerNameo?.copy()?.setStyle(Style.EMPTY.withBold(true)),
            throwCount,
            Text.literal(stringArg).setStyle(Style.EMPTY.withColor(15897161).withBold(true)),
            resultList.sum(),
            description,
            Text.literal(rolls).setStyle(Style.EMPTY.withColor(16777215).withBold(false))
        ).setStyle(Style.EMPTY.withColor(16772528).withBold(false))

        context.source.server.broadcastText(msg)
    }

    return 1
}

//private fun roll(context: CommandContext<ServerCommandSource>, rollRange: Int): Int {
//
//    val serverCmdSrc: ServerCommandSource = context.source as ServerCommandSource
//
//    val player: ServerPlayerEntity = serverCmdSrc.entity as ServerPlayerEntity
//
//    val rollNumber = Random.nextInt(1, rollRange + 1)
//
//    val playerNameo = player.displayName?.string
//
////            val msg = Text.translatable("menu.candlepenisroll.msg_rolls", player.displayName).append(Text.translatable("menu.candlepenisroll.msg_num", Random.nextInt(1, rollRange + 1)) )
//    val msg = Text.literal("$playerNameo бросил D$rollRange. Выпало: $rollNumber")
//
//        // Gets iterator for all players
//    val playerIterator = serverCmdSrc.server.playerManager.playerList.iterator()
//
//        // Sends the message to everyone
//    for (p in playerIterator) {
//        p.sendMessage(msg)
//    }
//
//    return 1
//}