package minesim.entities;

import minesim.WorldTimer;
/** The class intended to tick in real time to update
 * PeonStatusContextControllerHandler by simply running
 * controller.setPeon(Peon) repeatedly, whenever the pane is active
 * Created by James on 10/22/2015.
 */
public class mineEventHandlerTrigger extends WorldEntity {
    WorldTimer clock = new WorldTimer();

    public mineEventHandlerTrigger(int xpos, int ypos, int height, int width,
               String spriteMovements, String attackSprite, int attackFrames,
               int spriteX, int spriteY, int mobHealth, int speed, int range,
               int delay) {
        super(xpos, ypos, height, width);
    }

    @Override
    public void onTick() {

    }

    //    public void mineEventHandlerTrigger() {
//        clock.onTick();
//        System.out.println("Time:" + clock.getMins());
//    }
}
