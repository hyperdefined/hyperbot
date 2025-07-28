package lol.hyper.hyperbot.bot.events;

import net.dv8tion.jda.api.events.GatewayPingEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class GatewayPingListener extends ListenerAdapter {

    private final Logger logger = LogManager.getLogger(this);

    @Override
    public void onGatewayPing(@NotNull GatewayPingEvent event) {
        logger.info("New ping from gateway! {}ms", event.getNewPing());
    }
}
