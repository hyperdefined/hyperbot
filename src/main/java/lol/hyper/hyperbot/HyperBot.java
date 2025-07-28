package lol.hyper.hyperbot;

import lol.hyper.hyperbot.bot.DiscordHyperBot;
import lol.hyper.hyperbot.storage.Maintenance;
import lol.hyper.hyperbot.utils.JSONUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

public class HyperBot {

    private static String userAgent;
    private static boolean debug = false;
    private static JSONObject config;
    private static final ArrayList<String> servers = new ArrayList<>();

    public static void main(String[] args) {
        System.setProperty("log4j.configurationFile", "log4j2config.xml");
        Logger logger = LogManager.getLogger(HyperBot.class);
        logger.info("{} {}bit", System.getProperty("os.name"), System.getProperty("sun.arch.data.model"));
        logger.info("Current directory {}", System.getProperty("user.dir"));
        userAgent = "hyperbot (+https://github.com/hyperdefined/hyperbot)";

        File configFile = new File("config.json");
        if (!configFile.exists()) {
            logger.error("Missing config, unable to start.");
            System.exit(1);
        }

        logger.info("Config file: {}", System.getProperty("user.dir") + File.separator + configFile);
        config = new JSONObject(JSONUtils.readFile(configFile));
        for (int i = 0; i < config.getJSONArray("servers").length(); i++) {
            servers.add(config.getJSONArray("servers").getString(i));
        }

        // if we are in debug mode, don't do database things.
        if (args.length != 0) {
            if (args[0].equalsIgnoreCase("debug")) {
                debug = true;
                logger.warn("Running in debug mode!!!!");
            }
        }

        if (!debug) {
            Maintenance maintenance = new Maintenance();
            try {
                maintenance.startUp();
            } catch (SQLException exception) {
                logger.error("Unable to perform startup database operations.", exception);
                System.exit(1);
            }
        }

        new DiscordHyperBot(config);
    }

    public static JSONObject getConfig() {
        return config;
    }

    public static String getUserAgent() {
        return userAgent;
    }

    public static ArrayList<String> getServers() {
        return servers;
    }

    public static boolean isDebug() {
        return debug;
    }
}
