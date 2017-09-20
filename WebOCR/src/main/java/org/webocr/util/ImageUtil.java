package org.webocr.util;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
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
    private static final int RESCALE_FACTOR = 2;

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

    /**
     * Resize the image by the predefined resize factor.
     * 
     * @param image BufferedImage to be resized.
     * @return
     */
    public static BufferedImage upscaleImage(BufferedImage image) {
	int width = image.getWidth() * RESCALE_FACTOR;
	int height = image.getHeight() * RESCALE_FACTOR;

	BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

	Graphics2D graphics2D = scaledImage.createGraphics();
	graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	graphics2D.drawImage(image, 0, 0, width, height, null);
	graphics2D.dispose();

	return scaledImage;
    }

    /**
     * Sharpen an image.
     * 
     * @param image BufferedImage to be sharpened.
     * @return
     */
    public static BufferedImage sharpenImage(BufferedImage image) {
	Kernel kernal = new Kernel(3, 3, new float[] { -1, -1, -1, -1, 9, -1, -1, -1, -1 });

	BufferedImageOp op = new ConvolveOp(kernal);

	return op.filter(image, null);
    }
}
