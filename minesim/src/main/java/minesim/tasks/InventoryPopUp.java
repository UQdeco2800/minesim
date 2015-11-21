package minesim.tasks;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import minesim.entities.Inventory;
import minesim.entities.Peon;

/**
 * IMPORTANT NOTE: Although Swing is used here for GUI design, we aknowledge that JavaFX is the
 * accepted gui import and intend to use it in the coming future. The reason the coding from the
 * swing coding remains is that it acts as a reference for future coding and provides some of the
 * foundation for the code.
 *
 * This class takes a peon's inventory and represents it in a visual format. Initially this class
 * was directly responsible for conveying a Peon's inventory, but once critiques were taken in,
 * feedback suggested that instead of this UI element freely appearing in-game, it should be
 * contained to the context pane on the left side of the screen, underneath the buttons. For now,
 * this class exists to test the item and inventory classes as its an easy way to model the arrays
 * and items when testing.
 *
 * @author Jamesg
 **/

public class InventoryPopUp extends JFrame implements ActionListener {
    //3 static values to set the peon's mood to when testing the mood state
    //tracking
    final static int isAnnoyed = 30;
    final static int isOkay = 50;
    final static int isHappy = 70;
    //The container panel for all ui elements that sits inside content container
    JPanel inventoryWindow = new JPanel();
    //The button for removing items in the table, items are added in-code
    JButton removeItem;
    //A blank inventory
    Inventory inv;
    //The table containing inv
    InventoryTable table;
    // Columns set to 3 by default, rows can be infinite depending on item count
    int numberOfRows, numberOfColumns = 3;
    // Number of full inventory slots
    int inventorySlots;
    // Holds inventoryWindow inside a window
    Container content;
    //owner is created to create a peon and set its mood and assign inv to it
    Peon owner;

    /**
     * The constructor class for InventoryPopUp, which takes a Peon's inventory and its Peon as the
     * arguments. Its displayed in a GridBag format
     *
     * @param inventory -the inventory being shown
     * @param owner     -the inventory's owner
     */
    public InventoryPopUp(Inventory inventory, Peon owner) {
        this.inv = inventory;
        this.owner = owner;
        this.content = getContentPane();
        this.content.setLayout(new GridBagLayout());
        setSize(500, 500);
        setVisible(true);
        setTitle(Peon.class.getName() + "'s Status Screen");
        this.content.setBackground(Color.cyan);
        this.content
                .setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.gridx = 0;
        constraint.gridy = 0;
        this.content.add(new JLabel("Buh"), constraint);
    }

    /* A method for simply setting up the window and its properties
     *
     */
    public void setUpMainPanel() {
        this.content = getContentPane();
        this.content.setLayout(new GridBagLayout());
        setSize(500, 500);
        setVisible(true);
        setTitle(Peon.class.getName() + "'s Status Screen");
        this.content.setBackground(Color.cyan);
    }

    /**
     * moodChange is the JPanel intended to display this.owner's mood through taking a JLabel and
     * giving it an emoji image that show's the owner's happiness or annyoance. The emoji shown
     * depends on the owner's happyness, which is scaled from 0 to 100 in ascending order of mood
     *
     * @param moodImg -the mood image the moodPanel is set to
     **/
    public void moodChange(JLabel moodImg) {
        int annoyance = this.owner.getAnnoyance();
        int happiness = this.owner.getHappiness();
        ImageIcon happyIcon = new ImageIcon("resources/mood/happy.png");
        ImageIcon okayIcon = new ImageIcon("resources/mood/okay.png");
        ImageIcon unhappyIcon = new ImageIcon("resources/mood/unhappy.png");
        ImageIcon annoyedIcon = new ImageIcon("resources/mood/annoyed.png");
        if (happiness >= this.isHappy) {
            moodImg.setIcon(happyIcon);
        } else if (happiness < this.isHappy && happiness > this.isOkay) {
            moodImg.setIcon(okayIcon);
        } else if (happiness < this.isOkay && happiness > this.isAnnoyed) {
            moodImg.setIcon(unhappyIcon);
        } else {
            moodImg.setIcon(annoyedIcon);
        }
    }

    /**
     * setUpMoodPanel creates a small box panel that takes an image of a happy emoji that sits above
     * the inventory panel and represents the mood of the peon (this.owner in this case) holding the
     * inventory, as a form of visual feedback based on the owner's mood. This method and moodChange
     * go beyond this classes original testing purpose and provide the framework for the reworked
     * Peon status panel testing purposes
     **/
    private JPanel setUpMoodPanel() {
        JPanel moodPanel = new JPanel();
        GridBagLayout constraint = new GridBagLayout();
        JLabel moodIndicator = new JLabel(
                new ImageIcon("resources/mood/happy.png"));
        moodChange(moodIndicator);
        moodPanel.add(moodIndicator, constraint);
        return moodPanel;
    }


    /**
     * Takes an inventory and sets up a JPanel which holds the inventory given to it in the
     * inventory parameter, using gridbag grid layout, with a lisener for if the window is closed to
     * exit the program. The exit code is just a test using swing.
     *
     * @param inventory -the inventory to be displayed
     * @return JPanel displaying a peon's inventory if inventory is valid
     **/
    private JPanel setUpInventoryPanel(Inventory inventory) {
        removeItem = new JButton("Drop Item");
        // setTableDimensions()
        removeItem.addActionListener(this);
        setTableDimensions();
        table = new InventoryTable(this.numberOfRows, this.numberOfColumns);
        JPanel inventoryPanel = new JPanel();
        JLabel invLabel = new JLabel("Peon Inventory");

        invLabel.setHorizontalTextPosition(JLabel.CENTER);
        inventoryPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraint = new GridBagConstraints();

        constraint.gridy = 0;
        constraint.gridx = 0;
        constraint.anchor = GridBagConstraints.CENTER;
        inventoryPanel.add(invLabel, constraint);
        constraint.gridx = 1;
        constraint.gridwidth = 1;
        constraint.gridy = 1;
        inventoryPanel.add(table.returnTable(), constraint);
        constraint.gridy = 2;
        inventoryPanel.add(removeItem, constraint);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        return inventoryPanel;
    }


    /**
     * Sets the dimensions of the table held in setUpInventoryPanel that is created in the
     * InventoryTable subclass's populateTable method. This method forces the number of rows and
     * columns to be limited by default so that the pop-up window wouldn't change size in
     * unpredictable ways, and in the future only the number of rows will change and columns will be
     * permanently set to keep the table small yet easily readable.
     **/
    private void setTableDimensions() {
        this.inventorySlots = this.inv.getInventorySize();
        if (this.inventorySlots == 0) {
            this.numberOfRows = 0;
            this.numberOfColumns = 0;
        }
        if (this.inventorySlots % this.numberOfColumns == 0) {
            numberOfRows = inventorySlots / numberOfColumns;
        } else {
            numberOfRows = (inventorySlots / numberOfColumns) + 1;
        }
        System.out
                .println(this.inventorySlots + "/" + this.inv.getInventorySize()
                        + " in inventory " + this.numberOfColumns + " columns"
                        + this.numberOfRows + "rows" + this.inv.toString());
    }


    /**
     * This method was originally a listener for the event where the table is called upon to be
     * created, upon which an InventoryTable would be displayed, with a specified number of columns
     * and rows.
     **/
    @Override
    public void actionPerformed(ActionEvent e) {
        new InventoryTable(this.numberOfRows, this.numberOfColumns);
    }

    /**
     * InventoryTable is a private class made simply to hold a table made for the pop up window.
     **/
    private class InventoryTable extends JDialog {
        /**
         * Eclipse gave a warning for the id num below, no idea why its needed but it works! Please
         * change/explain this line if you find the time!
         *
         * @author JamesG
         */
        private static final long serialVersionUID = 1L;
        //The Jtable ui element that holds the new table
        JTable inventoryTable;
        //The actual table being constructed's ADT
        DefaultTableModel model;

        /**
         * Constructor class for inventory table, takes params as dimensions and then creates the
         * table
         *
         * @param rows    -the number of table rows
         * @param columns -table columns
         **/
        public InventoryTable(int rows, int columns) {
            inventoryTable = new JTable(rows, columns);
            populateTable();
        }

        /**
         * Takes the constructor's properties, iterates over the number of rows and columns while
         * popping string representations of items from the inventory set in the outer constructor
         * method above to table cells. Don't worry, its WAY less complicated than it looks, we're
         * just making a table!
         **/
        public void populateTable() {
            int columnEntry = 0;
            boolean lastRowSet = false;
            int itemsLeftToAdd = InventoryPopUp.this.inventorySlots;
            // Returns the TableModel that provides the data displayed by this
            // JTable(inventoryTable), and assigns it model so data can be added
            model = (DefaultTableModel) inventoryTable.getModel();
            // Declare rowOfItems as a row of Inventory.InventoryItem objects
            String[] rowOfItems =
                    new String[InventoryPopUp.this.numberOfColumns];
            String[] lastRowOfItems = null;
            Inventory.InventoryItem currentItem;

            for (int row = 0; row <= InventoryPopUp.this.numberOfRows; row++) {
                while (columnEntry < InventoryPopUp.this.numberOfColumns) {
                    currentItem = InventoryPopUp.this.inv.getInventory()
                            .get(columnEntry);

                    // If we're in the last row of items
                    if (itemsLeftToAdd <= InventoryPopUp.this.numberOfColumns) {
                        /*
                         * Have we set the size of the last row to be the items
						 * remaining?
						 */
                        if (!lastRowSet) {
                            lastRowOfItems = new String[itemsLeftToAdd];
                            lastRowSet = true;
                        }
                        lastRowOfItems[columnEntry] = currentItem.toString();
                    } else { // If we haven't reached the last row of items
                        rowOfItems[columnEntry] = currentItem.toString();
                    }
                    itemsLeftToAdd--;
                    columnEntry++;
                }
                // If we init last row, add the last row
                if (lastRowSet) {
                    model.addRow(lastRowOfItems);
                    // Empty the array so it can be used to fill in the next row
                    Arrays.fill(lastRowOfItems, null);
                    // And finish!
                    return;
                } else { // Else keep adding the normal rowOfItems arry
                    model.addRow(rowOfItems);
                    // Empty the array so it can be used to fill in the next row
                    Arrays.fill(rowOfItems, null);
                }

            }
        }

        /**
         * returns the table made in the constructor
         *
         * @return the JTable made and displayed
         **/
        public JTable returnTable() {
            return new JTable(this.model);
        }
    }
}