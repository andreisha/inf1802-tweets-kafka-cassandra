import com.datastax.driver.core.*;
import com.datastax.driver.core.utils.UUIDs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.*;

public class HelloTweet {

    public static void main(String[] args){
        System.out.println("Hello, Cassandra!");
        Cluster cluster = null;
        try{
            cluster = Cluster.builder()
                    .addContactPoint("localhost")
                    .build();

            Session session = cluster.connect();

            ResultSet rs = session.execute("select release_version from system.local");
            Row row = rs.one();
            System.out.println(((Row) row).getString("release_version"));

            KeyspaceRepository sr = new KeyspaceRepository(session);

            // deletar keyspace se ja existe
            //sr.deleteKeyspace("library");
            //System.out.println("Delete Keyspace library");


            sr.createKeyspace("library", "SimpleStrategy", 1);
            System.out.println("Create repository");
            sr.useKeyspace("library");
            System.out.println("Using repository library");

            TweetRepository br = new TweetRepository(session);
            br.createTable();
            System.out.println("Create table tweets done");
            br.createTableTweetsByLanguage();
            System.out.println("Create table tweetsByLanguage done");

            // 5 items da classe tweet
            List<Tweet> tweets = new ArrayList<Tweet>();
            for (int i = 0; i< 5; i++){
                LocalDate dt = LocalDate.fromYearMonthDay(2018,8,05 + i);
                Tweet tweet = new Tweet(UUIDs.timeBased(), "andrea" + i, "Hola que dia mas bonito", dt, "lingua" + i , 153.0 + i, 140.0 + i);
                br.inserttweet(tweet);
                tweets.add(tweet);
            }
            System.out.println("Insert 5 tweets done");

            // 5 items da classe tweetsByLanguage
            List<Tweet> tweetsByLanguage = new ArrayList<Tweet>();
            for (int i = 0; i< 5; i++){
                LocalDate dt = LocalDate.fromYearMonthDay(2018,8,05 + i);
                Tweet tweetByLanguage = new Tweet(UUIDs.timeBased(), "andrea" + i, "Hola que dia mas bonito", dt, "lingua" + i, 130.0 + i, 120.0 + i);
                br.inserttweetByLanguage(tweetByLanguage);
                tweetsByLanguage.add(tweetByLanguage);
            }
            System.out.println("Insert 5 tweetsByLanguage done ");

            br.selectAll();
            br.selectAllByLanguage();

            br.selectByLanguage("lingua4");

            br.deletetweet(tweets.get(0));
            System.out.println("delete 1 tweet");

            Tweet tw = tweetsByLanguage.get(0);
            br.deletetweetByLanguage(tw.getLanguage(), tw.getUserName());
            System.out.println("delete 1 tweetByLanguage");

            br.selectAll();
            br.selectAllByLanguage();


            br.deleteTable("tweets");
            System.out.println("Delete table tweets done ");
            br.deleteTable("tweetsByLanguage");
            System.out.println("Delete table tweetsByLanguage done ");

            sr.deleteKeyspace("library");
            System.out.println("Delete Keyspace library done");
        } finally{
            if (cluster != null) cluster.close();
        }
    }
}
