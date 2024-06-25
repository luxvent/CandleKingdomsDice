package candle.dice

import candle.dice.commands.diceCommand
import candle.dice.commands.sizeRollCommand

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory


object CandlePenisDice : ModInitializer {
    private val logger = LoggerFactory.getLogger("candle-penis-dice")

	override fun onInitialize() {
		diceCommand
		sizeRollCommand

		logger.info("Hello Fabric world!")
	}
}