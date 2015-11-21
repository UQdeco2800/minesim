package minesim.entities;

import java.util.ArrayList;

import minesim.World;
import minesim.entities.items.properties.HasHealth;
import javafx.scene.image.Image;

public class Grave extends WorldEntity implements HasHealth {
	SpriteLoader graveSpawn = new SpriteLoader();

	private ArrayList<Image> spawnFromGrave = new ArrayList<Image>();
	private ArrayList<Image> digGraveOut = new ArrayList<Image>();
	private ArrayList<Image> idleAnimation = new ArrayList<Image>();

	private Animation spawnGrave;
	private Animation digGraveOutAnimation;
	private Animation idle;

	private ArrayList<Animation> graveAnimation = new ArrayList<Animation>();

	private int health = 100;
	private Boolean dead = Boolean.FALSE;

	private Boolean spawnZombie = Boolean.FALSE;
	private int percentSpawn;
	private int enrage;
	private Boolean spawnVar = Boolean.FALSE;

	public Grave(int xpos, int ypos, int height, int width) {
		super(xpos, ypos, height, width);
		graveSpawn.loadSpriteSheet("graveWithZombieSpriteSheettest", 60, 46);
		setUpAnimation();
		setAnimation(graveAnimation);
	}

	private void setUpAnimation() {
		spawnFromGrave.add(graveSpawn.getSprite(8, 0));
		spawnFromGrave.add(graveSpawn.getSprite(7, 0));
		spawnFromGrave.add(graveSpawn.getSprite(6, 0));
		spawnFromGrave.add(graveSpawn.getSprite(5, 0));
		spawnFromGrave.add(graveSpawn.getSprite(4, 0));

		idleAnimation.add(graveSpawn.getSprite(4, 0));
		idleAnimation.add(graveSpawn.getSprite(4, 0));

		digGraveOut.add(graveSpawn.getSprite(4, 0));
		digGraveOut.add(graveSpawn.getSprite(3, 0));
		digGraveOut.add(graveSpawn.getSprite(2, 0));
		digGraveOut.add(graveSpawn.getSprite(1, 0));
		digGraveOut.add(graveSpawn.getSprite(0, 0));

		digGraveOutAnimation = new Animation(digGraveOut, 60);
		spawnGrave = new Animation(spawnFromGrave, 60);
		idle = new Animation(idleAnimation, 60);

		graveAnimation.add(spawnGrave);
		graveAnimation.get(0).start();
	}

	@Override
	public void onTick() {
		super.onTick();
		
		Animation currentAnimation = graveAnimation.get(0);
		// if the grave animation is at its end stop it and idle

		// if its night time and the grave is idle then start the digout
		// animation
		if (World.timer.isDay() == 0 ) {
			// if it hasnt spawned a zombie
			if (spawnVar == Boolean.FALSE) {
				percentSpawn = (int) (Math.random() * 100);
				enrage =  (int) (Math.random() * 100);
				spawnVar = Boolean.TRUE;
			}
			if (spawnZombie == Boolean.FALSE){
				if (currentAnimation == idle && spawnZombie == Boolean.FALSE) {
					if (percentSpawn < 40) {
						graveAnimation.set(0, digGraveOutAnimation);
						graveAnimation.get(0).start();
					} else {
						spawnZombie = Boolean.TRUE;
					}
				}
				// if the animation of dig out is at its end spawn a zombie
				if (currentAnimation == digGraveOutAnimation
						&& (currentAnimation.getCurrentFrame() + 1) == digGraveOutAnimation
								.getTotalFrames()) {
					graveAnimation.get(0).stop();
					if (enrage < 50) {
						World.getInstance().addEntityToWorld(
								new ZombieMob(this.getXpos(), this.getYpos(),
										100, 42, 42));
					} else {
						World.getInstance().addEntityToWorld(
								new EnragedZombieMob(this.getXpos(), this.getYpos(),
										100, 42, 42));
					}
					spawnZombie = Boolean.TRUE;
					graveAnimation.set(0, idle);
				}
			}
		} else if(!(World.timer.isDay() == 0)){
			if (currentAnimation == spawnGrave
					&& (currentAnimation.getCurrentFrame() + 1) == spawnGrave.getTotalFrames()) {
				graveAnimation.set(0, idle);
			}
			if (spawnZombie == Boolean.TRUE) {
				spawnZombie = Boolean.FALSE;
				spawnVar = Boolean.FALSE;
			}
		}
		graveAnimation.get(0).start();
		graveAnimation.get(0).update();
		setAnimation(graveAnimation);
	}

	@Override
	public int getHealth() {
		return this.health;
	}

	@Override
	public Boolean isDead() {
		// TODO Auto-generated method stub
		return this.dead;
	}

	@Override
	public void subtractHealth(int factor) {
		this.health -= factor;
	}

	@Override
	public void setHealth(int health) {
		this.health = health;
	}
}