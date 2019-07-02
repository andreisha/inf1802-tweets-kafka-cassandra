import twitter4j.*;

import java.lang.String;
import java.util.Date;

public class Tweet {

    private String userName;      // Nome do usuario que enviou o tweet
    private String text;        // Texto do tweet
    private Date dateCreated;  // Data em que o tweet foi enviado
    private String language;
    private double geoLatitude;
    private double geoLongitude;

    public Tweet(String userName,String text, Date dateCreated, String language, double geoLatitude, double geoLongitude) {
        this.userName = userName;
        this.text = text;
        this.dateCreated = dateCreated;
        this.language = language;
        this.geoLatitude = geoLatitude;
        this.geoLongitude = geoLongitude;
    }

    public String toString(){   // Retorna string com as variaveis do objeto
        return "Usuario - " + userName + ", " +
                "Texto - " + text + ", " +
                "Data - " + dateCreated + ", " +
                "Lingua - " + language + ", " +
                "Latitude - " + geoLatitude + ", " +
                "Longitude - " + geoLongitude;
    }

    public String getUserName(){
        return this.userName;
    }

    public String getText(){
        return this.text;
    }

    public Date getDateCreated(){
        return this.dateCreated;
    }

    public String getLanguage(){
        return this.language;
    }

    public double getGeoLatitude(){
        return this.geoLatitude;
    }

    public double getGeoLongitude(){
        return this.geoLongitude;
    }

}