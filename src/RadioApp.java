import javax.swing.*;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rasmus on 22/12/16.
 *
 * Main class starts the gui and
 * the xml reader.
 *
 * @author Rasmus Dahlkvist
 */
public class RadioApp {

    public static void main(String[] args) {
        String apiUrl = "http://api.sr.se/api/v2/channels/?pagination=false";
        ApiCall apiCall = new ApiCall(new Channel());
        apiCall.setApiUrl(apiUrl);
        apiCall.setSaxDirections("channel");
        apiCall.setModelName("Channel");

        //for testing
        //apiCall.setStoredXml("channel.xml");
        ApiReader reader = new ApiReader(apiCall);
        List<?> chart = reader.readXml();

        final RadioHandler radioHandler = new RadioHandler(chart);
        radioHandler.start();
        radioHandler.updateObjects();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RadioChartGui gui =
                        new RadioChartGui("SR schedules", radioHandler);
                gui.show();
            }
        });
    }
}
