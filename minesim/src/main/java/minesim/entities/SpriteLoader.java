package minesim.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class SpriteLoader {
	// Logger which will display a message a based on the error
	private static final Logger LOGGER = LoggerFactory.getLogger(SpriteLoader.class);

	// Error messages
	private static final String INVFILE = "Could not load file.";
	private static final String INVGRID = "You are requesting a grid position "
			+ "outside of your sprite sheet.";
	private static final String NULLSPRITE = "You are trying to use a method "
			+ "which does not have a valid loaded spritesheet.";
	private static final String DIMERROR = "Invalid sprite sheet dimensions, "
			+ "make sure your file is a multiple of your specified sprite size.";
	private static final String INVDIM = "Your file is not a multiple of your "
			+ "specified sprite size, your sprites will not load correctly.";

	// The returned sprite image users will see
	private Image returnedSpriteImage;

	// The sprite sheet where the displayed sprite comes from
	private BufferedImage spriteSheet;

	// Sprite dimensions (Increase this if you want higher quality sprites)
	private int spriteSizeX;
	private int spriteSizeY;

	/**
	 * Loads a user specified sprite sheet.
	 *
	 * @param file
	 *            The name of the sprite sheet that you want to load.
	 * @param x
	 *            The number of pixels horizontally
	 * @param y
	 *            The number of pixels vertically
	 */
	public void loadSpriteSheet(String file, int x, int y) {
		try {
			URL fileURL = getClass().getResource("/" + file + ".png");
			if (fileURL == null) {
				LOGGER.error(INVFILE);
			} else {
				File fileObj = new File(fileURL.toURI());
				this.spriteSheet = ImageIO.read(fileObj);
			}
			this.spriteSizeX = x;
			this.spriteSizeY = y;
		} catch (IOException e) {
			LOGGER.error(INVFILE, e);
		} catch (URISyntaxException e) {
			LOGGER.error(INVFILE, e);
		}
	}

	/**
	 * Retrieves a specific sprite.
	 *
	 * @param xGrid
	 *            The x position of the sprite on the sprite sheet
	 * @param yGrid
	 *            The y position of the sprite on the sprite sheet
	 * @return Returns the sprite at the coordinates that you specify as an
	 *         image
	 */
	public Image getSprite(int xGrid, int yGrid) {
		try {
			returnedSpriteImage = SwingFXUtils.toFXImage(
					spriteSheet.getSubimage(xGrid * spriteSizeX, yGrid * spriteSizeY, spriteSizeX, spriteSizeY), null);

			// dimension check
			if (!(spriteSheet.getWidth() % spriteSizeX == 0) || !(spriteSheet.getHeight() % spriteSizeY == 0)) {
				throw new IllegalArgumentException(DIMERROR);
			}
		} catch (RasterFormatException e) {
			// Out of bounds dimensions
			LOGGER.error(INVGRID, e);
		} catch (NullPointerException e) {
			LOGGER.error(NULLSPRITE, e);
		} catch (IllegalArgumentException e) {
			LOGGER.warn(INVDIM, e);
		}
		return returnedSpriteImage;
	}
}
