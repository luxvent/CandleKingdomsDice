package candle.dice.commands

import net.minecraft.entity.attribute.EntityAttributes
import net.silkmc.silk.commands.command
import kotlin.random.Random
import net.minecraft.text.Text
import kotlin.math.round

val sizeRollCommand = command("sizeRoll") {
    literal("roll") {
        runs {
            val randomSize = round(Random.nextDouble(0.001, 3.2)*100) / 100
            source.player?.getAttributeInstance(EntityAttributes.GENERIC_SCALE)?.baseValue = randomSize
            source.sendMessage(Text.literal("Size: ${randomSize.toString()}"))
        }
    }
    literal("normal") {
        runs {
            source.player?.getAttributeInstance(EntityAttributes.GENERIC_SCALE)?.baseValue = 1.0
            }
    }

    literal("giant") {
        runs {
            source.player?.getAttributeInstance(EntityAttributes.GENERIC_SCALE)?.baseValue = 3.0
            }
    }

    literal("small") {
        runs {
            source.player?.getAttributeInstance(EntityAttributes.GENERIC_SCALE)?.baseValue = 0.2
            }
    }

    literal("micro") {
        runs {
            source.player?.getAttributeInstance(EntityAttributes.GENERIC_SCALE)?.baseValue = 0.01
            }
    }
}