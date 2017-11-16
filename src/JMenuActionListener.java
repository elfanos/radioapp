import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by Rasmus on 18/04/17.
 *
 * Listener for JMenu for
 * knowing which menuitem is clicked
 * by the user.
 *
 * @author Rasmus Dahlkvist
 */
public class JMenuActionListener implements ActionListener {
    private RadioHandler radioObjects;
    private ScheduleTable table;

    /**
     * Constructor for JMenuActionListener
     * Collect one schedule table and radioHandler to
     * adjust the view based on what the user choose in the
     * channels menu
     *
     * @param scheduleTable generated in the gui
     * @param radioHandler keeps track of the information from
     *                     the api
     * */
    public JMenuActionListener(ScheduleTable scheduleTable,
                               RadioHandler radioHandler){
        this.radioObjects = radioHandler;
        this.table = scheduleTable;
    }

    /**
     * Listener for selection in the channel menu
     * if user choose a channel the schedule of that
     * channel will be shown in the table
     *
     * @param e event keeps track on what the
     *          user is doing when interacting
     *          with the JMenu
     * */
    @Override
    public void actionPerformed(ActionEvent e) {
        radioObjects.setCurrentChannel(e.getActionCommand());
        for(int i = 0; i < radioObjects.objects().size(); i++){
            if(radioObjects.objects().get(i) instanceof Channel){
                if(((Channel) radioObjects.objects().get(i)).
                        getChannelName().equals(e.getActionCommand())){
                    List<?> schedule = ((Channel) radioObjects.objects().
                            get(i)).getScheduleList();
                    if(schedule != null) {
                        ((ScheduleTableModel)
                                table.getModel()).updateList(schedule);
                        if(!radioObjects.getGui().getIsTableVisible()) {
                            radioObjects.getGui().tableShow();
                        }
                        for (int j = 0; j < schedule.size(); j++) {
                            if (schedule.get(j) instanceof Schedule) {
                                for (int column = 0; column < 4; column++) {
                                    table.setValueAt(
                                            schedule.get(j), j, column
                                    );
                                }
                            }
                        }
                    }else{
                        radioObjects.getGui().tableHide();
                    }
                }
            }
        }

    }
}
