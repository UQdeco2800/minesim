package minesim.menu;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import minesim.Sound;

/**
 * Extends the Javafx Button and plays the button click noise when fired
 * Created by Michael on 10/10/2015.
 */
public class Button extends javafx.scene.control.Button {
    Sound btnClick = new Sound("btnClick.mp3");

    public Button() {
        super();
    }

    public Button(String var1) {
        super(var1);
    }

    public Button(String var1, Node var2) {
        super(var1, var2);
    }

    @Override
    public void fire() {
        btnClick.play();
        super.fireEvent(new ActionEvent());
    }
}