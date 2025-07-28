package lol.hyper.hyperbot.bot.commands;

import lol.hyper.hyperbot.utils.JSONUtils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.json.JSONObject;

public class CommandJoke implements HyperCommand {

    private final SlashCommandInteractionEvent event;
    private final String type;

    public CommandJoke(SlashCommandInteractionEvent event, String type) {
        this.event = event;
        this.type = type;
    }

    public void run() {
        String url = switch (type) {
            case "random" -> "https://v2.jokeapi.dev/joke/Any?type=single";
            case "programming" -> "https://v2.jokeapi.dev/joke/Programming?type=single";
            case "dark" -> "https://v2.jokeapi.dev/joke/Dark?type=single";
            case "pun" -> "https://v2.jokeapi.dev/joke/Pun?type=single";
            default -> null;
        };

        if (url == null) {
            event.reply("Not a valid joke type. The options are: random, programming, dark, or pun.").queue();
            return;
        }

        JSONObject jokeResponse = JSONUtils.fetchJSON(url);
        if (jokeResponse == null) {
            event.reply("Unable to fetch jokes. Something went wrong with the request.").queue();
            return;
        }

        String joke = jokeResponse.getString("joke");
        event.reply(joke).queue();
    }
}
