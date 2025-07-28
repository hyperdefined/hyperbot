package lol.hyper.hyperbot.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class CommandBark implements HyperCommand {

    private final Logger logger = LogManager.getLogger(this);

    private final SlashCommandInteractionEvent event;

    public CommandBark(SlashCommandInteractionEvent event) {
        this.event = event;
    }

    public void run() {
        // generate a number between 10-100
        Random random = new Random();
        int result = random.nextInt(100 - 10) + 10;
        logger.info("Rolled {} on bark", result);
        StringBuilder bark = new StringBuilder();
        for (int i = 0; i < result; i++) {
            if (i == 0) {
                bark = new StringBuilder("bark");
            } else {
                bark.append(" bark");
            }
        }
        event.reply(bark.toString()).queue();
    }
}
