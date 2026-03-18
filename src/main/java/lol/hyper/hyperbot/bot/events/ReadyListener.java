package lol.hyper.hyperbot.bot.events;

import lol.hyper.hyperbot.bot.HourlyWolf;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReadyListener extends ListenerAdapter {

    private final Logger logger = LogManager.getLogger(this);
    private final HourlyWolf hourlyWolf;

    public ReadyListener(HourlyWolf hourlyWolf) {
        this.hourlyWolf = hourlyWolf;
    }

    @Override
    public void onReady(ReadyEvent event) {
        logger.info("Connected guilds: {}", event.getJDA().getGuilds());
        hourlyWolf.start();
    }
}
