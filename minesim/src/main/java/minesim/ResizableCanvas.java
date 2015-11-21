package minesim;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

/**
 * Created by Michael on 2/09/2015.
 */
public class ResizableCanvas extends Canvas {

    public ResizableCanvas() {
        // Redraw canvas when size changes
    }

    //    @Override
    public boolean isResizeable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    public double prefWidth() {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }

    public double prefHeight() {
        return getHeight();
    }

    /**
     * Bind to a pane
     *
     * @param parent - the pane to bind to
     */
    public void bindTo(Pane parent) {
        this.widthProperty().bind(parent.widthProperty());
        this.heightProperty().bind(parent.heightProperty());
    }
}
