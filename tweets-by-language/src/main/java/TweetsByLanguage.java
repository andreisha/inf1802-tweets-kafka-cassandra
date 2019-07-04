import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import java.util.Date;

import java.util.Arrays;
import java.util.Properties;

public class TweetsByLanguage {

    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG,"tweets_application"); // Modify if new run
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass());

        KStreamBuilder builder = new KStreamBuilder();

        // pega stream entrante
        KStream<String, String> tweet = builder.stream("tweets_input");
        //tweet.print();

        // Vai de (null, {"userName":userName, "text":text, "dateCreated":dateCreated, "language":language, "geoLatitude":geoLatitude, "geoLongitude":geoLongitude} ) a (language, userName)
        tweet = tweet.selectKey( (key,value) ->  value.split("language")[1]); // bota ":language, "geoLatitude":geoLatitude, "geoLongitude":geoLongitude} como key
        tweet = tweet.selectKey( (key,value) ->  key.split(":")[1]); // bota language, "geoLatitude":geoLatitude, "geoLongitude":geoLongitude como key
        tweet = tweet.selectKey( (key,value) ->  key.split(",")[0]); // bota language como key
        //tweet.print();

        KStream<String, String> tweetLanguage = tweet.mapValues( value ->  value.split(",")[0]);  // bota {"userName":userName como value
        tweetLanguage = tweetLanguage.mapValues( value ->  value.split(":")[1]);  // bota userName como value
        //tweetLanguage.print();

        KTable<String, Long> languageCounts = tweetLanguage.groupByKey().count("counts");
        //languageCounts.print();
        languageCounts.to(Serdes.String(), Serdes.Long(), "tweets_by_language");

        KafkaStreams streams = new KafkaStreams(builder,config);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));


    }

}
