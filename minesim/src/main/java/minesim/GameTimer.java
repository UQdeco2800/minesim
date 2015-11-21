package minesim;

/**
 * Produces a game tick (100tps)
 */
public class GameTimer implements Runnable {
    World gameworld;
    int tickcount = 50;

    public GameTimer(World world, int tickcount) {
        this.gameworld = world;
        this.tickcount = tickcount;
    }

    @Override
    public void run() {
        while (true) {
            gameworld.onTick(0);
            try {
                Thread.sleep(tickcount);
            } catch (InterruptedException e) {
                // TODO Logger
            }
        }
    }
}
