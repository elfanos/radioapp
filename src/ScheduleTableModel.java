import javax.swing.table.AbstractTableModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Rasmus on 16/04/17.
 * The ScheduleTable which use an abstractTableModel
 * that show all the channels schedule in a JTable
 *
 * @author Rasmus Dahlkvist
 */
public class ScheduleTableModel extends AbstractTableModel{
    private static final long serialVersionUID = 1L;
    private String[] columns;
    private List<?> radioObjects;
    String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);
    Calendar calendar = Calendar.getInstance();

    /**
     * Constructor for the class ScheduleTableModel
     * it takes on list of radioOjbects, which is used
     * to fill the table with data.
     *
     * @param radioObjects radioObject contains the list of information
     *                     from the xml
     * */
    public ScheduleTableModel(List<?> radioObjects){
        this.radioObjects = radioObjects;
        System.out.println(radioObjects.size());
        columns = new String[]{
                "Title",
                "Start/endtime",
                "Episode",
                "Sändningsstatus"
        };
    }
    /**
     * Update the current list inside the table
     *
     * @param listOfObjects is a list of Object that is sett to
     *                      radio objects
     * */
    public void updateList(List<?> listOfObjects){
        this.radioObjects = listOfObjects;
    }
    /**
     * Number of columns
     *
     * @return columns.length of the JTable
     */
    public int getColumnCount() {
        return columns.length;
    }

    /**
     * Number of rows
     *
     * @return size of radioObjects
     */
    public int getRowCount() {
        return this.radioObjects.size();
    }

    /**
     * Get column name
     *
     * @param col the column index
     *
     * @return return a specific on that position
     */
    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    /**
     * Get column class needed to set row values
     * at the different columns. Declare each column with
     * the class that the row will gather values from.
     *
     * @param columnIndex the column index for the table
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Class<?> aClass = Schedule.class;
        if (radioObjects.isEmpty()) {
            return Object.class;

        }
        switch (columnIndex){
            case 0:
                aClass = Schedule.class;
                break;
            case 1:
                aClass = Schedule.class;
                break;
            case 2:
                aClass = Schedule.class;
                break;
            case 3:
                aClass = Schedule.class;
        }
        return aClass;
    }

    /**
     * Send the values inside the table to
     * the view. Each value gathered from the
     * schedule object.
     *
     * @param row of row index in the table
     * @param col of col index in the table
     * */
    public Object getValueAt(int row, int col) {
        Object listObject = radioObjects.get(row);
        switch (col) {
            case 0:
                return ((Schedule) listObject).getTitle();
            case 1:
                String startTime = this.convertDateToTime(
                        ((Schedule) listObject).getStartTime()
                );
                String endTime = this.convertDateToTime(
                        ((Schedule) listObject).getEndTime()
                );
                if(startTime.equals("n") && endTime.equals("n")){
                    return "Ingen tid angiven";
                }else {
                    return " " + startTime + " till " + endTime;
                }
            case 2:
                return ((Schedule) listObject).getEpisode();

            case 3:
                return this.checkBroadCastStatus(
                        ((Schedule) listObject).getStartTime(),
                        ((Schedule) listObject).getEndTime()
                );
            default:
                return null;
        }
    }

    /**
     * Set all value at a specific row and column
     * inside the table.
     *
     * @param value a value of the object
     * @param row the row index in the table
     * @param col the column index in the table
     */
    public void setValueAt(Object value, int row, int col) {
        if(value == null){
        }else {
            switch (col){
                case 0:
                    if(value instanceof Schedule){
                        ((Schedule)radioObjects.get(row)).
                                setTitle(((Schedule) value).
                                        getTitle());
                    }
                    break;
                case 1:
                    if(value instanceof Schedule){

                        ((Schedule)radioObjects.get(row)).
                                setStartTime(((Schedule) value).
                                        getStartTime());
                        ((Schedule)radioObjects.get(row)).
                                setEndTime(((Schedule) value).
                                        getEndTime());
                    }
                    break;
                case 2:
                    if(value instanceof Schedule){
                        ((Schedule)radioObjects.get(row)).setEpisode(
                                ((Schedule) value).getEpisode()
                        );
                    }
                    break;
                case 3:
                    if(value instanceof Schedule){
                        ((Schedule)radioObjects.get(row)).
                                setStartTime(((Schedule) value).
                                        getStartTime());
                        ((Schedule)radioObjects.get(row)).
                                setEndTime(((Schedule) value).
                                        getEndTime());
                    }
                    break;
                default:
                    break;
            }
            fireTableCellUpdated(row, col);

        }

    }



    /**
     * Convert date to time to be more
     * readable for the user
     *
     * @param date the date as a string,
     *
     * */
    public String convertDateToTime(String date){
        Date test = null;
        if(date !=null) {
            try {
                test = sdf.parse(date);
            } catch (ParseException e) {
                System.out.println("Parse not working: " + e);
            }
            calendar.setTime(test);
            return String.format("%02d:%02d",
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE)
            );
        }
        return "n";
    }

    /**
     * Check the status of each broadcast, with start time
     * and end time as params. The method checks by comparing
     * current date with the start time as well as end time.
     * And return a string value based on how the compare went.
     *
     * @param start the start time given by the xml
     * @param end end time given by the xml
     *
     * @return a string with the value based on which state
     *          the programs is in.
     * */
    public String checkBroadCastStatus(String start, String end){
        String stateString = " ";
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(),
                ZoneId.systemDefault());
        Date startDate = null;
        Date endDate = null;

        Date localDate  = Date.from(ldt.atZone(ZoneId.systemDefault()).
                toInstant());
        if(start != null && end !=null) {
            try {
                startDate = sdf.parse(start);
                endDate = sdf.parse(end);
            } catch (ParseException e) {
                System.out.println("Parse not working " + e);
            }
            switch (startDate.compareTo(localDate)) {
                case -1:
                    switch (endDate.compareTo(localDate)) {
                        case -1:
                            stateString = "Sändningen har upphört";
                            break;
                        case 0:
                            stateString = "Sändningen pågår";
                            break;
                        case 1:
                            stateString = "Sändningen pågår";
                            break;
                    }
                    break;
                case 0:
                    stateString = "Sändning pågår";
                    break;
                case 1:
                    stateString = "Sändningen börjar snart";
                    break;
            }
        }
        return stateString;
    }

}
