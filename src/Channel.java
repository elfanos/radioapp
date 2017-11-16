import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.awt.SystemColor.text;

/**
 * Created by Rasmus on 23/01/17.
 * Basic model class that holds
 * data retrived from the xml-file
 *
 * @author Rasmus Dahlkvist
 */
public class Channel {
    private List<?> scheduleList;
    private URL image;
    private String channelType;
    private String channelName;

    /**
     * @return the channelName the channel name as a string
     * */
    public String getChannelName() {
        return channelName;
    }

    /**
     * Set the channelName
     *
     * @param channelName name of the channel in string
     * */
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    /**
     * Set the channel type
     *
     * @param channelType the type of channel in string
     * */
    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    /**
     * @return the schedule list as a list
     * */
    public List<?> getScheduleList() {
        return scheduleList;
    }

    /**
     * Collect data from a link to another xml-file
     * which contains the schedule for the channel.
     * The data is stored inside a list
     *
     * @param xmlURL normal url in string format
     * */
    public void setScheduleList(String xmlURL) {
        /*using local xml*/
        String xmlString = xmlURL;
        String xmlReworked = xmlString.replaceAll("(?m)^[ \t]*\r?\n", "");

        /*API xml*/
        String sUrl = xmlURL;
        ApiCall call = new ApiCall(new Schedule());
        call.setApiUrl(sUrl);
        call.setSaxDirections("scheduledepisode");
        call.setStoredXml(xmlReworked);
        ApiReader reader2 = new ApiReader(call);
        List<?> sh = reader2.readXml();
       this.scheduleList = sh;
    }
    /**
     * Set the image using the url of the image
     *
     * @param image given by a string from the xml
     *              gathered from the api
     * */
    public void setImage(String image) {
        try {
            this.image = new URL(image);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            System.out.println("Cannot find the url");
        }
    }

}
