import javax.swing.*;
import javax.swing.table.TableModel;

/**
 * Created by Rasmus on 16/04/17.
 *
 * ScheduleTable class which extends a JTable
 * this class creates a table for the gui
 * and use a moodified abstractablemodel as
 * an model
 *
 * @author Rasmus Dahlkvist
 */
public class ScheduleTable extends JTable {
    private RadioHandler radioHandler;
    private RadioChartGui gui;

    /**
     * Constructor for ScheduleTable
     * Basic jtable which use an abstract table model
     * for penitrating the data from the radioHandler.
     *
     * @param radioHandler a object containing the list of information
     *                      about programs
     * @param gui the gui of the program
     * */
    public ScheduleTable (RadioHandler radioHandler, RadioChartGui gui){
        this.gui = gui;
        this.radioHandler = radioHandler;
        if(radioHandler.objects() != null) {
            if (radioHandler.objects().size() != 0) {
                this.setModelForTable();
            } else {
                this.radioHandler.setTableInitalized(false);
                this.gui.throwErrorToUser();
            }
        }else{
            radioHandler.getGui().tableHide(0);
        }
    }
    /**
     * Initialize a model for the table and use
     * P1 as default channel
     * */
    public void setModelForTable(){
        if (radioHandler.objects().get(0) instanceof Channel) {
            this.setModel(
                    new ScheduleTableModel(
                            ((Channel) radioHandler.objects().get(0)).
                                    getScheduleList()
                    )
            );
            this.radioHandler.setTableInitalized(true);
        }
    }
}
