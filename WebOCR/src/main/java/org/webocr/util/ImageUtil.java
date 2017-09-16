package org.webocr.util;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.imageio.ImageIO;

/**
 * 
 * This class contains all the image manipulation functionalities.
 * 
 * @author Saminda Peramuna
 * @since 15/09/2017
 *
 */
public class ImageUtil {

    private static final String TEMP_FILE_PATH = "temp.png";
    private static final String FORMAT_NAME = "PNG";

    /**
     * Crop an image with the given rectangle.
     * 
     * @param src Image source.
     * @param rect Rectangle to crop.
     * @return
     */
    public static BufferedImage cropImage(BufferedImage src, Rectangle rect) {
	BufferedImage dest = src.getSubimage(rect.x, rect.y, rect.width, rect.height);

	return dest;
    }

    /**
     * Crop an image with the given dimensions.
     * 
     * @param src Image source.
     * @param x Starting x coordinate.
     * @param y Starting y coordinate.
     * @param width Width of the crop area.
     * @param height Height of the crop area.
     * @return
     */
    public static BufferedImage cropImage(BufferedImage src, int x, int y, int width, int height) {
	Rectangle rect = new Rectangle(x, y, width, height);

	return cropImage(src, rect);
    }

    /**
     * Convert from Base64Image to a Byte array.
     * 
     * @param base64Image
     * @return
     */
    public static byte[] getImageFromBase64(String base64Image) {
	Decoder decoder = Base64.getDecoder();

	return decoder.decode(base64Image);
    }

    /**
     * Get a BuffredImage from a byte array of an image.
     * 
     * @param image The byte array containing the image.
     * @return
     * @throws IOException
     */
    public static BufferedImage getBufferedImage(byte[] image) throws IOException {
	ByteArrayInputStream bis = new ByteArrayInputStream(image);
	BufferedImage bufferedImage = null;

	bufferedImage = ImageIO.read(bis);

	return bufferedImage;
    }

    /**
     * Writes an image to a temporary file to obtain a file handle.
     * 
     * @param img Image to be saved.
     * @return
     * @throws IOException
     */
    public static File writeToTempFile(BufferedImage img) throws IOException {
	File file = new File(TEMP_FILE_PATH);
	ImageIO.write(img, FORMAT_NAME, file);

	return file;
    }
}
