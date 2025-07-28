package lol.hyper.hyperbot.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class CommandAwoo implements HyperCommand {

    private final Logger logger = LogManager.getLogger(this);

    private final SlashCommandInteractionEvent event;

    public CommandAwoo(SlashCommandInteractionEvent event) {
        this.event = event;
    }

    public void run() {
        // generate a number between 50-200
        Random random = new Random();
        int result = random.nextInt(200 - 50) + 50;
        logger.info("Rolled {} on awoo", result);
        StringBuilder awoo = new StringBuilder();
        for (int i = 0; i < result; i++) {
            if (i == 0) {
                awoo = new StringBuilder("Aw");
            } else {
                awoo.append("o");
            }
        }
        event.reply(awoo.toString()).queue();
    }
}
