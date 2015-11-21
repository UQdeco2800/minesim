package minesim;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import minesim.events.tracker.MineEventHandler;

public class WorldTimer {

    private int ticks;
    //private MineEventHandler eventhandler = new MineEventHandler(Music);

    MineEventHandler eventhandler = MineEventHandler.getInstance();

    private int mins;

    private int hours;

    public WorldTimer() {
        ticks = 0;
        mins = 0;
        hours = 8;
    }

	public void onTick() {
        ticks++;
        if (ticks >= 10) {
            ticks = 0;
            mins++;
        }
        if (mins >= 60) {
            mins = 0;
            hours++;
        }
        if (hours >= 24) {
            hours = 0;
        }
		
        eventhandler.checkEvents();
        
    }
	
	/**Dynamic background**/
    public RadialGradient backgroundGenerator(double xOffset, double yOffset) {
        int x = 0;
        int y = 0;
        /**Sin formula that keeps generating the x and y value based on the time.**/
		double positionOfY= 400 - 400 * Math.sin((((double)hours+((double)mins/60)-6)/12)*22/7) - yOffset;
		double positionOfX= ((((double)hours + ((double)mins/60)-6)/12)*1600)-300 - xOffset/10;
		RadialGradient gradient1 = null;

        //System.out.println(positionOfX);
        //System.out.println(positionOfY);
		/**radial gradient to set the position of the x and y and the gradient colors**/
        gradient1 = new RadialGradient(0,
                0,
                positionOfX,
                positionOfY,
                1600,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0.03, Color.WHITE),
                new Stop(0.04, Color.YELLOW),
                new Stop(0.1, Color.WHITE),
                new Stop(0.7, Color.SKYBLUE),
                new Stop(1.0, Color.DARKBLUE));


        return gradient1;
    }
    
    /**time formatting**/
    public String getTimeString() {
        return String.valueOf(hours) + ':' + String.valueOf(mins);
    }
    
    /**function that gets the hour**/
    public int getHours() {
        return hours;
    }
    /**function that gets the min**/
    public int getMins() {
        return mins;
    }

    public int isDay() {
        if (hours < 6 || hours > 18) {
            return 0;
        } else {
            return 1;
        }
    }
}
