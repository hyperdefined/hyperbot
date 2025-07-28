package lol.hyper.hyperbot.bot.events;

import lol.hyper.hyperbot.HyperBot;
import lol.hyper.hyperbot.bot.DiscordHyperBot;
import lol.hyper.hyperbot.bot.commands.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SlashCommandListener extends ListenerAdapter {

    private final Logger logger = LogManager.getLogger(SlashCommandListener.class);
    private final DiscordHyperBot bot;

    public SlashCommandListener(DiscordHyperBot bot) {
        this.bot = bot;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String guild = event.getGuild().getId();
        if (!HyperBot.getServers().contains(guild)) {
            // ignore servers we don't approve the bot in
            return;
        }

        String userID = event.getUser().getId();
        List<OptionMapping> options = event.getOptions();
        logger.info("{} ran command '{}' with args {}", userID, event.getName(), options);

        switch (event.getName()) {
            case "roll": {
                if (options.isEmpty()) {
                    event.reply("You must give me a number to roll!").queue();
                    logger.warn("No number was supplied for 'roll'");
                    break;
                }
                OptionMapping input = event.getOption("number");
                if (input == null) {
                    event.reply("You must give me a number to roll!").queue();
                    logger.warn("OptionMapping 'number' was not supplied for 'roll'");
                    break;
                }
                new CommandRoll(event, input).run();
                break;
            }
            case "awoo": {
                new CommandAwoo(event).run();
                break;
            }
            case "bark": {
                new CommandBark(event).run();
                break;
            }
            case "joke": {
                if (options.isEmpty()) {
                    event.reply("What type of joke do you want? The options are: random, programming, dark, or pun.").queue();
                    logger.warn("No type was supplied for 'joke'");
                    break;
                }
                OptionMapping type = event.getOption("type");
                if (type == null) {
                    event.reply("What type of joke do you want? The options are: random, programming, dark, or pun.").queue();
                    logger.warn("OptionMapping 'type' was not supplied for 'joke'");
                    break;
                }
                new CommandJoke(event, type.getAsString()).run();
                break;
            }
            case "ping": {
                new CommandPing(event, bot).run();
                break;
            }
            case "topquotes": {
                new CommandTopQuotes(event).run();
                break;
            }
            case "randomquote": {
                if (HyperBot.isDebug()) {
                    event.reply("Bot is in debug mode, cannot get quotes.").queue();
                    break;
                }
                // if the options are empty, send null for author
                if (options.isEmpty()) {
                    new CommandRandomQuote(event, null).run();
                    break;
                }
                OptionMapping author = event.getOption("author");
                if (author == null) {
                    // This shouldn't happen...?
                    logger.warn("OptionMapping 'author' was not supplied for 'randomquote'");
                    break;
                }
                new CommandRandomQuote(event, author.getAsString()).run();
                break;
            }
            case "status": {
                new CommandStatus(event, bot).run();
                break;
            }
            case "animal": {
                if (options.isEmpty()) {
                    event.reply("What type of animal do you want?").queue();
                    logger.warn("No type was supplied for 'animal'");
                    break;
                }
                OptionMapping type = event.getOption("animal");
                if (type == null) {
                    event.reply("What type of animal do you want?").queue();
                    logger.warn("OptionMapping 'type' was not supplied for 'animal'");
                    break;
                }
                new CommandAnimal(event, type.getAsString()).run();
                break;
            }
        }
    }
}
