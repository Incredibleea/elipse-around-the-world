package elipserectangle;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PixelArray {
	public static PixelArray instance = new PixelArray();
	private int[][] pixelArray;
	private int width;
	private int height;
	
	private PixelArray() {
		try {
			Image image = ImageIO.read(new File("pictures/04.bmp"));
			BufferedImage img = new BufferedImage(image.getWidth(null),
												  image.getHeight(null),
												  BufferedImage.TYPE_BYTE_GRAY);
			Graphics g = img.createGraphics();
			g.drawImage(image, 0, 0, null);
			g.dispose();
			
			Raster raster = img.getData();
			
			this.width  = img.getWidth();
			this.height = img.getHeight();
			
			pixelArray = new int[width][height];
			
			for (int j = 0; j < width; j++) {
			    for (int k = 0; k < height; k++) {
			        if ( raster.getSample(j, k, 0) > 0 )
			        	pixelArray[k][j] = 0;
			        else
			        	pixelArray[k][j] = 1;
			    }
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static PixelArray getInstance() {
		return instance;
	}
	
	public int[][] getPixelArray() {
		return this.pixelArray;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void printPixelArray() {
		for (int j = 0; j < width; j++) {
			System.out.println();
		    for (int k = 0; k < height; k++) {
		        System.out.print(pixelArray[j][k]);
		    }
		}
	}
}