import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Rasmus on 30/01/17.
 *
 * Class RadioHandler keeps track of the
 * List which gather data from the xml-reader
 * it update the list every 60 minutes.
 *
 * @author Rasmus Dahlkvist
 */
public final class  RadioHandler implements Runnable{
    private List<?> objects;
    private Thread thread;
    private Boolean running;
    private String currentChannel;
    private RadioChartGui gui;
    private boolean isTableInitialized;

    /**
     * Constructor for the class RadioHandler
     * Takes on list of objects and retrieve it's
     * data for futhermore use in the application
     *
     * @param objects a list of objects
     * */
    public RadioHandler(List<?> objects){
        thread = null;
        running = false;
        this.objects = objects;
        this.setCurrentChannel("P1");
    }

    /**
     * Runs the thread
     * wait 60 minutes before the
     * list of objects updates.
     * */
    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(60 * 60 * 1000);
               // this.setObjects(this.updateObjects());
                new InterValUpdater(this).execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Update list of objects,
     * retrieve data from the api, in xml format
     * then return the update data.
     *
     * @return List<?> of objects from an xml reader
     * */
    public synchronized List<?> updateObjects(){
        String apiUrl = "http://api.sr.se/api/v2/channels/?pagination=false";
        ApiCall apiCall = new ApiCall(new Channel());
        apiCall.setApiUrl(apiUrl);
        apiCall.setSaxDirections("channel");
        apiCall.setModelName("Channel");
        apiCall.setStoredXml("channel.xml");
        ApiReader reader = new ApiReader(apiCall);
        return reader.readXml();
    }
    /**
     * Get the current list of objects
     * */
    public List<?> objects(){ return this.objects;}

    /**
     * Set a new list of objects
     * remove all data in the existing one.
     *
     * @param listOfObjects which is a list that contains
     *                      objects
     * */
    public synchronized void setObjects(List<?> listOfObjects){
        this.objects = null;
        this.objects = listOfObjects;
    }

    /**
     * @return the currentChannel
     * */
    public String getCurrentChannel() {
        return currentChannel;
    }

    /**
     * Set the currentChannel
     *
     * @param currentChannel as a string
     * */
    public void setCurrentChannel(String currentChannel) {
        this.currentChannel = currentChannel;
    }

    /**
     * @return the gui
     * */
    public RadioChartGui getGui() {
        return gui;
    }

    /**
     * Set the current gui
     *
     * @param gui of the application
     * */
    public void setGui(RadioChartGui gui) {
        this.gui = gui;
    }

    /***
     * @return if table is initialized
     */
    public boolean isTableInitalized() {
        return isTableInitialized;
    }

    /**
     * Set the state of initialized table
     *
     * @param tableInitialized if table is setted
     * */
    public void setTableInitalized(boolean tableInitialized) {
        isTableInitialized = tableInitialized;
    }

    /**
     * Class that use a SwingWorker as background thread
     * to not interrupt the EDT thread while updating
     * values for the table.
     * */
    public final class InterValUpdater extends SwingWorker<List<?>, Object> {
        private RadioHandler radioObjects;

        /**
         * Constructor for IntervalUpdater
         * Take one radioObject for updating the list
         *
         * @param radioObjects a radioHandler object
         */
        public InterValUpdater(RadioHandler radioObjects){
            this.radioObjects = radioObjects;
        }
        /**
         * Read an xmlfile from the apie in a swingworker thread
         *
         * @return radioObject.updateObjects which update the
         *          radioHandler list in background
         */
        @Override
        protected List<?> doInBackground() {
            return radioObjects.updateObjects();
        }

        /**
         * When doInBackground is done, done updates
         * the table with the new values if they not
         * already exist
         */
        @Override
        protected void done() {
            try {
                radioObjects.setObjects(get());
                radioObjects.getGui().
                        updateTable(radioObjects.getCurrentChannel());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }




}
