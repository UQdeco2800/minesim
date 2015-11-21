package minesim.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

public class Animation {

	// Logger which will display a message a based on the error
	private static final Logger LOGGER = LoggerFactory.getLogger(Animation.class);

	// Error messages
	private static final String NULLFRAMES = "You cannot have null frames.";
	private static final String INVDURATION = "You cannot have a duration equal to or less than 0.";

	// Number of animation frames
	private int frameCount;

	// The length of time between frames (animation speed)
	private int frameDelay;

	// The current frame
	private int currentFrame;

	// The total number of frames
	private int totalFrames;

	// Direction of the sprite animation
	private int animationDirection;

	// True when animation is paused or finished
	private boolean stopped;

	// ArrayList of sprite frames
	private List<Frame> frames = new ArrayList<Frame>();

	/**
	 * Sets up an animation variable.
	 *
	 * @param frames
	 *            The specified sprite frames
	 * @param frameDelay
	 *            The length of time between frames
	 */
	public Animation(List<Image> frames, int frameDelay) {
		this.frameDelay = frameDelay;
		stopped = true;

		try {
			for (int i = 0; i < frames.size(); i++) {
				addFrame(frames.get(i), frameDelay);
			}
		} catch (NullPointerException npe) {
			LOGGER.error(NULLFRAMES, npe);
		}

		frameCount = 0;
		currentFrame = 0;
		animationDirection = 1;
		totalFrames = this.frames.size();
	}

	/**
	 * Begin the animation. Start only if it hasn't started already and frames
	 * exist.
	 */
	public void start() {
		if (stopped && !frames.isEmpty()) {
			stopped = false;
		}
	}

	/**
	 * Stop the animation. Stop only if it hasn't been stopped already and
	 * frames exist.
	 */
	public void stop() {
		if (!stopped && !frames.isEmpty()) {
			stopped = true;
		}
	}

	/**
	 * Restart the animation from the beginning if frames exist.
	 */
	public void restart() {
		if (!frames.isEmpty()) {
			stopped = false;
			currentFrame = 0;
		}
	}

	/**
	 * Reset the current frame, frame count and stop(or pause) animation.
	 */
	public void reset() {
		stopped = true;
		frameCount = 0;
		currentFrame = 0;
	}

	/**
	 * Gets the total number of frames.
	 *
	 * @return the total number of frames
	 */
	public int getTotalFrames() {
		return totalFrames;
	}

	/**
	 * Gets the frame count.
	 *
	 * @return the frame count
	 */
	public int getFrameCount() {
		return frameCount;
	}

	/**
	 * Determines whether the animation has stopped or not.
	 *
	 * @return a boolean value where true is stopped, and false is running
	 */
	public boolean getStopped() {
		return stopped;
	}

	/**
	 * Gets the current frame.
	 *
	 * @return the current frame
	 */
	public int getCurrentFrame() {
		return currentFrame;
	}

	/**
	 * Gets the frame delay.
	 *
	 * @return the frame delay
	 */
	public int getFrameDelay() {
		return frameDelay;
	}

	/**
	 * Gets the current sprite.
	 *
	 * @return an image of the current animation frame
	 */
	public Image getCurrentSprite() {
		return frames.get(currentFrame).getFrame();
	}

	/**
	 * Gets the animation direction for traversing the sprite sheet.
	 *
	 * @return the animation direction (1 is going left to right).
	 */
	public int getAnimationDirection() {
		return animationDirection;
	}

	/**
	 * Sets the animation direction.
	 *
	 * @param direction
	 *            the new direction which the animation will travel in
	 */
	public void setAnimationDirection(int direction) {
		animationDirection = direction;
	}

	/**
	 * Gets the frames of the animation.
	 *
	 * @return a copy of the animation frames
	 */
	public List<Frame> getFrames() {
		List<Frame> returnFrame = new ArrayList<>();
		for (Frame f : frames) {
			returnFrame.add(f);
		}
		return returnFrame;
	}

	/**
	 * A private function which increases the number of animation frames.
	 *
	 * @param frame
	 *            The specified sprite frame you want to add
	 * @param duration
	 *            The length of time between frames
	 */
	private void addFrame(Image frame, int duration) {
		if (duration > 0) {
			frames.add(new Frame(frame, duration));
			currentFrame = 0;
		} else {
			LOGGER.error(INVDURATION);
		}
	}

	/**
	 * Update the frames.
	 */
	public void update() {
		if (!stopped) {
			frameCount++;

			if (frameCount > frameDelay) {
				frameCount = 0;
				currentFrame += animationDirection;

				if (currentFrame > totalFrames - 1) {
					currentFrame = 0;
				} else if (currentFrame < 0) {
					currentFrame = totalFrames - 1;
				}
			}
		}
	}
}