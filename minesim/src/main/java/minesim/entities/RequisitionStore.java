package minesim.entities;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import minesim.ContextAreaHandler;
import minesim.DatabaseHandler;
import minesim.StockInfo;
import minesim.entities.items.Item;
import notifications.NotificationManager;
import org.apache.log4j.Logger;


/**
 * requisition Created by Isaac-macPro on 20/08/15.
 * The Requisition Store works as a storage house, allowing for peons to drop items and leave them, where they will
 * be safe and unedited when they Player leaves the game and logs back in.
 */
public class RequisitionStore extends WorldEntity {

    private Pane context = null;
    private VBox gearMenu = null;
    private static GridPane storeGear = null;
    private static GridPane peonGear = null;




    private static Peon peon = null;

    private static DatabaseHandler stockTable = DatabaseHandler.getInstance();
    private static final Logger LOGGER = Logger.getLogger(RequisitionStore.class);

    /**
     * given the attributes for a worldEntity , when the Requisiton store is first called it its javaFx node and children
     * and appends it to the ContextAreaHandler, under the storeMenu title to be called when needed.
     * @param xpos
     * @param ypos
     * @param height
     * @param width
     */
    public RequisitionStore(int xpos, int ypos, int height, int width) {
        super(xpos, ypos, height, width);
        setImageurl("/ReqStore.png");
        try {
            VBox menu = new VBox();
            menu.setAlignment(Pos.TOP_CENTER);
            menu.setSpacing(5);
            GridPane buttons = new GridPane();
            this.context = new StackPane();
            context.setMaxHeight(400);
            context.setMaxWidth(300);

            //initial set up of gearMenu
            this.gearMenu = gearMenu();
            context.getChildren().add(gearMenu);


            menu.getChildren().add(buttons);
            context.getStylesheets().add("css/store/storeStyles.css");

            menu.getChildren().add(context);
            ContextAreaHandler.getInstance().addContext("storeMenu", menu);
        } catch (NoClassDefFoundError | ExceptionInInitializerError e) {
            LOGGER.debug("JavaFX not running - are you running tests?", e);
        }

    }


    /**
     * Given a peon, sets the peon as the new master peon, to be called and used in different methods within Requisiton Store.
     * Also changes the current ContextArea to display Store menu and updates the store and peon Inventory nodes with
     * current information.
     * @param entityClicked
     */
    private void showStoreInfo(Peon entityClicked) {
        try {
            ContextAreaHandler window = ContextAreaHandler.getInstance();
            peon = entityClicked;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    populateGear();
                }
            });
            window.setContext("storeMenu");
        } catch (NoClassDefFoundError | ExceptionInInitializerError e) {
            LOGGER.debug("JavaFX not running - are you running tests?", e);
        }
    }

    /**
     * helper method to save time when calling to populate both the peon Inventory pane and the Store Pane.
     */
    private static void populateGear() {
        populateInv(peonGear);
        populateStore(storeGear);

    }

    /**
     * given the store pane will create a new button and label which gives the name and amount of an item for each
     * item within the Database, Shows all items, even if stock is 0, to show Players all available items that can be
     * found in the game.
     * @param storeGear
     */
    private static void populateStore(GridPane storeGear) {
        storeGear.getChildren().clear();

        int i = 0;
        for (StockInfo stock :  stockTable.getAllItems()) {
            Button button = new Button();
            button.setPrefWidth(80);
            button.minWidth(80);
            button.minHeight(30);
            button.setText(stock.getName());
            button.setOnAction(event -> itemMovement(stock));
            storeGear.add(button, 0, i);
            Label label = new Label(String.format(" %02d ",stock.getStock()));
            label.minWidth(40);
            label.maxWidth(40);
            label.minHeight(25);
            storeGear.add(label,1,i);
            i++;
        }
    }

    /**
     * given the peon Pane, will create a new button and label which gives the name and amount of an item for each
     * item within the Peons Inventory.
     * @param peonGear
     */
    private static void populateInv(GridPane peonGear) {
        peonGear.getChildren().clear();
        LOGGER.warn("this occured");
        int j = 0;
        for (Inventory.InventoryItem item : peon.peonInventory.getInventory()) {
            Button button = new Button();
            button.setPrefWidth(80);
            button.minHeight(30);
            button.setMaxWidth(80);
            button.setText(item.getName());
            button.setOnAction(event -> itemMovement(item));
            peonGear.add(button, 0, j);
            Label label = new Label(String.format(" %02d ", item.getAmount()));
            label.minWidth(40);
            label.setMaxWidth(40);
            label.minHeight(30);
            peonGear.add(label,1 ,j);
            j++;
        }

    }

    /**
     * creates the Trade gear menu and sets up all of the CSS for the context window, called on Requsition Store
     * Construction
     *
     */
    private VBox gearMenu() {
        VBox gearMenu = new VBox();
        gearMenu.setAlignment(Pos.CENTER);
        gearMenu.setSpacing(10);

        Label title = new Label("Trade Gear");
        gearMenu.getChildren().add(title);

        ScrollPane storeGearScroll = new ScrollPane();
        storeGearScroll.fitToWidthProperty();
        storeGearScroll.setMaxHeight(150);
        storeGearScroll.setMaxWidth(125);
        storeGearScroll.setMinHeight(150);
        storeGearScroll.setMinWidth(125);
        storeGearScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        storeGearScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        storeGearScroll.getStylesheets().add("css/store/storeStyles.css");
        storeGearScroll.getStyleClass().add("holder-pane");

        Label store = new Label("Store");
        gearMenu.getChildren().add(store);
        GridPane storeGear = new GridPane();
        storeGear.getStyleClass().add("holder-pane");
        storeGear.setMaxHeight(300);
        storeGear.setMaxWidth(125);
        storeGear.setMinWidth(125);
        storeGear.setMinHeight(150);
        storeGear.setAlignment(Pos.TOP_LEFT);
        storeGear.getStylesheets().add("css/store/ItemButtons.css");
        storeGearScroll.setContent(storeGear);

        ScrollPane peonGearScroll = new ScrollPane();
        peonGearScroll.fitToWidthProperty();
        peonGearScroll.setMaxHeight(150);
        peonGearScroll.setMaxWidth(125);
        peonGearScroll.setMinHeight(150);
        peonGearScroll.setMinWidth(125);
        storeGearScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        storeGearScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        peonGearScroll.getStylesheets().add("css/store/storeStyles.css");

        GridPane peonGear = new GridPane();
        peonGear.getStyleClass().add("holder-pane");
        peonGear.setMaxHeight(300);
        peonGear.setMaxWidth(125);
        peonGear.setAlignment(Pos.TOP_LEFT);
        peonGearScroll.setContent(peonGear);
        peonGear.getStylesheets().add("css/store/ItemButtons.css");


        gearMenu.getChildren().add(storeGearScroll);
        Label inven = new Label("Inventory");
        gearMenu.getChildren().add(inven);

        gearMenu.getChildren().add(peonGearScroll);

        setStoreGear(storeGear);
        setPeonGear(peonGear);

        return gearMenu;

    }

    /**
     * Moves an item from the Store to the Peon Inventory, as well as updating the store menu and sending the relevant
     * Notification
     * @param stock
     */
    private static void itemMovement(StockInfo stock) {
        if (moveItem(stock)) {
            LOGGER.warn("dis happened apparnetly  "+stock.getName());
            NotificationManager.notify(stock.getName() + " given to " + peon.getName());
            populateGear();
        } else {
            NotificationManager.notify(peon.getName()+"'s Inventory is full");
        }
    }

    /**
     * Moves an item from the Peon Inventory to the Store, as well as updating the store menu and sending the relevant
     * Notification
     * @param stock
     */
    private static void itemMovement(Inventory.InventoryItem stock) {
        moveItem(stock);
        NotificationManager.notify(stock.getName() + "given to Store by " + peon.getName());
        populateGear();
    }
    /**
     * accesses the Database and insures that the requested item, meets all the required conditions before
     * creating an instance of this item and appending it to the the Peon Inventory. If the item within the peon's
     * Inventory is at Max Stack the item is made into a new stack.
     * @param stock
     */
    public static boolean moveItem(StockInfo stock) {
        Item item = stockTable.getItem(stock.getName(), 1);
        if (item == null) {
            return false;
        }
        for (Inventory.InventoryItem pitem : peon.getInventory().getInventory()) {
            if (pitem.getName().equals(item.getName())) {
                if (pitem.stackFull()) {
                    continue;
                } else {
                    pitem.incrementAmount(1);
                    return true;
                }
            }
        }
        if ((peon.getInventory().inventorySlotsLeft()<= 0)) {
            stockTable.addStock(stock.getName(), 1);
            return false ;
        } else {
            peon.getInventory().addItem(item);
            return true;
        }

    }

    /**
     * Moves the selected item from a Peons Inventory to the Store by accessing the database, Removes the instance of
     * the item if the current stack is 1 before item removal, otherwise simply reduces stack by 1.
     * @param item - a item within the Peons Inventory
     */
    public static boolean moveItem(Inventory.InventoryItem item) {
        if (item.getAmount() == 1) {
            peon.getInventory().getInventory().remove(item);
        }
        item.decrementAmount(1);
        stockTable.addStock(item.getName(),1);
        return true;
    }

    /**
     * Called when WorldEntity interacts with store, if WorldEntity is an instance of Peon calls
     * showStoreInfo and returns true, otherwise does nothing and returns false
     * @param sender the sending objects
     * @return
     */
    @Override
    public Boolean interact(WorldEntity sender) {
        Peon peon;
        if (sender instanceof Peon) {
            peon = (Peon) sender;
        } else {
            LOGGER.debug("was not instance of Peon");
            return false;
        }
        showStoreInfo(peon);
        return true;
    }





    private void setStoreGear(GridPane storeGear) {
        this.storeGear = storeGear;
    }
    private void setPeonGear(GridPane peonGear) {
        this.peonGear = peonGear;
    }
    public void setDatabase(DatabaseHandler database) {
        this.stockTable = database;
    }
    public DatabaseHandler getDatabase() {
        return this.stockTable;
    }
}
