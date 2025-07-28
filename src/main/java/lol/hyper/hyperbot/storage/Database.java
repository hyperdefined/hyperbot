package lol.hyper.hyperbot.storage;

import lol.hyper.hyperbot.HyperBot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private final Logger logger = LogManager.getLogger(Database.class);

    private final String connectionURL;
    private final String username;
    private final String password;

    public Database() {
        JSONObject databaseConfig = HyperBot.getConfig().getJSONObject("database");
        String host = databaseConfig.getString("host");
        String port = databaseConfig.getString("port");
        this.username = databaseConfig.getString("username");
        this.password = databaseConfig.getString("password");
        String database = databaseConfig.getString("database");
        this.connectionURL = "jdbc:mysql://" + host + ":" + port + "/" + database;
    }

    public Connection connect() throws SQLException {
        logger.info("Attempting to connect to {}", connectionURL);
        return DriverManager.getConnection(connectionURL, username, password);
    }
}
