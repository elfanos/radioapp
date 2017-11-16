import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;


/**
 * Created by Rasmus on 30/01/17.
 *
 * The gui implements all the components
 * for the gui. And add listener to the buttons.
 *
 * @author Rasmus Dahlkvist
 */
public class RadioChartGui {
    private JFrame frame;
    private ScheduleTable table;
    private JButton refreshButton;
    private RadioHandler radioObjects;
    private JDialog popup;
    private boolean firstElement;
    private Menu menu;
    private ErrorDialog dlg = null;
    private JPanel middlePanel;
    private JPanel middlePanelNoTable;
    private JPanel middlePanelLoading;
    private JPanel jtablePanel;
    private JPanel jLowerPanel;
    private boolean isTableVisible;
    private JScrollPane tableScroller;
    private boolean firstLoad = true;

    /**
     * Constructor for the radioChartGui
     * retrieves one radiohandler which contains
     * all data that is in use in the application.
     *
     * @param title of the program
     * @param radioObjects contains necessary for the view
     * */
    public RadioChartGui(String title, RadioHandler radioObjects){
        this.radioObjects = radioObjects;
        this.radioObjects.setGui(this);
        setFirstElement(true);
        jLowerPanel  = lowerPanel();
        this.addRefreshButton();
        refreshButton.setEnabled(false);
        middlePanel = buildMiddlePanel();
        middlePanelNoTable =
                this.createMiddlePanelNoTable("noschedule");
        middlePanelLoading = this.createMiddlePanelNoTable("loading");
        dlg = new ErrorDialog(frame, "ERROR",
                "No internet connection cant update schedule");
        frame = new JFrame(title);

        table = new ScheduleTable(this.radioObjects, this);
        table.setRowHeight(100);

        table.setDefaultRenderer(BufferedImage.class,
                new BufferedImageCellRenderer());
        table.getSelectionModel().
               addListSelectionListener(new JTableClickListener());
        table.setDefaultRenderer(Schedule.class,
                new ScheduleTimeBackgroundRender());

        tableScroller = new JScrollPane(table);
        middlePanel.add(tableScroller);
       // middlePanel.add(middlePanelNoTable,BorderLayout.CENTER);
        frame.add(middlePanel,BorderLayout.CENTER);
        frame.add(jLowerPanel, BorderLayout.SOUTH);
        frame.setBackground(Color.decode("#2D3142"));
        frame.pack();
        frame.setSize(600, 600);
        this.tableHide(0);
        (new SWTableUpdate()).execute();

    }
    /**
     * Show gui
     * */
    public void show() {
        frame.setVisible(true);
    }

    /**
     * Show popup
     * */
    public void popupShow(){
        popup.setVisible(true);
    }

    /**
     * Structure the lowerpanel with
     * a new flowlayout. And create a new
     * JPanel
     *
     * @return JPanel with data
     * */
    private JPanel lowerPanel(){
        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        return lowerPanel;
    }

    /**
     * Add the refresh button to
     * the lowerpanel in the gui
     * Refresh update the radioOjbect on click.
     * */
    private void addRefreshButton(){
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                (new SWTableUpdate()).execute();
                refreshButton.setEnabled(false);
            }
        });
        jLowerPanel.add(refreshButton);
    }

    /**
     * @return middlePanel with new layout
     * */
    private JPanel buildMiddlePanel() {

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BorderLayout());
        jtablePanel = new JPanel();
        jtablePanel.add(new JScrollPane(table));
      //  middlePanel.add(jtablePanel);

        return middlePanel;
    }

    /**
     * JDialog used as popup window when
     * user click on one item in the table.
     * It will show the picture, description and
     * title of the program clicked by the user.
     *
     * @param description in the popup panel
     * @param title title in th popup panel
     * @param image background of the popup panel
     * */
    public JDialog listSelectionPopup(String description,
                                      String title,URL image){
        JDialog popup = new JDialog();
        JPanel infoSide = new JPanel();
        PicturePanel pictureSide = new PicturePanel(image);

        JTextArea textDescription = new JTextArea();
        JLabel textTitle = new JLabel(title,SwingConstants.CENTER);
        textTitle.setFont(new Font("Serif", Font.BOLD, 20));
        textDescription.setMargin(new Insets(5,5,5,5));
        textDescription.setText(description);
        textDescription.setLayout(new FlowLayout());

        textDescription.setLineWrap(true);
        textDescription.setEditable(false);
        infoSide.add(textTitle);
        infoSide.add(textDescription);

        pictureSide.setLayout(
                new FlowLayout(FlowLayout.CENTER, 0, 8));
        pictureSide.setBorder(
                new EmptyBorder(10,40,10,40));

        JPanel test = new JPanel();
        test.add(textTitle);
        test.add(textDescription);
        test.add(pictureSide);
        test.setLayout(new GridLayout(3,0,10,10));
        test.setBackground(Color.decode("#FFFFFF"));
        popup.add(test, BorderLayout.CENTER);
        popup.setLayout(new GridLayout(1,1));
        popup.setSize(200, 400);
        popup.setBackground(Color.decode("#FFFFFF"));
        popup.addWindowListener(closeListener);
        return popup;
    }

    /**
     * Listener for the popup window, it will
     * set first element false when it's closed.
     * To avoid duplicate elements in the popup.
     * */
    private WindowListener closeListener = new WindowListener() {

        /**
         * Calls when window opened.
         *
         * @param e event of the window
         * */
        @Override
        public void windowOpened(WindowEvent e) {

        }

        /**
         * Calls when window is closing
         *
         * @param e event of the window
         * */
        @Override
        public void windowClosing(WindowEvent e) {
            setFirstElement(true);
        }

        /**
         * Calls when window is closed
         *
         * @param e event of the window
         * */
        @Override
        public void windowClosed(WindowEvent e) {
            System.out.println("??");
        }

        /**
         * Calls when window is conified
         *
         * @param e event of the window
         * */
        @Override
        public void windowIconified(WindowEvent e) {

        }

        /**
         * Calls when window is deiconified
         *
         * @param e event of the window
         * */
        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        /**
         * Calls when window is activated
         *
         * @param e event of the window
         * */
        @Override
        public void windowActivated(WindowEvent e) {

        }

        /**
         * Calls when window is deactivated
         *
         * @param e event of the window
         * */
        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    };

    /**
     * Panel which contains a image
     * as background.
     *
     * @author Rasmus Dahlkvist
     * */
    class PicturePanel extends JPanel {
        Image image;

        /**
         * Constructor
         * Takes on imageUrl and
         *
         * @param imageURL of the image from internet
         * */
        public PicturePanel(URL imageURL) {
            try
            {
                image = javax.imageio.ImageIO.read(imageURL);
            }
            catch (Exception e) { }
        }

        /**
         * Paint the JPanel with the new
         * pictures as a background.
         *
         * @param g the graphics to draw an image
         * */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null)
                g.drawImage(image, 0,0,this.getWidth(),
                        this.getHeight(),this);
        }
    }


    /**
     * Class which generate a bufferedImage for
     * the gui.
     *
     * @author Rasmus Dahlkvist
     * */
    public class BufferedImageCellRenderer extends DefaultTableCellRenderer {

        /**
         * Render a cell with an icon as background.
         *
         * @param table which the cell is to be rendered
         * @param value the image object
         * @param isSelected if cell is selected
         * @param hasFocus if cell has focus
         * @param row the index
         * @param column which column index
         * */
        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {
            super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);
            if (value instanceof BufferedImage) {
                setIcon(new ImageIcon((BufferedImage)value));
                setText(null);
            } else {
                setText("Bad image");
            }
            return this;
        }
    }
    /**
     * To keep track on if more information
     * about a program is viewed by the user
     * to avoid duplicates popups
     *
     * @return if it is the firstElement
     * */
    public boolean isFirstElement() {
        return firstElement;
    }

    /**
     * Set the firstElement
     *
     * @param firstElement the new boolean value of the firstElement
     *
     * */
    public void setFirstElement(boolean firstElement) {
        this.firstElement = firstElement;
    }

    /**
     * Update the table with new values
     * from the list
     *
     * @param currentChannel string on which channel that
     *                       the user current has selected
     * */
    public void updateTable(String currentChannel){

        if(radioObjects.objects().size() != 0) {
            if(radioObjects.isTableInitalized()) {
                for (int i = 0; i < radioObjects.objects().size(); i++) {
                    if (radioObjects.objects().get(i) instanceof Channel) {
                        if (((Channel) radioObjects.objects().get(i)).
                                getChannelName().
                                contains(currentChannel)) {
                            this.updateSchedules((
                                    (Channel) radioObjects.objects().
                                    get(i)).getScheduleList());
                        }
                    }
                }
            }else{
                table.setModelForTable();
            }
        }else{
            this.throwErrorToUser();
        }
    }
    /**
     * Method for updating the schedule table with
     * the new list, retrieved from the api.
     *
     * @param schedule a list that contains schedule information
     *                 from the api
     * */
    public void updateSchedules(List<?> schedule){
        if(schedule != null) {
            ((ScheduleTableModel) table.getModel()).
                    updateList(schedule);
            for (int j = 0; j < schedule.size(); j++) {
                if (schedule.get(j) instanceof Schedule) {
                    for (int column = 0; column < 4; column++) {
                        table.setValueAt(schedule.get(j), j, column);
                    }
                }
            }

        }else{

        }
    }

    /**
     * Initialize menus that show all the
     * channels available in the radio application
     * Using a darylls menu scroller api though a lot
     * of channels is gathered from the radio api.
     * */
    private void initializeMenu(){
        menu = new Menu(this, frame, radioObjects, table);
        menu.startMenu();
        //Set a scroller for the menu since its a lot of channels
        //The solution is crafted by daryll and I used his
        //api for fixing my scroller.
        MenuScroller.setScrollerFor(menu,8,125,3,1);
    }
    /**
     * Cast a error dialog as an
     * java JDialog.
     * */
    public void throwErrorToUser(){
        if(!dlg.isItVisible()) {
            dlg.setDialog();
            dlg.setIsVisible(true);
        }else{
            dlg.hideDialog();
            dlg.setDialog();
        }
    }

    /**
     * Remove a component from the middlePanel
     * Then add another.
     * In this case it remove the table component
     * and add a new component with a text that
     * says No schedule found or Downloading schedules
     * and channels
     *
     * @param hideCase which panel that is going to
     *                 be shown instead of the JScrollpane
     *
     * */
    public void tableHide(int hideCase){
        if(tableScroller != null) {
            middlePanel.remove(tableScroller);
        }
        if(hideCase == 0){
            middlePanel.add(middlePanelLoading);
        }else{
            middlePanel.add(middlePanelNoTable);
        }
        this.setIsTableVisible(false);
        middlePanel.revalidate();
        middlePanel.repaint();
    }

    /**
     * Remove a component from the middlePanel
     * Then add another.
     * In this case it remove the component which
     * says No schedule found or Downloading schedules and channels
     * with a a JScrollpane.
     *
     * */
    public void tableShow(int hideCase){
        if(hideCase == 0){
            middlePanel.remove(middlePanelLoading);
            this.initializeMenu();
            firstLoad = false;
        }else{
            middlePanel.remove(middlePanelNoTable);
        }
        middlePanel.add(tableScroller);
        this.setIsTableVisible(true);
        middlePanel.revalidate();
        middlePanel.repaint();
    }
    /**
     * Setter for if the table is visible
     *
     * @param value if the table is visible or not
     * */
    public void setIsTableVisible(boolean value){
        this.isTableVisible = value;
    }

    /**
     * Getter for if the table is visible or not
     * this is used to not call tableShow function every time
     * a new channel is selected.
     *
     * @return  isTableVisible as boolean
     *
     * */
    public boolean getIsTableVisible(){
        return this.isTableVisible;
    }

    /**
     * Create a panel for the text "No schedule found" which
     * replace the table panel with this when a
     * channel doesnt have a schedule.
     *
     * @return panel with components to show the text No Schedule Found
     *         a label and some design on the text, as well as a white
     *         background
     * */
    public JPanel createMiddlePanelNoTable(String tableCase){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.white);
        JLabel label = null;
        if(tableCase == "noschedule") {
            label = new JLabel("No schedule found");
        }else if(tableCase == "loading"){
            label = new JLabel("Downloading schedules and channels");
        }else{
            label = new JLabel(" ");
        }
        label.setFont(new Font("Boulder", Font.PLAIN, 20));
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label);
        return panel;
    }
    /**
     * Class which change color on the table cell based on
     * the current state of each program/cell.
     *
     * @author Rasmus Dahlkvist
     * */
    public class ScheduleTimeBackgroundRender extends DefaultTableCellRenderer {

        /**
         * Painting the table cell with new a color
         * that get the date of each item in the table
         * and check which of the three state the
         * program is in.
         *
         * @return l a cell component with background color
         * */
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int col){

            Component l = super.getTableCellRendererComponent(table,
                    value, isSelected, hasFocus, row, col);
            if(table != null) {
                switch ((table.getModel().
                        getValueAt(row, 3)).toString()) {
                    case "Sändningen har upphört":
                        l.setBackground(Color.decode("#E1D89F"));
                        break;
                    case "Sändningen pågår":
                        l.setBackground(Color.decode("#26A96C"));
                        break;
                    case "Sändningen börjar snart":
                        l.setBackground(Color.decode("#376996"));
                        break;
                }
            }
            l.setForeground(Color.WHITE);
            return l;
        }
    }
    /**
     * SwingWorker to avoid
     * interruption in EDT thread
     * Update the table and repaint with new
     * values.
     * */
    class SWTableUpdate extends SwingWorker<List<?>, Object> {

        /**
         * Read an xmlfile from the apie in a swingworker thread
         *
         * @return radioObject.updateObjects a update list.
         */
        @Override
        protected List<?> doInBackground() {
            return radioObjects.updateObjects();
        }

        /**
         * When doInBackground is done, done updates
         * the table with the new values.
         * Show the table with the new values
         * retrieved from the radio API.
         * Set the refreshbutton to enable.
         */
        @Override
        protected void done() {
            try {
                radioObjects.setObjects(get());
                updateTable(radioObjects.getCurrentChannel());
                if(firstLoad) {
                    radioObjects.getGui().tableShow(0);
                }
                refreshButton.setEnabled(true);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     *
     * Class that is a listener for when user
     * click on a row item.
     *
     * @author Rasmus Dahlkvist
     * */
    public class JTableClickListener implements ListSelectionListener{



        /**
         * Check which element was clicked
         * and find the element inside the list and
         * show relevant information for the user
         *
         * @param e event on list selection for getting the
         *          row which the user press
         * */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(e.getValueIsAdjusting()) {
                for(int i = 0; i < radioObjects.objects().size(); i++){
                    if(radioObjects.objects().get(i) instanceof Channel){
                        List<?> schedule = ((Channel) radioObjects.objects().
                                get(i)).getScheduleList();
                        if(schedule != null) {
                            for (int j = 0; j < schedule.size(); j++) {
                                if (schedule.get(j) instanceof Schedule) {
                                    if (((Schedule) schedule.get(j)).
                                            getTitle() != null) {
                                        this.getInfoFromProgram(j, schedule);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        /**
         * Get the item from the list
         * and show relevant information
         *
         * @param j to get the right programs schedule
         *
         * @param schedule a list of object that contains
         *                 information about schedule on each
         *                 program from the xml
         * */
        private void getInfoFromProgram(int j, List<?> schedule){
            if (((Schedule) schedule.get(j)).getTitle().
                    contains(table.getValueAt(table.
                            getSelectedRow(), 0).toString()
            ) && isFirstElement()) {
                popup = listSelectionPopup(
                        ((Schedule) schedule.get(j)).getDescription(),
                        ((Schedule) schedule.get(j)).getTitle(),
                        ((Schedule) schedule.get(j)).getImage()
                );

                setFirstElement(false);  /* to avoid duplicates */
                popupShow();
            }
        }
    }
    /***
     * Dialog that appear if user has
     * no internet connection and tell them
     * that the schedule cant be retrieved
     *
     * @author Rasmus Dahlkvist
     */
    public class ErrorDialog extends JDialog implements ActionListener {
        private String message;
        private JFrame parent;
        private boolean visible;

        /**
         * Constructor
         * Take one parent JFrame and title
         * with message
         *
         * @param parent a JFrame declared as parent for the error
         *               dialog
         * @param title gives the title of the component
         * @param message the message inside the component
         * */
        public ErrorDialog(JFrame parent,
                           String title, String message) {
            super(parent, title, true);
            this.message = message;
            this.parent = parent;
            visible = false;
        }

        /**
         * Initialize a new JDialog with
         * the text given when a new instance of
         * the class where initialized
         * */
        public void setDialog(){
            if (parent != null) {
                Dimension parentSize = parent.getSize();
                Point p = parent.getLocation();
                setLocation(p.x + parentSize.width / 4,
                        p.y + parentSize.height / 4);
            }
            JPanel messagePane = new JPanel();
            messagePane.add(new JLabel(message));
            getContentPane().add(messagePane);
            JPanel buttonPane = new JPanel();
            JButton button = new JButton("OK");
            buttonPane.add(button);
            button.addActionListener(this);
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            pack();
            setVisible(true);
        }
        /**
         * @return visible if dialog is visible boolean
         *          declare state of dialog
         * */
        public boolean isItVisible(){
            return visible;
        }

        /**
         * Set if dialog is visible
         *
         * @param value boolean which declare state of dialog
         * */
        public void setIsVisible(boolean value){
            visible = value;
        }
        /**
         * Hide current dialog
         * */
        public void hideDialog(){
            setVisible(false);
            this.setIsVisible(false);
            dispose();
        }

        /**
         * Listener for the ok button
         * inside the JDialog
         * Hides it if ok is pressed
         *
         * @param e event check when user click ok button
         * */
        public void actionPerformed(ActionEvent e) {
           this.hideDialog();
        }
    }

}

