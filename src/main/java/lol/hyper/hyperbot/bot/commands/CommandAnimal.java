package lol.hyper.hyperbot.bot.commands;

import lol.hyper.hyperbot.utils.JSONUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;

public class CommandAnimal implements HyperCommand {

    private final SlashCommandInteractionEvent event;
    private final String type;
    private final Logger logger = LogManager.getLogger(this);

    public CommandAnimal(SlashCommandInteractionEvent event, String type) {
        this.event = event;
        this.type = type;
    }

    public void run() {
        JSONObject types = JSONUtils.fetchJSON("https://api.tinyfox.dev/img?animal=animal&json");
        if (types == null) {
            event.reply("Unable to fetch API.").queue();
            return;
        }
        JSONArray animalsArray = types.getJSONArray("available");
        if (animalsArray == null) {
            event.reply("Unable to fetch API.").queue();
            return;
        }

        ArrayList<String> animals = new ArrayList<>();
        for (int i = 0; i < animalsArray.length(); i++) {
            animals.add(animalsArray.getString(i));
        }

        if (!animals.contains(type)) {
            event.reply("Not a valid animal. Valid animals are: " + Strings.join(animals, ',')).queue();
            return;
        }

        JSONObject animal = JSONUtils.fetchJSON("https://api.tinyfox.dev/img?animal=" + type + "&json");
        if (animal == null) {
            event.reply("Unable to fetch API.").queue();
            return;
        }

        String image = animal.getString("loc");
        logger.info("Giving user image: https://api.tinyfox.dev{}", image);

        EmbedBuilder embed = new EmbedBuilder();
        // set the title to be the type but capitalize the first letter
        embed.setTitle(type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase() + " Machine");
        embed.setColor(Color.RED);
        embed.setImage("https://api.tinyfox.dev" + image);
        event.replyEmbeds(embed.build()).queue();
    }
}
