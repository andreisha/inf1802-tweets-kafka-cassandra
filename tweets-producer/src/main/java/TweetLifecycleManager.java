import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.Serializable;

public class TweetLifecycleManager implements LifecycleManager, Serializable {

    private TwitterStream twitterStream;
    private TwitterStreamFactory twitterStreamFactory;
    private Listener listener;

    public TweetLifecycleManager(){
        // Variaveis de ambiente
        String _consumerKey = System.getenv().get("TWITTER_CONSUMER_KEY");
        String _consumerSecret = System.getenv().get("TWITTER_CONSUMER_SECRET");
        String _accessToken = System.getenv().get("TWITTER_ACCESS_TOKEN");
        String _accessTokenSecret = System.getenv().get("TWITTER_ACCESS_TOKEN_SECRET");

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(_consumerKey)
                .setOAuthConsumerSecret(_consumerSecret)
                .setOAuthAccessToken(_accessToken)
                .setOAuthAccessTokenSecret(_accessTokenSecret);

        this.twitterStreamFactory = new TwitterStreamFactory(configurationBuilder.build());
        this.listener = new Listener();
        this.twitterStream = twitterStreamFactory.getInstance();
        twitterStream.addListener(listener);

    }

    public void start() {

        // Filtro para receber tweets com os termos precisados
        String tracked_terms = "bolsonaro";
        FilterQuery query = new FilterQuery();
        query.track(tracked_terms.split(","));
        twitterStream.filter(query);  // Comeco do streaming
    }

    public void stop() {

        twitterStream.shutdown();  // Para o streaming
        this.listener.closeProducer();
    }
}
