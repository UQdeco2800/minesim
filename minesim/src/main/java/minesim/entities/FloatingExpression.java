package minesim.entities;

import javafx.scene.paint.Color;

public class FloatingExpression extends WorldEntity {

    private int expressionDuration;
    private Boolean fadeOut;
    private float alphaValue;

    /**
     *
     * @param xpos
     * @param ypos
     * @param height
     * @param width
     * @param duration
     */
    public FloatingExpression(int xpos, int ypos, int height, int width, int duration) {
        super(xpos, ypos, height, width);
        setImageurl("/exclamantionPoint.png");
        expressionDuration = duration;
        alphaValue = 1;
        fadeOut = Boolean.FALSE;
        setEntityGravity(Boolean.FALSE);

    }

    public void onTick() {
        super.onTick();
        this.ypos--;
        //System.out.println(alphaValue);
        if (expressionDuration == 0) {
            fadeOut = Boolean.TRUE;
        }
        if (fadeOut == Boolean.TRUE && alphaValue >= 0) {
            this.setEntityColor(Color.rgb(0, 0, 0, alphaValue));
            alphaValue -= 0.01;
        }
        if (alphaValue <= 0.1) {
            this.alive = Boolean.FALSE;
        }
        expressionDuration--;
    }

}
