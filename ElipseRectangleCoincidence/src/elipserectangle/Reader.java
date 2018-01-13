package elipserectangle;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Reader {

	public static void main( String[] args ) throws IOException {
		Image image = ImageIO.read(new File("picture.bmp"));
		BufferedImage img = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);
		
		Graphics g = img.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		
		Raster raster = img.getData();
		
		int width  = img.getWidth();
		int height = img.getHeight();
		
		int[][] pixelArray = new int[width][height];
		for (int j = 0; j < width; j++) {
		    for (int k = 0; k < height; k++) {
		        if ( raster.getSample(j, k, 0) > 0 )
		        	pixelArray[k][j] = 0;
		        else
		        	pixelArray[k][j] = 1;
		    }
		}
		
		for (int j = 0; j < width; j++) {
			System.out.println();
		    for (int k = 0; k < height; k++) {
		        System.out.print(pixelArray[j][k]);
		    }
		}
	}
}