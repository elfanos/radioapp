import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Rasmus on 24/01/17.
 *
 * Class which keeps all values from the
 * xml. Then used for the controller to
 * show the values in the view
 *
 * @author Rasmus Dahlkvist
 */
public class ApiCall {
    private String saxDirections;
    private String apiUrl;
    private Object apiModel;
    private String modelName;
    private String storedXml;
    private Boolean isTwelveBefore;
    private Boolean isTwelveAfter;
    Calendar calendar = Calendar.getInstance();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Constructor for the class ApiCall
     * Set one api-model to be used when
     * collect the data in the xml-file
     *
     * @param apiModel is the model class as
     *                 a object class.
     * */
    public  ApiCall(Object apiModel){

        this.setApiModel(apiModel);
        this.setTwelveBefore(false);
        this.setTwelveAfter(true);
    }
    /**
     * Set the modelName
     *
     * @param modelName set the model name as a string
     * */
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    /**
     * @return the apiModel class which is the class
     *          that the xml is going to store the information
     *          in.
     * */
    public Class getApiModel() {
        return apiModel.getClass();
    }

    /**
     * Set the apiModel
     *
     * @param apiModel the model of the api.
     */
    public void setApiModel(Object apiModel) {
        this.apiModel = apiModel;
    }

    /**
     * @return the given sax direction
     * */
    public String getSaxDirections() {
        return saxDirections;
    }

    /**
     * Set the sax direction
     *
     * @param saxDirections which is direction for the sax
     *                      to check if its schedule or
     *                      channel xml from the api.
     * */
    public void setSaxDirections(String saxDirections) {
        this.saxDirections = saxDirections;
    }

    /**
     * @return the api url as a string
     * */
    public String getApiUrl() {
        return apiUrl;
    }

    /**
     * Set the api url
     *
     * @param apiUrl is the url of the api
     * */
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    /**
     * @return stored xml for testing purpose
     * */
    public String getStoredXml() {
        return storedXml;
    }

    /**
     * Set stored xml for testing purpose
     *
     * @param storedXml is a xml stored for testing the application
     *                  just add a xml to the project and use it as testing
     * */
    public void setStoredXml(String storedXml) {
        this.storedXml = storedXml;
    }


    /**
     *
     * Check the date and retrieve information
     * from the xml based on date.
     *
     * @param start is a string of the date
     *              given by the xml
     * */
    public void dateEndTime(String start){
        String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.
                ofInstant(in.toInstant(), ZoneId.systemDefault());
        Date startDate = null;
        Date localDate  = Date.
                from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        calendar.setTime(localDate);
        calendar.add(Calendar.HOUR,-12);
        Date newTime = calendar.getTime();
        calendar.setTime(localDate);
        calendar.add(Calendar.HOUR, +12);
        Date timeLimit = calendar.getTime();

        try {
            startDate = sdf.parse(start);

        } catch (ParseException e){
            System.out.println("Parse not working " + e);
        }
        switch (startDate.compareTo(newTime)){
            case -1:
                break;
            case 0:
                break;
            case 1:
                this.setTwelveBefore(true);
                break;
        }
        switch (startDate.compareTo(timeLimit)){
            case -1:
            break;
            case 1:
                this.setTwelveAfter(false);
                break;
        }

    }
    /***
     * Return the dateTomorrow in reworked String
     *
     * @return a date tomorrow as a reworked string
     *          for readability.
     */
    public String dateTomorrow(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, +24);
        Date dateTomorrow = calendar.getTime();
        return dateFormat.format(dateTomorrow).toString();
    }
    /***
     * Return the dateYestarDay in reworked String
     *
     * @return a date yesterday as a reworked string
     */
    public String dateYesterDay(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, -24);
        Date dateYesterDay = calendar.getTime();
        return dateFormat.format(dateYesterDay).toString();
    }
    /***
     * Return the dateToday in reworked String
     *
     * @return date today as a reworked string for
     *          readability
     */
    public String dateToday(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        calendar.setTime(date);
        return dateFormat.format(date).toString();
    }

    /**
     *
     * This is used in api-reader to check if
     * api information for twelve before is gathered from
     * the xml or not.
     *
     * @return if its twelveBefore as a boolean return true
     *          if its twelve before
     *
     * */
    public Boolean getTwelveBefore() {
        return isTwelveBefore;
    }

    /**
     * Set if its twelveBefore
     *
     * @param twelveBefore as boolean
     * */
    public void setTwelveBefore(Boolean twelveBefore) {
        isTwelveBefore = twelveBefore;
    }

    /**
     * This is used in api-reader to check if
     * api information for twelve after is gathered from
     * the xml or not.
     *
     * @return if its twelveAfter as a boolean return true if
     *          it is twelve after
     * */
    public Boolean getTwelveAfter() {
        return isTwelveAfter;
    }

    /**
     * Set if its twelveAfter
     *
     * @param twelveAfter new value for twelve after
     * */
    public void setTwelveAfter(Boolean twelveAfter) {
        isTwelveAfter = twelveAfter;
    }

}
