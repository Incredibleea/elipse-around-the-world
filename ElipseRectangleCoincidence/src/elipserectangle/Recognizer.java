package elipserectangle;

import java.util.ArrayList;
import java.util.List;

/*
 * Recognizer class is responisble for recognize shapes
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

public class Recognizer {
	PixelArray pa = PixelArray.getInstance();
	int[][] array = pa.getPixelArray();
	int width = pa.getWidth();
	int height = pa.getHeight();
	List<Point> ul;
	List<Point> ur;
	List<Point> ll;
	List<Point> lr;
	
	public Recognizer() {
		ul = new ArrayList<>();
		ur = new ArrayList<>();
		ll = new ArrayList<>();
		lr = new ArrayList<>();
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
	}
	
	private void isUpperLeftCorner(int k, int l) {
		if ( k+1 < pa.getHeight() && l+1 < pa.getWidth() 
		     && array[k][l] == 1 
		     && k+2 <= height && array[k+1][l] == 1 && array[k+2][l] == 1
		     && l+2 <= width  &&array[k][l+1] == 1 && array[k][l+2] == 1 ) {
			ul.add(new Point(k,l));
			//array[k][l] += 1;
		}
	}
	
	private void isUpperRightCorner(int k, int l) {
		if ( array[k][l] == 1
			 && k+2 <= height && array[k+1][l] == 1 && array[k+2][l] == 1
			 && l-2 >= 0      && array[k][l-1] == 1 && array[k][l-2] == 1) {
			ur.add(new Point(k,l));
			//array[k][l] += 2;
		}
	}
	
	private void isLowerLeftCorner(int k, int l) {
		if ( array[k][l] == 1
			 && k-2 >= 0     && array[k-1][l] == 1 && array[k-2][l] == 1
			 && l+2 <= width && array[k][l+1] == 1 && array[k][l+2] == 1) {
			ll.add(new Point(k,l));
			//array[k][l] += 3;
		}
	}

	private void isLowerRightCorner(int k, int l) {
		if ( array[k][l] == 1
			 && k-2 >= 0     && array[k-1][l] == 1 && array[k-2][l] == 1
			 && l-2 >= 0     && array[k][l-1] == 1 && array[k][l-2] == 1) {
			lr.add(new Point(k,l));
			//array[k][l] += 4;
		}
	}
	
	private void markEdges() {
		for ( int i = 0 ; i < ul.size(); i++) {
			Point pul = ul.get(i);
			int ill = findL(ll, pul.l);
			Point pll = ll.get(ill);
			int iur = findK(ur, pul.k);
			int ilr = findK(lr, pll.k);
			if ( iur != -1 && ilr != -1) {
				Point pur = ur.get(iur);
				Point plr = lr.get(ilr);
				if ( pur.l == plr.l ) {
					System.out.println("Znaleziono prostokat");
					pul.printPoint();
					pur.printPoint();
					pll.printPoint();
					plr.printPoint();
					ul.remove(i);
					ur.remove(iur);
					ll.remove(ill);
					lr.remove(ilr);
				}
			}
		}
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

	private int findK( List<Point> l, int k) {
		for( int i = 0; i < l.size(); i++) {
			if ( l.get(i).k == k ) {
				return i;
			}
		}
		return -1;
	}
	
	private int findL( List<Point> lst, int l) {
		for( int i = 0; i < lst.size(); i++) {
			if ( lst.get(i).l == l ) {
				return i;
			}
		}
		return -1;
	}
	
	private void printList(List<Point> l) {
		System.out.println();
		for ( Point p : l ) {
			p.printPoint();
		}
	}
	
	public void print() {
		for (int j = 0; j < pa.getWidth(); j++) {
			System.out.println();
		    for (int k = 0; k < pa.getHeight(); k++) {
		        System.out.print(array[j][k]);
		    }
		}
		printList(ul);
		printList(ur);
		printList(ll);
		printList(lr);
	}
}
