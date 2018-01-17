package elipserectangle;

import java.util.ArrayList;
import java.util.List;

/*
 * Recognizer class is responsible for recognize shapes
 */

class Point {
	int k = 0;
	int l = 0;
	Point ( int k, int l) {
		this.k = k;
		this.l = l;
	}
	
	public void printPoint() {
		System.out.print("(" + k + "," + l + ")");
	}
}

class Rectangle {
	Point upperLeftCorner  = null;
	Point lowerRightCorner = null;
	
	Rectangle( Point a, Point b ) {
		this.upperLeftCorner = a;
		this.lowerRightCorner = b;
	}
}

public class Recognizer {
	private PixelArray pa = PixelArray.getInstance();
	private int[][] array = pa.getPixelArray();
	private int width = pa.getWidth();
	private int height = pa.getHeight();
	private List<Point> ul;						// list of upper left corners
	private List<Point> ur;						// list of upper right corners
	private List<Point> ll;						// list of lower left corners
	private List<Point> lr;						// list of lower right corners
	
	private List<Rectangle> rectangles;			// list of found rectangles
	
	public Recognizer() {
		ul = new ArrayList<>();
		ur = new ArrayList<>();
		ll = new ArrayList<>();
		lr = new ArrayList<>();
		
		rectangles = new ArrayList<>();
	}
	
	public void recognizeRectangle() {
		for( int j = 0; j < pa.getWidth(); j++ ) {
			for( int i = 0; i < pa.getHeight(); i++ ) {
				isUpperLeftCorner(i,j);
				isUpperRightCorner(i,j);
				isLowerLeftCorner(i,j);
				isLowerRightCorner(i,j);
			}
		}
		markEdges();
	}
	
	private void isUpperLeftCorner(int k, int l) {
		if ( array[k][l] == 1 
		     && k+2 < height && array[k+1][l] == 1 && array[k+2][l] == 1
		     && l+2 < width  && array[k][l+1] == 1 && array[k][l+2] == 1 ) {
			ul.add(new Point(k,l));
		}
	}
	
	private void isUpperRightCorner(int k, int l) {
		if ( array[k][l] == 1
			 && k+2 < height && array[k+1][l] == 1 && array[k+2][l] == 1
			 && l-2 >= 0      && array[k][l-1] == 1 && array[k][l-2] == 1) {
			ur.add(new Point(k,l));
		}
	}
	
	private void isLowerLeftCorner(int k, int l) {
		if ( array[k][l] == 1
			 && k-2 >= 0     && array[k-1][l] == 1 && array[k-2][l] == 1
			 && l+2 < width && array[k][l+1] == 1 && array[k][l+2] == 1) {
			ll.add(new Point(k,l));
		}
	}

	private void isLowerRightCorner(int k, int l) {
		if ( array[k][l] == 1
			 && k-2 >= 0     && array[k-1][l] == 1 && array[k-2][l] == 1
			 && l-2 >= 0     && array[k][l-1] == 1 && array[k][l-2] == 1) {
			lr.add(new Point(k,l));
		}
	}
	
	private void markEdges() {
		
		int currentRect = 0;
		
		for ( int i = 0 ; i < ul.size(); i++) {
			Point pul = ul.get(i);
			Point pll;
			//System.out.println("\nParsing: ");
			//pul.printPoint();
			int ill = findL(ll, pul.l);
			if( ill != -1 ) {
				pll = ll.get(ill);
				
				List<Integer> iur = findK(ur, pul.k);		// to powinno zwracac tablice wszystkich znalezionych
				List<Integer> ilr = findK(lr, pll.k);
				
				for ( int x = 0; x < iur.size(); x++ ) {
					for ( int y = 0; y < ilr.size(); y++ ) {
						Point pur = ur.get(iur.get(x));
						Point plr = lr.get(ilr.get(y));
						if ( pur.l == plr.l ) {
							// debugging, remove.
							System.out.println("\nZnaleziono prostokat");
							pul.printPoint();
							pur.printPoint();
							pll.printPoint();
							plr.printPoint();
							
							rectangles.add(new Rectangle(pul, plr));
							System.out.println(currentRect);
							
							colorRectangle(rectangles.get(currentRect));
							if (currentRect == 3) moveRectangle(rectangles.get(currentRect), 20, -20);
							currentRect+=1;
							
							ul.remove(i);
							ur.remove((int)iur.get(x));
							iur.remove(x);
							ll.remove(ill);
							lr.remove((int)ilr.get(y));
							ilr.remove(y);
							i--;
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Funkcja "kolorujaca" prostokat (1 -> 2)
	 * @param r
	 */
	private void colorRectangle( Rectangle r ) {
		
		for( int j = 0; j <= r.lowerRightCorner.l - r.upperLeftCorner.l; j++ ) {
			for( int i = 0; i <= r.lowerRightCorner.k - r.upperLeftCorner.k; i++ ) {
				if (array[r.upperLeftCorner.k+i][r.upperLeftCorner.l+j] == 1) array[r.upperLeftCorner.k+i][r.upperLeftCorner.l+j] += 1;
			}
		}

//		for (int i = 0; i <= r.lowerRightCorner.k - r.upperLeftCorner.k; i++) {
//			array[r.upperLeftCorner.k+i][r.upperLeftCorner.l] += 1;
//			array[r.lowerRightCorner.k-i][r.lowerRightCorner.l] += 1;
//		}
//		
//		for (int i = 1; i < r.lowerRightCorner.l - r.upperLeftCorner.l; i++) {
//			array[r.upperLeftCorner.k][r.upperLeftCorner.l+i] += 1;
//			array[r.lowerRightCorner.k][r.lowerRightCorner.l-i] += 1;
//		}
		
//		for (int j = 0; j < width; j++) {
//			System.out.println();
//		    for (int k = 0; k < height; k++) {
//		        System.out.print(array[j][k]);
//		    }
//		}
	}
	
	/**
	 * Metoda do przesuwania prostokata
	 * 
	 * @param r obiekt przesuwanego prostokata, moze trzeba bedzie dodac jakis parametr do obiektu, ktory go zidentyfikuje?
	 * @param moveX przesuniecie o x
	 * @param moveY przesuniecie o y
	 */
	private void moveRectangle( Rectangle r, int moveX, int moveY ) {
		
		if (r.upperLeftCorner.l + moveY > 99 || r.upperLeftCorner.l + moveY < 0 || r.lowerRightCorner.l + moveY > 99 || r.lowerRightCorner.l + moveY < 0) {
			System.out.println("WARNING! Cannot move " + r + "! Index Y out of band!");
		} else if (r.upperLeftCorner.k + moveX > 99 || r.upperLeftCorner.k + moveX < 0 || r.lowerRightCorner.k + moveX > 99 || r.lowerRightCorner.k + moveX < 0) {
			System.out.println("WARNING! Cannot move " + r + "! Index X out of band!");
		} else {
			for( int j = 0; j <= r.lowerRightCorner.l - r.upperLeftCorner.l; j++ ) {
				for( int i = 0; i <= r.lowerRightCorner.k - r.upperLeftCorner.k; i++ ) {
					array[r.upperLeftCorner.k+i+moveX][r.upperLeftCorner.l+j+moveY] += array[r.upperLeftCorner.k+i][r.upperLeftCorner.l+j];
					array[r.upperLeftCorner.k+i][r.upperLeftCorner.l+j] = 0;
				}
			}
		}
		
		for (int j = 0; j < width; j++) {
			System.out.println();
		    for (int k = 0; k < height; k++) {
		        System.out.print(array[j][k]);
		    }
		}
	}
	
	private List<Integer> findK( List<Point> l, int k) {
		List<Integer> val = new ArrayList<>();
		for( int i = 0; i < l.size(); i++) {
			if ( l.get(i).k == k ) {
				val.add(i);
			}
		}
		//System.out.println("\nNie znaleziono k: " + k);
		return val;
	}
	
	private int findL( List<Point> lst, int l) {
		for( int i = 0; i < lst.size(); i++) {
			if ( lst.get(i).l == l ) {
				return i;
			}
		}
		System.out.println("Nie znaleziono l: " + l);
		return -1;
	}
	
	private void printList(List<Point> l) {
		System.out.println();
		for ( Point p : l ) {
			p.printPoint();
		}
	}
	
	public void print() {
		/*for (int j = 0; j < pa.getWidth(); j++) {
			System.out.println();
		    for (int k = 0; k < pa.getHeight(); k++) {
		        System.out.print(array[j][k]);
		    }
		}*/
		System.out.println("\nFound unused UL corners");
		printList(ul);
		System.out.println("Found unused UR corners");
		printList(ur);
		System.out.println("Found unused LL corners");
		printList(ll);
		System.out.println("Found unused LR corners");
		printList(lr);
	}

	public void recognizeElipse() {
		/* okno 3x3
		[k-1][l-1]	[k-1][l]	[k-1][l+1]
		
		[k]  [l-1]	[k]  [l]	[k]  [l+1]
		
		[k+1][l-1]	[k+1][l]	[k+1][l+1]
		*/
	}
}
