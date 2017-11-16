import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Rasmus on 18/04/17.
 *
 * Class which create a JMenu
 * with channels from the xml.
 *
 * @author Rasmus Dahlkvist
 */
public class Menu extends JMenu {

    private JMenuItem filter, myFavorites;
    private ArrayList<JMenuItem> listOfChannels = new ArrayList<>();
    private JMenuBar startMenu = new JMenuBar();
    private JFrame frame;
    private RadioChartGui gui;
    private RadioHandler radioHandler;
    private ScheduleTable table;
    private String currenChannel = "";

    /**
     * Constructor for Menu
     *
     * @param gui of the application
     * @param frame given from the gui
     * @param radioHandler which store the information from the
     *                     api
     * @param table generated in the the gui
     * */
    public Menu(RadioChartGui gui, JFrame frame,
                RadioHandler radioHandler, ScheduleTable table){
        super("Channels");
        this.gui = gui;
        this.frame = frame;
        this.radioHandler = radioHandler;
        this.table = table;
        this.currenChannel = "P1";
    }
    /**
     * Initialize tha menu and
     * create actionlistener for the
     * under menus.
     * Loop each inside the object list and
     * get all the channel name. Apply each channel with listener
     */
    public void startMenu() {
        frame.setJMenuBar(startMenu);
        startMenu.add(this);
        for(int i = 0; i < radioHandler.objects().size(); i++){
            if(radioHandler.objects().get(i) instanceof  Channel){
                listOfChannels.add(
                        this.channelItem(
                                ((Channel)(radioHandler.objects().
                                        get(i))).getChannelName()
                        ));
                listOfChannels.get(i).setActionCommand(
                        ((Channel)radioHandler.objects().get(i)).
                                getChannelName()
                );
                listOfChannels.get(i).addActionListener(
                        new JMenuActionListener(table,radioHandler)
                );
                this.add(listOfChannels.get(i));

            }
        }

    }
    /**
     * Create one new JMenuItem
     * with a name given and color white.
     *
     * @param name of the JMenu
     * */
    public JMenuItem channelItem(String name) {
        JMenuItem item = new JMenuItem();
        item.setBackground(Color.white);
        item.setText(name);
        return item;
    }


}
