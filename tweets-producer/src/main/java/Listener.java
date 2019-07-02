import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class Listener implements StatusListener {
    //private ProducerDemo ProducerDemo = new ProducerDemo();
    private ProducerDemoWithSerializer ProducerDemoWithSerializer = new ProducerDemoWithSerializer();

    public void onStatus(Status status) {
        double geoLatitude = 0.0;   //valor default
        double geoLongitude = 0.0;   // valor default
        String language = "none";  // valor default

        if (status.getGeoLocation() != null){
            geoLatitude = status.getGeoLocation().getLatitude();
            geoLongitude = status.getGeoLocation().getLongitude();
        }
        if (status.getLang() != null){
            language = status.getLang();
        }

        Tweet tweet = new Tweet(status.getUser().getScreenName(),status.getText(),status.getCreatedAt(), language, geoLatitude, geoLongitude);
        // Normal producer
        //this.ProducerDemo.send(tweet.getText());

        // Serializer
        this.ProducerDemoWithSerializer.send(tweet);
        //System.out.println(tweet);
    }

    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
    }
    public void onTrackLimitationNotice(int i) {
    }
    public void onScrubGeo(long l, long l1) {
    }
    public void onStallWarning(StallWarning stallWarning) {
    }
    public void onException(Exception e) {
        e.printStackTrace();
    }

    public void closeProducer(){
        // Normal producer
        //this.ProducerDemo.close();
        // Serializer producer
        this.ProducerDemoWithSerializer.close();

    }
}
