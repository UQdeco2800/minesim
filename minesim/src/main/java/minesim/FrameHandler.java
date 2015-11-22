package minesim;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import minesim.buffs.Buff;
import minesim.entities.*;
import minesim.entities.items.Elevator;
import minesim.entities.items.Ladder;
import minesim.entities.items.Rope;
import minesim.entities.items.properties.HasHealth;

import java.awt.geom.Rectangle2D;
import java.util.concurrent.ConcurrentLinkedDeque;

public class FrameHandler extends AnimationTimer {

    ResizableCanvas canvas;
    GraphicsContext graphicscontext;
	World mainworld;
	Group root;

	// FPS Counter
	private long lastTime = 0;
	private long lastFPSUpdate = 0;
	float fps = 0;
	float oldfps = 0;
	
	private ConcurrentLinkedDeque<Class> hasNameIgnored = new ConcurrentLinkedDeque<Class>();
	private ConcurrentLinkedDeque<Class> hasHealthIgnored = new ConcurrentLinkedDeque<Class>();

	public FrameHandler(GraphicsContext graphicscontext, World world, ResizableCanvas canvas) {
		this.mainworld = world;
		this.root = root;
		this.graphicscontext = graphicscontext;
        this.canvas = canvas;
	}

    private void updateFPS() {
        long thisTime = System.nanoTime();

        fps = 1 / ((thisTime - lastTime) / (float) (1000000000.0));

        lastTime = thisTime;
    }
    
    /**display a shovel icon on the top right corner of the screen if is true.**/
    public void diggingText(boolean visible) {
    	if (visible){
    	graphicscontext.drawImage(new Image("cursor/dig.jpg"), graphicscontext.getCanvas().getWidth()*0.9,20);}
    }
    
    
    /*
     * Updates the screen with the Sticky Camera icon if true
     */
    public void stickyCamIcon(boolean visible) {
    	if (visible) {
            graphicscontext.drawImage(new Image("Icons/stickyCamera.png"), graphicscontext.getCanvas().getWidth()*0.9,5);
        }
    }

    private void renderFPS() {
        if (System.nanoTime() - lastFPSUpdate > 100000000.0) {
            oldfps = fps;
            lastFPSUpdate = System.nanoTime();
        }
        /**set the color and position of the FPS in the game.**/
        graphicscontext.setFill(Color.BLACK);
        graphicscontext.fillText("FPS: " + (long) oldfps, 20, 20);

        //visible if we're in the digging context
        diggingText(mainworld.getContextMode() == 1);
        //visible in sticky cam context
        stickyCamIcon(mainworld.getContextMode() == 2);
    }

    /**set the color and the position of the time in the game**/
    private void renderTime() {
        graphicscontext.setFill(Color.BLACK);
        graphicscontext.fillText(mainworld.getWorldTimer().getTimeString(), 100, 20);
    }

    private void drawSelectedArea() {
        graphicscontext.setFill(mainworld.getAreaColor());
        Rectangle2D rectangle = mainworld.getScreenArea();
        graphicscontext.fillRect(rectangle.getX(), rectangle.getY(),
                rectangle.getWidth(), rectangle.getHeight());
    }
    
    private void renderAchievments(){
        for(WorldEntity e : World.getInstance().achievments){
            if( !(e instanceof Achievement)){
                World.getInstance().achievments.remove(e);
                continue;
            }

            if (e.image.isPresent()) {
                double width = graphicscontext.getCanvas().getWidth();
                graphicscontext.setGlobalAlpha(((Achievement) e).getRemainingTime()/100f);
                graphicscontext.drawImage(new Image(e.image.get()), (width - e.width)/2, 10, e.width, e.height);
                graphicscontext.setGlobalAlpha(1);
            }

            e.onTick();
        }
    }
    

    @Override
    public void handle(long now) {
    	
        addHasNameIgnoredClass(Rope.class);
        addHasNameIgnoredClass(Ladder.class);
        addHasNameIgnoredClass(Elevator.class);
        addHasNameIgnoredClass(ElevatorShaft.class);
        addHasHealthIgnoredClass(Rope.class);
        addHasHealthIgnoredClass(Ladder.class);
        addHasHealthIgnoredClass(Elevator.class);
        addHasHealthIgnoredClass(ElevatorShaft.class);

        updateFPS();

        // Update mainworld size
        mainworld.setDimensions((int)canvas.getWidth(), (int)canvas.getHeight());

        // Clear the pixel map
        graphicscontext.clearRect(0, 0, canvas.prefWidth(), canvas.prefHeight());

        // Render background depends on time

        RadialGradient radial = mainworld.getWorldTimer().backgroundGenerator(mainworld.getXOffset(), mainworld.getYOffset());
//        System.out.println(radial);
        graphicscontext.setFill(radial);
        graphicscontext.fillRect(0, 0, canvas.prefWidth(), canvas.prefHeight());

        // Render tiles.
        mainworld.getTileManager().render(graphicscontext,
                mainworld.getXOffset(), mainworld.getYOffset(), mainworld.getWidth(), mainworld.getHeight());

        if (mainworld.getArea() != null)
            drawSelectedArea();

        // Render the entities
        for (WorldEntity entity : mainworld.getWorldentities()) {
            if (entity.alive) {

                if (entity.image.isPresent()) {
                    graphicscontext.drawImage(new Image(entity.image.get()), entity.getXpos() - mainworld.getXOffset(),
                            entity.ypos - mainworld.getYOffset(), entity.width, entity.height);
                } else if (entity.animationIsPresent()) {
                	for( int i = 0; i < entity.animation.size(); i++){
	                    graphicscontext.drawImage(entity.animation.get(i), entity.getXpos() - mainworld.getXOffset(),
	                            entity.ypos - mainworld.getYOffset(),
	                            entity.width, entity.height);
	                }
                } else {
                    int a = entity.getXpos();
                    graphicscontext.fillRect(entity.getXpos(), entity.ypos, entity.width, entity.height);
                }

                if (entity instanceof HasName && !(hasNameIgnored.contains(entity.getClass()))) {
                    //draw the entities name over its head

                    graphicscontext.fillText(((HasName) entity).getName(), entity.getXpos() - mainworld.getXOffset(),
                            entity.ypos - mainworld.getYOffset() - 20);
                }

                if (entity instanceof HasHealth && !(hasNameIgnored.contains(entity.getClass()))) {
                    // We should draw a health bar

                    // Draw the dead bar first
                    graphicscontext.setFill(Color.RED);
                    graphicscontext.fillRect(entity.getXpos() - mainworld.getXOffset(),
                            entity.ypos - mainworld.getYOffset() - 15, entity.width, 5);

                    // The overlay the alive bar, so that dead section is visible
                    graphicscontext.setFill(Color.GREEN);
                    graphicscontext.fillRect(entity.getXpos() - mainworld.getXOffset(),
                            entity.ypos - mainworld.getYOffset() - 15,
                            entity.width * ((HasHealth) entity).getHealth() / 100, 5);

                    // Set fill color back to black (default)
                    graphicscontext.setFill(Color.BLACK);
                }

                if (entity instanceof HasTiredness) {
                    // Draw the tired bar first
                    graphicscontext.setFill(Color.RED);
                    graphicscontext.fillRect(entity.getXpos() - mainworld.getXOffset(),
                            entity.ypos - mainworld.getYOffset() - 10, entity.width, 5);

                    // The overlay the alive bar, so that dead section is visible
                    graphicscontext.setFill(Color.BLUE);
                    graphicscontext.fillRect(entity.getXpos() - mainworld.getXOffset(),
                            entity.ypos - mainworld.getYOffset() - 10,
                            entity.width * (1000 - ((HasTiredness) entity).getTiredness()) / 1000, 5);

                    // Set fill color back to black (default)
                    graphicscontext.setFill(Color.BLACK);
                }
                
                if (entity.hasBuff()) {
                	int upamount = 1;
                	for (Buff b: entity.getBuffs()) {
                		graphicscontext.drawImage(new Image(b.getImageUrl()), entity.getXpos() - mainworld.getXOffset() - 12 - 1,
                                entity.ypos - mainworld.getYOffset() - 15 - upamount*12, 12, 12);
                		upamount += 1;
                	}
                	
//                	graphicscontext.drawImage(new Image("BuffImages/disease.png"), entity.getXpos() - mainworld.getXOffset() - 12,
//                            entity.ypos - mainworld.getYOffset() - 24, 12, 12);
                }

                //if()

            } else {
                mainworld.removeEntityFromWorld(entity);
            }
        }
        
        renderAchievments();
        renderFPS();
        renderTime();
    }
    
    public void addHasNameIgnoredClass(Class x) {
        hasNameIgnored.add(x);
    }

    public void removeHasNameIgnoredClass(Class x) {
        hasNameIgnored.remove(x);
    }
    
    public void addHasHealthIgnoredClass(Class x) {
        hasHealthIgnored.add(x);
    }

    public void removeHasHealthIgnoredClass(Class x) {
        hasHealthIgnored.remove(x);
    }
}
