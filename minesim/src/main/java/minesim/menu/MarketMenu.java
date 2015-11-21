package minesim.menu;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import static javafx.geometry.Pos.*;

/**
 * Created by Isaac Hill @isaachill from team Dahl
 * The GUI for the marketMenu to interact with the mineClient, Has no interaction with mineClient or marketPlace as
 * calling mineClient from common causes Errors NoClassDefFoundError and has therefore been left out.
 */
public class MarketMenu  {
    private static StackPane marketContainer =  new StackPane();
    private static Button peonButton = new Button("Peons");
    private static Button marketSell = new Button("Sell");
    private static StackPane mContext = new StackPane();
    private static HBox buttons = new HBox();

    private static HBox peonPane;

    private static HBox buyPane;

    private static VBox sellPane;

    private static VBox wallet;

    private static Label myMoney = new Label("fake money ammount");
    /** Unused Contructor.
     *
     */
    public MarketMenu() {

    }

    /**
     * called initially to attach the menu to the main pane.
     * Creates children nodes and adds CSS.
     * only run once.
     * @param attachmentPoint
     * @return
     */
    public static MarketMenu init(StackPane attachmentPoint) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                attachmentPoint.getChildren().add(marketContainer);
                buttons.getChildren().add(peonButton);
                buttons.getChildren().add(marketSell);
                marketContainer.getStylesheets().add("css/market/buttons.css");
                marketContainer.getStylesheets().add("css/market/Panes.css");
                marketContainer.getStyleClass().add("container");
                marketContainer.setMaxHeight(300);
                marketContainer.setMaxWidth(500);
                marketContainer.setPrefHeight(300);
                marketContainer.setPrefWidth(500);
                marketContainer.setAlignment(mContext, BOTTOM_RIGHT);
                marketContainer.getChildren().add(mContext);
                mContext.setMaxHeight(245);
                mContext.setMaxWidth(395);
                mContext.getStyleClass().add("context");
                mContext.setAlignment(CENTER);
                buttons.setSpacing(20);
                marketContainer.setAlignment(buttons, TOP_CENTER);
                marketContainer.getChildren().add(buttons);
                buttons.setMaxHeight(50);
                buttons.fillHeightProperty();
                buttons.setMaxWidth(500);
                buttons.setAlignment(CENTER);
                peonButton.setAlignment(CENTER);
                marketSell.setAlignment(CENTER);
                setPeonPanel();
                setBuyMenu();
                setSellMenu();
                setWalletMenu();

                Button buyButton = new Button("Buy");
                buyButton.setAlignment(CENTER);
                buttons.getChildren().add(buyButton);

                buttons.setPadding(new Insets(20, 40, 20, 40));


                peonButton.setOnAction(event ->  showPanel(peonPane));
                marketSell.setOnAction(event -> showPanel(sellPane));
                buyButton.setOnAction(event -> showPanel(buyPane));
            }
        });
        marketContainer.setVisible(false);
        return new MarketMenu();
    }

    /**
     * taking a Pane, sets all other panes within the market Context area to not be visiable and sets the given pane
     * as visible
     * @param pane
     */
    private static void showPanel(Pane pane) {
        peonPane.setVisible(false);
        buyPane.setVisible(false);
        sellPane.setVisible(false);
        pane.setVisible(true);
    }

    /**
     * Creates the UI for the Peon pane, which is a child of the mContext node.
     * called once during init.
     */
    private static void setPeonPanel() {
        peonPane = new HBox();
        peonPane.getStyleClass().add("hboxContainer");
        peonPane.setPadding(new Insets(20, 40, 20, 40));

        Label bestMiner = new Label("Miners");
        Label bestGaurd = new Label("Guards");
        Label bestGather = new Label("Gathers");
        ScrollPane minerPane = new ScrollPane();
        ScrollPane gaurdPane = new ScrollPane();
        ScrollPane gatherPane = new ScrollPane();
        VBox miner = new VBox(bestMiner,minerPane);
        miner.setAlignment(CENTER);
        VBox guard = new VBox(bestGaurd, gaurdPane);
        guard.setAlignment(CENTER);
        VBox gather = new VBox(bestGather, gatherPane);
        gather.setAlignment(CENTER);
        peonPane.setAlignment(CENTER);
        peonPane.getChildren().addAll(miner, guard, gather);
        peonPane.setSpacing(20);
        
        mContext.getChildren().add(peonPane);
        peonPane.setVisible(false);
    }

    /**
     * Creates the UI for the peon Pane, which is a child of hte mContext node.
     * Called once during init.
     */
    private static void setBuyMenu() {
        buyPane = new HBox();
        buyPane.getStyleClass().add("hboxContainer");
        buyPane.setPadding(new Insets(20, 40, 20, 40));
        buyPane.setAlignment(CENTER);
        buyPane.setSpacing(30);
        VBox farmContainer = new VBox();
        VBox factoryContainer = new VBox();
        Label farmLabel = new Label("Farm's Products");
        Label factoryLabel = new Label("Factory's Products");

        ScrollPane factoryHolder = new ScrollPane();
        ScrollPane farmHolder = new ScrollPane();

        VBox factoryItems = new VBox();
        VBox farmItems = new VBox();

        farmContainer.getChildren().addAll(farmLabel, farmHolder);
        factoryContainer.getChildren().addAll(factoryLabel, factoryHolder);
        buyPane.getChildren().addAll(farmContainer, factoryContainer);
        buyPane.getStyleClass().add("buyPanes");



        factoryHolder.setContent(factoryItems);
        farmHolder.setContent(farmItems);
        buyPane.setVisible(false);
        mContext.getChildren().add(buyPane);

    }

    /**
     * Creates the UI for the Sell Pane, Which is a child of the mContext node.
     * Called once during init.
     */
    private static void setSellMenu() {
        sellPane = new VBox();

        VBox itemContainer = new VBox();

        ScrollPane itemHolder = new ScrollPane();
        itemContainer.getChildren().addAll(new Label("My Items"), itemHolder);
        itemHolder.getStyleClass().add("sellItems");
        GridPane items = new GridPane();
        HBox sellContainer = new HBox();
        VBox quantityContainer = new VBox();
        Label quantityLabel = new Label("Quantity");
        TextField quantity = new TextField();
        quantityContainer.getChildren().addAll(quantityLabel, quantity);
        VBox priceContainer = new VBox();
        Label priceLabel = new Label("Price");
        TextField price = new TextField();
        price.getStyleClass().add("textField");
        quantity.getStyleClass().add("textField");
        priceContainer.getChildren().addAll(priceLabel, price);
        Button confirm = new Button("Sell Item");
        Pane currentItem = new Pane();



        itemHolder.setContent(items);
        sellContainer.getChildren().addAll(quantityContainer, priceContainer, confirm);
        sellContainer.setSpacing(15);
        sellContainer.getStyleClass().add("sellContainer");
        currentItem.setMaxHeight(50);
        currentItem.setMaxWidth(100);
        currentItem.setStyle("-fx-background-color: red;");
        sellPane.getChildren().addAll(itemContainer, sellContainer);
        sellPane.setVisible(false);
        sellPane.setPadding(new Insets(20,40, 20, 40));
        sellPane.setSpacing(40);
        mContext.getChildren().add(sellPane);



    }

    /**
     * Creates the UI for the Wallet Pane, Will display general Information, such as current money.
     * Called once during init.
     */
    private static void setWalletMenu() {
        wallet = new VBox();
        Label moneyTitle = new Label("my Money");
        myMoney = new Label("current money");
        marketContainer.setAlignment(wallet, BOTTOM_LEFT);
        wallet.setMaxHeight(245);
        wallet.setMaxWidth(95);
        wallet.getChildren().add(moneyTitle);
        wallet.getChildren().add(myMoney);
        marketContainer.getChildren().add(wallet);
        wallet.getStyleClass().add("context");

    }


    /**
     * Displays the Market Menu, if not visible, else hides the market Menu.
     */
    public static void showMenu() {
        if (marketContainer.isVisible()) {
            marketContainer.setVisible(false);
        } else {
            marketContainer.setVisible(true);
        }
    }
}


