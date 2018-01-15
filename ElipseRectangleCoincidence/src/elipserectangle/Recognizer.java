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
		
	}
	
	/*
	private boolean isRectangle( int k, int l ) {
		int hght = 0;
		int wdth = 0;
		// upper edge
		for( int i = k; i < pa.getWidth(); i++ ) {
			if ( isUpperRightCorner(i,l) ) {
				hght++;
				break;
			}			
			if ( array[i][l] == 1 )
				hght++;
			else
				break;
		}
		
		// left edge
		for( int j = l; j < pa.getHeight(); j++ ) {
			if( isLowerLeftCorner(k,j) ) {
				wdth++;
				break;
			}
			if ( array[k][j] == 1 ) {
				wdth++;
			}
			else
				break;
		}
		
		if ( hght >= 2 && wdth >= 2 ) {
			// mark horizontal edges
			for( int i = k; i < (k + hght); ++i ) {
				array[i][l] 	 = 2;
				array[i][l+wdth-1] = 2;
			}
			// mark vertical edges
			for( int i = l; i < (l+wdth); ++i ) {
				array[k+hght-1][i] = 2;
				array[k][i] 	 = 2;
			}
			
			System.out.println("height: " + hght + ", weigth: " + wdth);
			return true;
		}
		else
			return false;
	}
	*/
	
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
}
