package candle.dice

import candle.dice.commands.diceCommand
import candle.dice.commands.sizeRollCommand

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory


object CandleKingdomsDice : ModInitializer {
    private val logger = LoggerFactory.getLogger("candle-kingdoms-dice")

	override fun onInitialize() {
		diceCommand
		sizeRollCommand

		logger.info("Hello Fabric world!")
	}
}