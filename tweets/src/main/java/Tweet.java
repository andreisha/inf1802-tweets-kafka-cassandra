
import com.datastax.driver.core.LocalDate;

import java.util.UUID;


public class Tweet {
    private UUID id;
    private String userName;
    private String text;
    private LocalDate dateCreated;
    private String source;
    private boolean isTruncated;
    private double geoLatitude;   // implementar melhor ao ver como os dados sao enviados pelo tweeter
    private double geoLongitude;
    private boolean isFavorited;
    private long[] contributors;
    private String language;

    Tweet(){}

    public Tweet( UUID id, String userName, String text, LocalDate dateCreated, String language, double geoLatitude, double geoLongitude ){
        this.id = id;
        this.userName = userName;
        this.text = text;
        this.dateCreated = dateCreated;
        this.language = language;
        this.geoLatitude = geoLatitude;
        this.geoLongitude = geoLongitude;
    }

    public UUID getId(){
        return id;
    }

    public void setId(UUID id){
        this.id = id;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public LocalDate getDateCreated(){
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated){
        this.dateCreated = dateCreated;
    }

    // outros getSet

    public String getSource(){
        return source;
    }

    public void setSource(String source){
        this.source = source;
    }

    public boolean getTruncated(){
        return isTruncated;
    }

    public void setTruncated(boolean isTruncated){ this.isTruncated = isTruncated; }

    public double getGeoLatitude(){
        return geoLatitude;
    }

    public void setGeoLatitude(double geoLatitude){
        this.geoLatitude = geoLatitude;
    }

    public double getGeoLongitude(){
        return geoLongitude;
    }

    public void setGeoLongitude(double geoLongitude){
        this.geoLongitude = geoLongitude;
    }

    public boolean getFavorited(){
        return isFavorited;
    }

    public void setFavorited(boolean isFavorited){ this.isFavorited = isFavorited; }

    public long[] getContributors(){
        return contributors;
    }

    public void setContributors(long[] contributors){
        this.contributors = contributors;
    }

    public String getLanguage(){
        return language;
    }

    public void setLanguage(String language){
        this.language = language;
    }
}

