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
		for( int j = 0; j < height; j++ ) {
			for( int i = 0; i < width; i++ ) {
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
	
	
	/*
	array[tk-1][tl-1] tk = tk-1; tl = tl-1;	array[tk-1][l] tk = tk-1;	array[tk-1][tl+1] tk = tk-1; tl = tl+1;
	
	array[tk][tl-1] tl = tl-1;				array[tk][tl]				array[tk][tl+1] tl = tl+1;
	
	array[tk+1][tl-1] tk = tk+1; tl = tl-1;	array[tk+1][tl]	tk = tk+1; 	array[tk+1][tl+1] tk = tk+1; tl = tl+1;
	*/
	
	/*
	 && tk-1 >= 0
	 && tk+1 < height
	 && tl-1 >= 0
	 && tl+1 < width
	 */

	public void recognizeElipse() {
		int tk  = 0;	// temporary value used in iterations
		int tl  = 0;	// temporary value used in iterations
		int ttk = 0;	// temporary value used in condition with last marked pixel
		int ttl = 0;	// temporary value used in condition with last marked pixel
		int tempTK = 0;	// temporary value used to store tk value;
		int tempTL = 0; // temporary value used to store tl value;
		boolean flag = false;	// flag indicating if the neighbor of currently parsed pixel is closing the shape
		
		int licz = 0;
		
		for ( int k = 0; k < height; k++ ) {
			for ( int l = 0; l < width; l++ ) {
				if ( array[k][l] == 1 ) {
					
					licz ++;
					
					int iter = 0;
					flag = false;
					
					tk = k;
					tl = l;
					
					System.out.println("Start parsowania: " + tl + ". " + tk);
					
					array[tk][tl] += 2;
					ttk = tk;	// one before last marked pixel
					ttl = tl;	// one before last marked pixel
					
					// mark second pixel to state the direction
					
					if ( tk-1 >= 0 && tl+1 < width && array[tk-1][tl+1] == 1 ) {
						array[tk-1][tl+1] += 2;
						tk = tk-1;
						tl = tl+1;
					}
					else if ( tl+1 < width && array[tk][tl+1] == 1 ) {
						array[tk][tl+1] += 2;
						tl = tl+1;
					}
					else if ( tk+1 < height && tl+1 < width && array[tk+1][tl+1] == 1 ) {
						array[tk+1][tl+1] += 2;
						tk = tk+1;
						tl = tl+1;
					}
					else if ( tk+1 < height && array[tk+1][tl] == 1 ) {
						array[tk+1][tl] += 2;
						tk = tk+1;
					}
					else if ( tk+1 < height && tl-1 >= 0 && array[tk+1][tl-1] == 1 ) {
						array[tk+1][tl-1] += 2;
						tk = tk+1;
						tl = tl-1;
					
					}
					
					// higher probability could be made by parsing one before last frame
					
					do {
						tempTK = tk;		// store index of currently parsed pixel for future use as one before last parsed pixel
						tempTL = tl;
						
						// Check if current pixel(tk,tl) is the neighbor of start pixel (k,l)
						// iter > 1 is needed due to the fact that lastly parsed pixel was start pixel
						if ( iter > 1 && (( tk-1 == k && tl-1 == l ) ||
								 ( tk-1 == k && tl == l ) ||
								 ( tk-1 == k && tl+1 == l ) ||
								 ( tk == k && tl+1 == l ) ||
								 ( tk+1 == k && tl+1 == l ) ||
								 ( tk+1 == k && tl == l ) ||
								 ( tk+1 == k && tl-1 == l ) ||
								 ( tk == k && tl - 1 == l ))) {
								flag = true;
								System.out.println("Zamknieto figure przed czasem: " + tl + ", " +  tk);
								break;
							}
						
						// Upper left
						if ( ttk == tk -1 && ttl == tl -1) {
							if ( tk+1 < height && tl+1 < width && array[tk+1][tl+1] == 1  ) {	// Lower right
								array[tk+1][tl+1] += 2;
								tk = tk+1;
								tl = tl+1;
							}
							else if ( tl+1 < width && array[tk][tl+1] == 1 && tl+1 < width ) {	// Right
								array[tk][tl+1] += 2;
								tl = tl+1;
							}
							else if ( tk+1 < height && array[tk+1][tl] == 1 ) {					// Lower
								array[tk+1][tl] += 2;
								tk = tk+1;
							}
							else {
								System.out.println("UL ZGUBILEM SIE");
							}
						}
						
						// Upper
						else if ( ttk == tk-1 && ttl == tl ) {
							if ( tk+1 < height && array[tk+1][tl] == 1 ) {							// Lower		
								array[tk+1][tl] += 2;
								tk = tk+1;
							}
							else if ( tk+1 < height && tl-1 >= 0 && array[tk+1][tl-1] == 1 ) {		// Lower left
								array[tk+1][tl-1] += 2;
								tk = tk+1;
								tl = tl-1;
							}
							else if ( tk+1 < height && tl+1 < width && array[tk+1][tl+1] == 1  ) {	// Lower right
								array[tk+1][tl+1] += 2;
								tk = tk+1;
								tl = tl+1;
							}
							else if ( tl-1 >= 0 && array[tk][tl-1] == 1 ) {							// Left
								array[tk][tl-1] += 2;
								tl = tl-1;
							}
							else if ( tl+1 < width && array[tk][tl+1] == 1 ) {						// Right
								array[tk][tl+1] += 2;
								tl = tl+1;
							}
							else {
								System.out.println("U ZGUBILEM SIE");
							}
							
						}
						
						// Upper right
						else if ( ttk == tk-1 && ttl == tl + 1) {
							if ( tk+1 < height && tl-1 >= 0 && array[tk+1][tl-1] == 1 ) {	// Lower left
								array[tk+1][tl-1] += 2;
								tk = tk+1;
								tl = tl-1;
							}
							else if ( array[tk][tl-1] == 1&& tl-1 >= 0  ) {					// Left
								array[tk][tl-1] += 2;
								tl = tl-1;
							}
							else if ( tk+1 < height && array[tk+1][tl] == 1 ) {				// Lower
								array[tk+1][tl] += 2;
								tk = tk+1;
							}
							else {
								System.out.println("UR ZGUBILEM SIE");
							}
						}
						
						// Right
						else if ( ttk == tk  && ttl == tl+1 ) {
							if ( tl-1 >= 0 && array[tk][tl-1] == 1 ) {							// Left
								array[tk][tl-1] += 2;
								tl = tl-1;
							}
							else if ( tk-1 >= 0 && tl-1 >= 0 && array[tk-1][tl-1] == 1 ) {		// Upper left
								array[tk-1][tl-1] += 2;
								tk = tk-1;
								tl = tl-1;
							}
							else if ( tk+1 < height && tl-1 >= 0 && array[tk+1][tl-1] == 1 ) {	// Lower left
								array[tk+1][tl-1] += 2;
								tk = tk+1;
								tl = tl-1;
							}
							else if ( tk-1 >= 0 && array[tk-1][tl] == 1  ) {					// Upper
								array[tk-1][tl] += 2;
								tk = tk-1;
							}
							else if ( tk+1 < height && array[tk+1][tl] == 1 ) {					// Lower
								array[tk+1][tl] += 2;
								tk = tk+1;
							}
							else {
								System.out.println("R ZGUBILEM SIE");
							}
						}

						// Lower right
						else if ( ttk == tk+1 && ttl == tl+1 ) {
							if ( tk-1 >= 0 && tl-1 >= 0 && array[tk-1][tl-1] == 1) {	// Upper left
								array[tk-1][tl-1] += 2;
								tk = tk-1;
								tl = tl-1;
							}
							else if ( tl-1 >= 0 && array[tk][tl-1] == 1 ) {				// Left
								array[tk][tl-1] += 2;
								tl = tl-1;
							}
							else if ( tk-1 >= 0 && array[tk-1][tl] == 1  ) {			// Upper
								array[tk-1][tl] += 2;
								tk = tk-1;
							}
							else {
								System.out.println("LR ZGUBILEM SIE " + tk + ", " + tl + ", " + array[tk-1][tl]);
							}
						}

						// Lower
						else if ( ttk == tk+1 && ttl == tl ) {
							if ( tk-1 >= 0 && array[tk-1][tl] == 1  ) {							// Upper
								array[tk-1][tl] += 2;
								tk = tk-1;
							}
							else if ( tk-1 >= 0 && tl-1 >= 0 && array[tk-1][tl-1] == 1 ) {		// Upper left
								array[tk-1][tl-1] += 2;
								tk = tk-1;
								tl = tl-1;
							}
							else if ( tk-1 >= 0 && tl+1 < width && array[tk-1][tl+1] == 1 ) { 	// Upper right
								array[tk-1][tl+1] += 2;
								tk = tk-1;
								tl = tl+1;
							}
							else if ( tl-1 >= 0 && array[tk][tl-1] == 1 ) {						// Left
								array[tk][tl-1] += 2;
								tl = tl-1;
							}
							else if ( tl+1 < width && array[tk][tl+1] == 1 ) {					// Right
								array[tk][tl+1] += 2;
								tl = tl+1;
							}
							else {
								System.out.println("Lo ZGUBILEM SIE");
							}
						}

						// Lower left
						else if ( ttk == tk+1 && ttl == tl-1 ) {
							if ( tk-1 >= 0 && tl+1 < width && array[tk-1][tl+1] == 1 ) { 	// Upper right
								array[tk-1][tl+1] += 2;
								tk = tk-1;
								tl = tl+1;
							}
							else if ( tk-1 >= 0 && array[tk-1][tl] == 1  ) {				// Upper
								array[tk-1][tl] += 2;
								tk = tk-1;
							}
							else if ( tl+1 < width && array[tk][tl+1] == 1 ) {				// Right
								array[tk][tl+1] += 2;
								tl = tl+1;
							}
							else {
								System.out.println("LL ZGUBILEM SIE");
							}
						}

						// Left
						else if ( ttk == tk && ttl == tl-1 ) {
							if ( tl+1 < width && array[tk][tl+1] == 1 ) {							// Right
								array[tk][tl+1] += 2;
								tl = tl+1;
							}
							else if ( tk-1 >= 0 && tl+1 < width && array[tk-1][tl+1] == 1 ) { 		// Upper right
								array[tk-1][tl+1] += 2;
								tk = tk-1;
								tl = tl+1;
							}
							else if ( tk+1 < height && tl+1 < width && array[tk+1][tl+1] == 1  ) {	// Lower right
								array[tk+1][tl+1] += 2;
								tk = tk+1;
								tl = tl+1;
							}
							else if ( tk-1 >= 0 && array[tk-1][l] == 1  ) {							// Upper
								array[tk-1][tl] += 2;
								tk = tk-1;
							}
							else if ( tk+1 < height && array[tk+1][tl] == 1 ) {						// Lower
								array[tk+1][tl] += 2;
								tk = tk+1;
							}
							else {
								System.out.println("L ZGUBILEM SIE " + tl + ", " + tk + ", " + (tk-1) + ", " + array[tk-1][tl]);
							}
						}
						else {
							if ( iter == 13) {
							System.out.println("CENTRAL ZGUBILEM SIE " + tl + ", " + tk + " ttk= " + ttk + " ttl= " + ttl);
							}
						}
						
						ttk = tempTK;
						ttl = tempTL;
						
						iter++;
						
						/*if ( iter > 1 && (tk == k || tk+1 == k || tk-1 == k) && (tl == l || tl-1 == l || tl+1 ==l) ) {
							flag = true;
							System.out.println("Zamknieto figure przed czasem");
						}*/
						

						
					}
					while ( flag != true && iter < 100);		// zakladam, ze robimy okrazenie i wyladujemy w tym samym punkcie, iter mozna bedzie wyrzucic
					System.out.println("Koniec parsowania: " + tl + ". " + tk);
				}
			}
		}
		for (int j = 0; j < pa.getWidth(); j++) {
			System.out.println();
		    for (int k = 0; k < pa.getHeight(); k++) {
		        System.out.print(array[j][k]);
		    }
		}
		System.out.println("\nRozpoczeto parsowanie: " + licz + " razy.");
	}
}
