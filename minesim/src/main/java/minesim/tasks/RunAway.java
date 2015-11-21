package minesim.tasks;

import java.util.Optional;
import java.util.Random;

import minesim.entities.Peon;

enum Running {
    YES, NO
}

public class RunAway extends Task {

    Running state = Running.YES;
    int peon;
    int distance;
    int run;
    

    public RunAway(Peon parent) {
        super(parent, 0, 0);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void doTask() {
        super.doTask();
        peon = getPeon().getXpos();
        distance = getRandomNumberInRange(-400, 400);
        run = peon + distance;
        if (run < 15) {
            run = 15;
        }
        if (getPeon().getHealth() <= 0) {
            getPeon().standStill();
            getPeon().updateTask(Optional.<Task>empty());
        }
        if (run > peon) {
            getPeon().animateRight();
        } else {
            getPeon().animateLeft();
        }
        if (state == Running.YES) {
            if (!getPeon().moveTowards(run, 0, 10)) {
                state = Running.NO;
            }
        } else {
            getPeon().standStill();
            getPeon().updateTask(Optional.<Task>empty());
        }
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
