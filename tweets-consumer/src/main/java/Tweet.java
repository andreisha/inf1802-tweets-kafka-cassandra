import java.lang.String;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;
import com.datastax.driver.core.LocalDate;
import com.datastax.driver.core.utils.UUIDs;
import java.util.Calendar;

public class Tweet {
    private String id;
    private String userName;      // Nome do usuario que enviou o tweet
    private String text;        // Texto do tweet
    private Date dateCreated;  // Data em que o tweet foi enviado
    //private LocalDate dateCreated;
    private String language;
    private double geoLatitude;
    private double geoLongitude;

    public Tweet() {
        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();
    }

    public Tweet(String userName,String text, Date dateCreated, String language, double geoLatitude, double geoLongitude) {
        //this.id = UUIDs.timeBased();
        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();
        System.out.println(uuid);
        System.out.println(this.id);
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

    public String getId(){
        return this.id;
    }

    public String getUserName (){
        return this.userName;
    }

    public String getText(){
        return this.text;
    }

    public Date getDateCreated(){return this.dateCreated;}

    public LocalDate getDateCreatedForCassandra(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.dateCreated);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        LocalDate dateCreatedForCassandra = LocalDate.fromYearMonthDay(year,month,day);

        return dateCreatedForCassandra;
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