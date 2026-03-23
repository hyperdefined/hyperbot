package lol.hyper.hyperbot.bot;

import lol.hyper.hyperbot.HyperBot;
import lol.hyper.hyperbot.utils.JSONUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.awt.*;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class HourlyWolf {

    private final ScheduledExecutorService scheduler;
    private final Logger logger = LogManager.getLogger(this);
    private final DiscordHyperBot discordHyperBot;
    private final String server;
    private final String channel;

    private final long PERIOD_MILLIS = TimeUnit.HOURS.toMillis(1);
    private final AtomicLong nextRunMillis = new AtomicLong();

    public HourlyWolf(DiscordHyperBot discordHyperBot, String server, String channel) {
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.discordHyperBot = discordHyperBot;
        this.server = server;
        this.channel = channel;
    }

    /**
     * Start the scheduler to run.
     */
    public void start() {
        long now = System.currentTimeMillis();
        nextRunMillis.set(now + PERIOD_MILLIS);

        scheduler.scheduleAtFixedRate(() -> {
            try {
                run();
            } catch (Exception exception) {
                logger.error("Error while running hourly wolf task", exception);
            } finally {
                nextRunMillis.addAndGet(PERIOD_MILLIS);
            }
        }, 0, 1, TimeUnit.HOURS);
    }

    /**
     * Execute the hourly task.
     */
    public void run() {
        logger.info("Running hourly wolf");
        JSONObject wolfyData = fetch();
        if (wolfyData == null) {
            logger.warn("Wolfy data returned null :(");
            return;
        }

        String path = wolfyData.getString("loc");
        String fullPath = "https://api.tinyfox.dev" + path;

        Guild guild = discordHyperBot.bot().getGuildById(server);
        if (guild == null) {
            logger.warn("Unable to find wolfy server to post in.");
            return;
        }

        TextChannel channelToPost = guild.getTextChannelById(channel);
        if (channelToPost == null) {
            logger.warn("Unable to find wolfy channel to post in.");
            return;
        }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Wolf of the Hour");
        embed.setColor(Color.BLACK);
        embed.setTimestamp(Instant.now());
        embed.setImage(fullPath);

        channelToPost.sendMessageEmbeds(embed.build()).queue();
        logger.info("Next run: {}", Instant.ofEpochMilli(nextRunMillis.get()));
    }

    /**
     * Get a JSONObject from a URL.
     *
     * @return The response JSONObject. Returns null if there was some issue.
     */
    private JSONObject fetch() {
        String WOLF_URL = "https://api.tinyfox.dev/img?animal=woof&json";
        logger.info("Fetching JSONObject from {}", WOLF_URL);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(WOLF_URL))
                    .header("Accept", "application/json")
                    .header("User-Agent", HyperBot.getUserAgent())
                    .GET()
                    .build();

            HttpResponse<String> response = JSONUtils.client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return new JSONObject(response.body());
            } else {
                logger.error(
                        "HTTP status code {} for {} in getting JSONObject",
                        response.statusCode(),
                        WOLF_URL
                );
                return null;
            }
        } catch (Exception exception) {
            logger.error("Unable to request JSONObject", exception);
            return null;
        }
    }
}