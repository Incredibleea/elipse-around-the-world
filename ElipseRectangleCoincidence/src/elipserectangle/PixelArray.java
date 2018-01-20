package elipserectangle;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Klasa odpowiedzialna za odczytywanie pixeli z pliku. Tworzona jest tablica dwuwymiarowa, w ktorej
 * 1 oznaczaja brzegi figur, a 0 puste miejsca.
 * 
 * @author Wojciech Nowak, Dawid Gadomski
 *
 */
public class PixelArray {
	public static PixelArray instance = new PixelArray();
	private int[][] pixelArray;
	private int width;
	private int height;
	
	/**
	 * Konstruktor. Wybierana jest tutaj nazwa wczytywanego pliku, a nastepnie jest on 
	 * przetwarzany i wpisywany do tablicy.
	 */
	private PixelArray() {
		try {
			Image image = ImageIO.read(new File("pictures/06.bmp"));
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
	
	/**
	 * Metoda zwraca instancje klasy.
	 * 
	 * @return instancja klasy
	 */
	public static PixelArray getInstance() {
		return instance;
	}
	
	/**
	 * Metoda zwraca tablice.
	 * 
	 * @return dwuwymiarowa tablice zapisanych pixeli
	 */
	public int[][] getPixelArray() {
		return this.pixelArray;
	}
	
	/**
	 * Metoda odczytuje szerokosc.
	 *  
	 * @return szerokosc
	 */
	public int getWidth() {
		return this.width;
	}
	
	/**
	 * Metoda odczytuje wysokosc.
	 * 
	 * @return wysokosc
	 */
	public int getHeight() {
		return this.height;
	}
	
	/**
	 * Metoda wypisuje wczytane dane z pliku.
	 */
	public void printPixelArray() {
		for (int j = 0; j < width; j++) {
			System.out.println();
		    for (int k = 0; k < height; k++) {
		        System.out.print(pixelArray[j][k]);
		    }
		}
	}
}