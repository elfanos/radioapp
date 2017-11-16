
/**
 * Created by Rasmus on 25/01/17.
 *
 * Class which handling the gathering of data
 * from the xml-file to the models into the list.
 *
 * @author Rasmus Dahlkvist
 */
public class ApiHandler {
    private ApiCall apiCall;

    public ApiHandler(ApiCall apiCall){
        this.apiCall = apiCall;
    }

    /**
     * Methods in model needs to be the
     * same name as the nodes. Check which xml
     * that is being read by the saxparser. To get right
     * right node directions inside setValues.
     *
     * @param element element
     * @param element given to add to the model channel
     * @param currentValue that the element should be added to
     * @param currentNode which node that information is
     *                    gathered from
     * @param aClass which class that the element should be added to.
     * @param channelName of the channel
     * */
    public void insertValueInApiModel(String element, Object currentValue,
                                      String currentNode,
                                      Class<?> aClass, String channelName){
        if(aClass.getName() == "Channel"){

            this.setValuesForChannel(element,currentValue,
                    currentNode, channelName);

        }else if(aClass.getName() == "Schedule"){

            this.setValuesForSchedule(element,currentValue,
                    currentNode);
        }
    }
    /**
     * Insert values in the models for the given
     * api. Must be based on the what values in the
     * xml that wants to be retrived.
     *
     * @param element given to add to the model channel
     * @param currentValue that the element should be added to
     * @param currentNode which node that information is
     *                    gathered from
     * @param channelName of the channel
     * */
    public void setValuesForChannel(String element, Object currentValue,
                                    String currentNode, String channelName){
        if(channelName != null){
            if(currentValue instanceof Channel){
                ((Channel) currentValue).setChannelName(channelName);
            }
        }
        if(currentNode.equals("image")){
            if(currentValue instanceof  Channel){
                 ((Channel) currentValue).setImage(element);
                 /*System.out.println(
                         ((Channel) currentValue).getImage()
                 );*/
            }
        }else if(currentNode.equals("scheduleurl")){
            if(currentValue instanceof  Channel){
                ((Channel) currentValue).setScheduleList(element);
            }
        }else if(currentNode.equals("channeltype")){
            if(currentValue instanceof  Channel){
                ((Channel) currentValue).setChannelType(element);
            }
        }
    }
    /**
     * Method which collect data based on certain restriction from the
     * xml-file. The data is added to an Object which refers to a schedule
     * object
     *
     * @param element given to add to the model channel
     * @param currentValue that the element should be added to
     * @param currentNode which node that information is
     *                    gathered from
     * */
    public void setValuesForSchedule(String element, Object currentValue,
                                     String currentNode){
        if (currentNode.equals("starttimeutc")) {
            apiCall.dateEndTime(element);
        }
        if(apiCall.getTwelveBefore()) {
            if(apiCall.getTwelveAfter()) {
                if (currentNode.equals("description")) {
                    if (currentValue instanceof Schedule) {
                        ((Schedule) currentValue).setDescription(element);
                    }

                } else if (currentNode.equals("starttimeutc")) {
                    //System.out.println("Schedule starttime");
                    if (currentValue instanceof Schedule) {
                        ((Schedule) currentValue).setStartTime(element);
                    }
                } else if (currentNode.equals("endtimeutc")) {
                    if (currentValue instanceof Schedule) {
                        ((Schedule) currentValue).setEndTime(element);
                    }
                } else if (currentNode.equals("episodeid")) {
                    if (currentValue instanceof Schedule) {
                        ((Schedule) currentValue).setEpisode(element);
                    }
                } else if (currentNode.equals("imageurl")) {
                    if (currentValue instanceof Schedule) {
                        ((Schedule) currentValue).setImage(element);
                    }
                } else if (currentNode.equals("title")) {
                    if (currentValue instanceof Schedule) {
                        ((Schedule) currentValue).setTitle(element);
                    }
                }
            }
        }

    }

}
