import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import java.util.UUID;

public class TweetRepository {
    private static final String TABLE_NAME = "tweets";

    private static final String TABLE_NAME_BY_LANGUAGE = TABLE_NAME + "ByLanguage";

    private Session session;

    public TweetRepository(Session session){
        this.session = session;
    }

    /*
    Creates Tweets table
     */
    public void createTable(){
        System.out.println("createTable - init");
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(TABLE_NAME).append("(")
                .append("id text PRIMARY KEY, ")
                .append("userName text, ")
                .append("text text, ")
                .append("dateCreated date, ")
                .append("geoLatitude double, ")
                .append("geoLongitude double);");   //verificar tipo date

        final String query = sb.toString();
        System.out.println(query);
        session.execute(query);
        System.out.println("createTable - end");
    }

    /*
    Creates tweets table by language
    => Para pesquisar os tweets feitos em uma lingua precisa
    */
    public void createTableTweetsByLanguage(){
        System.out.println("createTableTweetsByLanguage - init");
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(TABLE_NAME_BY_LANGUAGE).append("(")
                .append("language text, ")
                .append("userName text, ")
                .append("tweetText text, ")
                .append("tweetId text, ")
                .append("dateCreated date, ")
                .append("PRIMARY KEY (language,userName)) ")
                .append("WITH CLUSTERING ORDER BY (userName ASC);");

        final String query = sb.toString();
        System.out.println(query);
        session.execute(query);
        System.out.println("createTableTweetsByLanguage - end");
    }

    /*
    Alters the table Tweets and adds an extra column
    @params columnName,columnType
     */
    public void alterTabletweets(String columnName, String columnType){
        System.out.println("alterTabletweets - init");
        StringBuilder sb = new StringBuilder("ALTER TABLE ")
                .append(TABLE_NAME)
                .append(" ADD ")
                .append(columnName).append(" ").append(columnType).append(";");

        final String query = sb.toString();
        System.out.println(query);
        session.execute(query);
        System.out.println("alterTabletweets - end");
    }

    /*
    Insert a row in the table books
    @param book
     */
    public void inserttweet(Tweet tweet){
        System.out.println("inserttweet - init");
        StringBuilder sb = new StringBuilder("INSERT INTO ")
                .append(TABLE_NAME).append("(id, userName, text, dateCreated, geoLatitude, geoLongitude) ")
                .append("VALUES ('")
                .append(tweet.getId()).append("', '")
                .append(tweet.getUserName()).append("', '")
                .append(tweet.getText()).append("', '")
                .append(tweet.getDateCreated()).append("', ")
                .append(tweet.getGeoLatitude()).append(", ")
                .append(tweet.getGeoLongitude()).append(");");

        final String query = sb.toString();
        System.out.println(query);
        session.execute(query);
        System.out.println("inserttweet - end");
    }

    /*
    Insert a row in the table TweetsByLanguage
    @param tweet
    */
    public void inserttweetByLanguage(Tweet tweet){
        System.out.println("inserttweetByLanguage - init");
        StringBuilder sb = new StringBuilder("INSERT INTO ")
                .append(TABLE_NAME_BY_LANGUAGE).append("(language, userName, tweetText, tweetId, dateCreated) ")
                .append("VALUES ('")
                .append(tweet.getLanguage()).append("', '")
                .append(tweet.getUserName()).append("', '")
                .append(tweet.getText()).append("', '")
                .append(tweet.getId()).append("', '")
                .append(tweet.getDateCreated()).append("');");

        final String query = sb.toString();
        System.out.println(query);
        session.execute(query);
        System.out.println("inserttweetByLanguage - end");
    }

    /*
    Select tweet by language
    @return tweet
    */
    public void selectByLanguage(String language){
        System.out.println("selectByLanguage - init");
        StringBuilder sb = new StringBuilder("SELECT * FROM ")
                .append(TABLE_NAME_BY_LANGUAGE)
                .append(" WHERE language = '")
                .append(language)
                .append("';");

        final String query = sb.toString();
        System.out.println(query);
        ResultSet rs = session.execute(query);

        for (Row r : rs){
            System.out.println("Tweet = "
                     + r.getString("language") + ", "
                    + r.getString("userName") + ", "
                    + r.getString("tweetText") + ", "
                    + r.getString("tweetId") + ", "
                    +  r.getDate("dateCreated"));
        }
        System.out.println("selectByLanguage - end");
    }


    /*
    Select all tweets from Tweets
    @return tweets
    */
    public void selectAll(){
        System.out.println("selectAll - init");
        StringBuilder sb = new StringBuilder("SELECT * FROM ").append(TABLE_NAME);

        final String query = sb.toString();
        System.out.println(query);
        ResultSet rs = session.execute(query);

        for (Row r : rs){    ////CHECK O DATE
            System.out.println("Tweet = " + r.getString("id") + ", "
                    + r.getString("userName") + ", "
                    + r.getString("text") + ", "
                    + r.getDate("dateCreated") + ", "
                    + r.getDouble("geoLatitude") + ", "
                    +  r.getDouble("geoLongitude"));
        }

        System.out.println("selectAll - end");
    }

    /*
    Select all tweets from TweetsByLanguage
    @return tweets
     */
    public void selectAllByLanguage(){
        System.out.println("selectAllByLanguage - init");
        StringBuilder sb = new StringBuilder("SELECT * FROM ").append(TABLE_NAME_BY_LANGUAGE);

        final String query = sb.toString();
        System.out.println(query);
        ResultSet rs = session.execute(query);

        for (Row r : rs){    ////CHECK O DATE
            System.out.println("TweetByLanguage = "
                    + r.getString("language") + ", "
                    + r.getString("userName") + ", "
                    + r.getString("tweetText") + ", "
                    + r.getString("tweetId") + ", "
                    +  r.getDate("dateCreated"));
        }

        System.out.println("selectAllByLanguage - end");
    }

    /*
    Delete a specific tweet
    */
    public void deletetweet(Tweet tweet){
        System.out.println("deletetweet - init");
        UUID uid = tweet.getId();
        StringBuilder sb = new StringBuilder("DELETE FROM ")
                .append(TABLE_NAME)
                .append(" WHERE id = '")
                .append(uid).append("';");

        final String query = sb.toString();
        System.out.println(query);
        ResultSet rs = session.execute(query);

        System.out.println("deletetweet - end");
    }

    /*
    Delete a tweet by language and person
    */

    public void deletetweetByLanguage(String language, String userName){
        System.out.println("deletetweetByLanguage - init");
        StringBuilder sb = new StringBuilder("DELETE FROM ")
                .append(TABLE_NAME_BY_LANGUAGE)
                .append(" WHERE language = '")
                .append(language)
                .append("' AND userName = '")
                .append(userName)
                .append("';");

        final String query = sb.toString();
        System.out.println(query);
        ResultSet rs = session.execute(query);
        System.out.println("deletetweetByLanguage - end");
    }


    /*
    Delete table
    @param tableName -> name of the table to delete
     */
    public void deleteTable(String tableName){
        System.out.println("deleteTable - init");
        StringBuilder sb = new StringBuilder("DROP TABLE IF EXISTS ")
                .append(tableName);

        final String query = sb.toString();
        System.out.println(query);
        ResultSet rs = session.execute(query);
        System.out.println("deleteTable - end");
    }

}
