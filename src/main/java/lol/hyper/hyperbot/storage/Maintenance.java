package lol.hyper.hyperbot.storage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class Maintenance {

    private final Logger logger = LogManager.getLogger(this);

    public void startUp() throws SQLException {
        Database databaseConnector = new Database();
        Connection connection = databaseConnector.connect();
        DatabaseMetaData metadata = connection.getMetaData();

        ResultSet resultSet = metadata.getTables(null, null, "quotes", null);
        boolean tableExists = false;
        while (resultSet.next()) {
            String name = resultSet.getString("TABLE_NAME");
            if (name != null && name.equalsIgnoreCase("quotes")) {
                tableExists = true;
                break;
            }
        }
        resultSet.close();

        if (!tableExists) {
            logger.info("Database does not exist, creating...");
            Statement statement = connection.createStatement();
            statement.executeUpdate(SQLQueries.QUOTE_TABLE_SCHEMA);
            statement.close();
            connection.close();
            return;
        }
        logger.info("Database already exists.");
    }
}
