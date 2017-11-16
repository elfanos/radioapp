import java.net.MalformedURLException;
import java.net.URL;
/**
 * Created by Rasmus on 24/01/17.
 * Basic model class that holds
 * data retrived from the xml-file
 *
 * @author Rasmus Dahlkvist
 */
public class Schedule {

    private String description;
    private String title;
    private String episode;
    private String startTime;
    private String endTime;
    private URL image;

    /**
     * @return description of the program
     * */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description
     *
     * @param description of program
     * */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return title of the program
     * */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the program
     *
     * @param title of the program
     * */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return episode which episode of the program
     * */
    public String getEpisode() {
        return episode;
    }

    /**
     * Set the episode
     *
     * @param episode of the program
     * */
    public void setEpisode(String episode) {
        this.episode = episode;
    }

    /**
     * @return startTime of the program
     * */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Set the startTime for the program
     *
     * @param startTime of the program
     * */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return endTime the endTime of the program
     * */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Set the endTime
     *
     * @param endTime of the programs
     * */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * @return image the url to the image
     * */
    public URL getImage() {
        return image;
    }

    /**
     * Set the image url
     *
     * @param image of the program
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
