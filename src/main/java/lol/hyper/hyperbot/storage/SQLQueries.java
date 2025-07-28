package lol.hyper.hyperbot.storage;

public class SQLQueries {

    public static final String QUOTE_TABLE_SCHEMA = "CREATE TABLE quotes (QuoteID VARCHAR(255) PRIMARY KEY, QuoteAuthor VARCHAR(255), QuoteText VARCHAR(255), QuoteDate BIGINT);";
    public static final String ADD_QUOTE = "INSERT INTO quotes (QuoteID, QuoteAuthor, QuoteText, QuoteDate) VALUES (?, ?, ?, ?);";
    public static final String SELECT_BY_NAME = "Select QuoteText, QuoteDate from quotes where LOWER(QuoteAuthor) = ? ORDER BY RAND() LIMIT 1;";
    public static final String SELECT_RANDOM = "SELECT QuoteAuthor, QuoteDate, QuoteText FROM quotes ORDER BY RAND() LIMIT 1;";
    public static final String SELECT_ID = "SELECT 1 FROM quotes WHERE QuoteID = ?";
    public static final String SELECT_TOP = "SELECT LOWER(QuoteAuthor) AS NormalizedAuthor, COUNT(*) AS QuoteCount FROM quotes GROUP BY NormalizedAuthor ORDER BY QuoteCount DESC LIMIT 30";
}
