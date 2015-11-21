package minesim.menu;

import org.apache.log4j.Logger;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Created by Michael on 9/10/2015.
 */
public class DebugBox {
    private static final Logger LOG = Logger.getLogger(DebugBox.class);

    private static VBox contents = new VBox();

    private DebugBox() {

    }

    /**
     * Returns the a pointer to the pane
     * @return
     */
    public static Pane getPane() {
        return contents;
    }

    /**
     * Addes a node to the contents of the debug menu
     * @param newNode
     */
    public static void addNode(Node newNode) {
        contents.getChildren().add(newNode);
    }
}
