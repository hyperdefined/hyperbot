package lol.hyper.hyperbot.bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Random;

public class CommandRoll implements HyperCommand {

    private final SlashCommandInteractionEvent event;
    private final OptionMapping inputRoll;
    private final Logger logger = LogManager.getLogger(this);

    public CommandRoll(SlashCommandInteractionEvent event, OptionMapping roll) {
        this.event = event;
        this.inputRoll = roll;
    }

    @Override
    public void run() {
        String inputString = inputRoll.getAsString();
        int inputInt;
        try {
            inputInt = Integer.parseInt(inputString);
        } catch (NumberFormatException ignored) {
            event.reply("You must enter a valid number! No decimals or commas.").queue();
            logger.warn("{} is not a valid int!", inputString);
            return;
        }

        if (inputInt < 1) {
            event.reply("You must enter a number above 1.").queue();
            logger.warn("{} is not above 1!", inputInt);
            return;
        }

        Random rand = new Random();
        int roll = rand.nextInt(inputInt) + 1;
        EmbedBuilder embed = new EmbedBuilder();
        embed.setDescription(event.getUser().getName() + " rolled **" + roll + "**!");
        embed.setColor(Color.RED);

        event.replyEmbeds(embed.build()).queue();
        logger.info("{} rolled a {}", event.getUser().getId(), roll);
    }
}
