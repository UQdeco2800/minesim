package minesim.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.image.Image;

public class Frame {

	// Logger which will display a message a based on the error
	private static final Logger LOGGER = LoggerFactory.getLogger(Frame.class);

	// Error messages
	private static final String NULLFRAMES = "You cannot have null frames.";
	private static final String OUTOFBOUNDS = "You are out of bounds, please make the duration greater than zero.";
	private static final String INVDURATION = "You cannot have a duration equal to or less than 0.";

	// Time between this frame and the next
	private int duration;

	// The frame of a specific Sprite
	private Image frameOfSprite;

	/**
	 * Sets up a frame variable.
	 *
	 * @param sprite
	 *            The image that you want for that specific frame
	 * @param duration
	 *            The length of time between frames
	 */
	public Frame(Image sprite, int duration) {
		try {
			if (sprite == null) {
				throw new NullPointerException();
			} else if (duration <= 0) {
				throw new IllegalArgumentException(OUTOFBOUNDS);
			} else {
				frameOfSprite = sprite;
				this.duration = duration;
			}
		} catch (NullPointerException e) {
			LOGGER.error(NULLFRAMES, e);
		} catch (IllegalArgumentException e) {
			LOGGER.error(INVDURATION, e);
		}
	}

	/**
	 * Retrieve the sprite.
	 *
	 * @return Returns the frame's sprite
	 */
	public Image getFrame() {
		return frameOfSprite;
	}

	/**
	 * Changes the frame's sprite.Throws a NullPointerException if frame is
	 * null.
	 *
	 * @param frame
	 *            The sprite that you want to change to
	 */
	public void setFrame(Image frame) {
		try {
			if (frame == null) {
				throw new NullPointerException();
			} else {
				frameOfSprite = frame;
			}
		} catch (NullPointerException e) {
			LOGGER.error(NULLFRAMES, e);
		}
	}

	/**
	 * Retrieve the frame duration.
	 *
	 * @return The length of time between frames
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Change the frame duration. Throws IllegalArgumentException if duration
	 * is less than 0.
	 *
	 * @param duration
	 *            The length of time between frames you want to change
	 */
	public void setDuration(int duration) {
		try {
			if (duration <= 0) {
				throw new IllegalArgumentException(OUTOFBOUNDS);
			} else {
				this.duration = duration;
			}
		} catch (IllegalArgumentException e) {
			LOGGER.error(INVDURATION, e);
		}
	}

}
