package elipserectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa Point reprezentuje punkt x,y.
 * 
 * @author Wojciech Nowak, Dawid Gadomski
 */
class Point {
	int k = 0;
	int l = 0;
	
	/**
	 * Konstruktor.
	 * 
	 * @param k okresla polozenie x
	 * @param l okresla polozenie y
	 */
	Point ( int k, int l) {
		this.k = k;
		this.l = l;
	}
	
	/**
	 * Metoda wypisuje punkt.
	 */
	public void printPoint() {
		System.out.print("(" + k + "," + l + ")");
	}
}

/**
 * Klasa Rectangle reprezentuje prostokat jako jego gorny lewy rog oraz dolny prawy rog. 
 * 
 * @author Wojciech Nowak, Dawid Gadomski
 */
class Rectangle {
	Point upperLeftCorner  = null;
	Point lowerRightCorner = null;
	
	/**
	 * Konstruktor.
	 * 
	 * @param a gorny, lewy rog prostokata (x,y)
	 * @param b dolny, prawy rog prostokata (x,y)
	 */
	Rectangle( Point a, Point b ) {
		this.upperLeftCorner = a;
		this.lowerRightCorner = b;
	}
}

/**
 * Klasa Shape potrzebna do rozpoznawania elips.
 * 
 * @author Wojciech Nowak, Dawid Gadomski
 */
class Shape {
	public List<Point> lst;
	public int maxW;
	public int minW;
	public int maxH;
	public int minH;
 	
	/**
	 * Konstruktor.
	 */
 	Shape ( ) {
 		this.lst = new ArrayList<>();
 		this.maxH = 0;
 		this.maxW = 0;
 	}
 	
 	/**
 	 * Metoda dodaje punkt do listy.
 	 * 
 	 * @param k polozenie x
 	 * @param l polozenie y
 	 */
 	void add ( int k, int l ) {
		if ( k < this.minH )
			this.minH = k;
		else if ( k > this.maxH )
			this.maxH = k;
		else if ( l < this.minW )
			this.minW = l;
		else if ( l > this.maxW )
			this.maxW = l;
		
 		this.lst.add(new Point (k,l));
 	}
 	
 	/**
 	 * Wypisuje punkt.
 	 */
 	public void print() {
 		System.out.println();
 		for ( Point p : lst ) {
 			p.printPoint();
 		}
 	}
}

/**
 * Klasa Rocognizer zawiera glowne metody potrzebne do oznaczania oraz przesuwania elips i prostokatow.
 * 
 * @author Wojciech Nowak, Dawid Gadomski
 */
public class Recognizer {
	private PixelArray pa = PixelArray.getInstance();
	private int[][] array = pa.getPixelArray();
	private int width = pa.getWidth();
	private int height = pa.getHeight();
	private List<Point> ul;						// list of upper left corners
	private List<Point> ur;						// list of upper right corners
	private List<Point> ll;						// list of lower left corners
	private List<Point> lr;						// list of lower right corners
	
	public List<Rectangle> rectangles;			// list of found rectangles
	public List<Shape> elipses;
	public List<Shape> prostokaty;
	
	/**
	 * Konstruktor klasy Recognizer, w ktorym definiowane sa potrzebne listy.
	 */
	public Recognizer() {
		ul = new ArrayList<>();
		ur = new ArrayList<>();
		ll = new ArrayList<>();
		lr = new ArrayList<>();
		
		rectangles = new ArrayList<>();
		elipses    = new ArrayList<>();
		prostokaty = new ArrayList<>();
	}
	
	/**
	 * Metoda rozpoznajaca prostokaty.
	 */
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
	
	/**
	 * Metoda rozpoznajaca elipsy.
	 */
	public void recognizeElipse() {
		int tk  = 0;			// temporary value used in iterations
		int tl  = 0;			// temporary value used in iterations
		int ttk = 0;			// temporary value used in condition with last marked pixel
		int ttl = 0;			// temporary value used in condition with last marked pixel
		int tempTK = 0;			// temporary value used to store tk value;
		int tempTL = 0; 		// temporary value used to store tl value;
		boolean flag = false;	// flag indicating if the neighbor of currently parsed pixel is closing the shape
		
		int licz = 0;
		
		for ( int k = 0; k < height; k++ ) {
			for ( int l = 0; l < width; l++ ) {
				if ( array[k][l] == 1 ) {
					
					Shape e = new Shape();
					e.minH = height;
					e.minW = width;
					
					e.add(k, l);  			// add first point to ellipse
					
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
					
					e.add(tk, tl);
					
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
						
						e.add(tk, tl);
						
						iter++;
						
					}
					while ( flag != true && iter < 100);		// zakladam, ze robimy okrazenie i wyladujemy w tym samym punkcie, iter mozna bedzie wyrzucic
					System.out.println("Koniec parsowania: " + tl + ". " + tk);
					
					this.elipses.add(e);
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

	/**
	 * Metoda odpowiedzialna za zmiane polozenia prostokata.
	 * 
	 * @param r referencja do obiektu klasy Rectangle
	 * @param moveX przesuniecie o x
	 * @param moveY przesuniecie o y
	 */
	public void moveRectangle( Rectangle r, int moveX, int moveY ) {
		
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
	
	/**
	 * Metoda odpowiedzialna za zmiane polozenia elipsy.
	 * 
	 * @param s referencja do obiektu klasy Shape
	 * @param moveX przesuniecie o x
	 * @param moveY przesuniecie o y
	 */
 	public void moveElipse( Shape s, int moveX, int moveY ) {
		
 		if ( (s.minW + moveX < 0) || (s.maxW + moveX >= width) ||
 			 (s.minH + moveY < 0) || (s.maxH + moveY >= height) ) {
 			System.out.println("WARNING! Cannot move elipse");
 			return;
 		}
 		
 		for ( Point p : s.lst ) {
 			array[p.k][p.l] -= 3;
 			p.k += moveY;
			p.l += moveX;
			array[p.k][p.l] += 3;
		}
	}
	
 	/**
 	 * Metoda wykonuje operacje potrzebne do zaznaczenia i rozpoznania prostokatow.
 	 */
	private void markEdges() {
		
		int currentRect = 0;
		
		for ( int i = 0 ; i < ul.size(); i++) {
			Point pul = ul.get(i);
			Point pll;
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
	 * Funkcja "kolorujaca" prostokat, zamienia 1 na 2 dla prostszego odnalezienia.
	 * 
	 * @param r wybrany prostokat
	 */
	private void colorRectangle( Rectangle r ) {
		Shape s = new Shape();
		for( int j = 0; j <= r.lowerRightCorner.l - r.upperLeftCorner.l; j++ ) {
			for( int i = 0; i <= r.lowerRightCorner.k - r.upperLeftCorner.k; i++ ) {
				if (array[r.upperLeftCorner.k+i][r.upperLeftCorner.l+j] == 1) {
					array[r.upperLeftCorner.k+i][r.upperLeftCorner.l+j] += 1;
					s.add(r.upperLeftCorner.k+i, r.upperLeftCorner.l+j);
				}
			}
		}
		this.prostokaty.add(s);
	}

	/**
	 * Metoda uzywana do rozpoznania czy jest to gorny, lewy rog prostokata.
	 * 
	 * @param k parametr x
	 * @param l parametr y
	 */
	private void isUpperLeftCorner(int k, int l) {
		if ( array[k][l] == 1 
		     && k+2 < height && array[k+1][l] == 1 && array[k+2][l] == 1
		     && l+2 < width  && array[k][l+1] == 1 && array[k][l+2] == 1 ) {
			ul.add(new Point(k,l));
		}
	}
	
	/**
	 * Metoda uzywana do rozpoznania czy jest to gorny, prawy rog prostokata.
	 * 
	 * @param k parametr x
	 * @param l parametr y
	 */
	private void isUpperRightCorner(int k, int l) {
		if ( array[k][l] == 1
			 && k+2 < height && array[k+1][l] == 1 && array[k+2][l] == 1
			 && l-2 >= 0      && array[k][l-1] == 1 && array[k][l-2] == 1) {
			ur.add(new Point(k,l));
		}
	}
	
	/**
	 * Metoda uzywana do rozpoznania czy jest to dolny, lewy rog prostokata.
	 * 
	 * @param k parametr x
	 * @param l parametr y
	 */
	private void isLowerLeftCorner(int k, int l) {
		if ( array[k][l] == 1
			 && k-2 >= 0     && array[k-1][l] == 1 && array[k-2][l] == 1
			 && l+2 < width && array[k][l+1] == 1 && array[k][l+2] == 1) {
			ll.add(new Point(k,l));
		}
	}

	/**
	 * Metoda uzywana do rozpoznania czy jest to dolny, prawy rog prostokata.
	 * 
	 * @param k parametr x
	 * @param l parametr y
	 */
	private void isLowerRightCorner(int k, int l) {
		if ( array[k][l] == 1
			 && k-2 >= 0     && array[k-1][l] == 1 && array[k-2][l] == 1
			 && l-2 >= 0     && array[k][l-1] == 1 && array[k][l-2] == 1) {
			lr.add(new Point(k,l));
		}
	}

	/**
	 * Metoda znajduje parametr k (x) na liscie punktow.
	 * 
	 * @param l lista puntow
	 * @param k szukany x
	 * @return val lista znalezionych punktow
	 */
	private List<Integer> findK( List<Point> l, int k) {
		List<Integer> val = new ArrayList<>();
		for( int i = 0; i < l.size(); i++) {
			if ( l.get(i).k == k ) {
				val.add(i);
			}
		}
		return val;
	}
	
	/**
	 * Metoda znajduje parametr l (y) na liscie punktow.
	 * 
	 * @param lst lista puntow
	 * @param l szukany y
	 * @return -1 jesli nie znaleziono, i jesli znaleziono
	 */
	private int findL( List<Point> lst, int l) {
		for( int i = 0; i < lst.size(); i++) {
			if ( lst.get(i).l == l ) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Metoda wypisuje liste punktow.
	 * 
	 * @param l lista punktow
	 */
	private void printList(List<Point> l) {
		System.out.println();
		for ( Point p : l ) {
			p.printPoint();
		}
	}
	
	/**
	 * Metoda uzywana do wypisania finalnych zmian.
	 */
	public void print() {

		System.out.println("\nFound unused UL corners");
		printList(ul);
		System.out.println("Found unused UR corners");
		printList(ur);
		System.out.println("Found unused LL corners");
		printList(ll);
		System.out.println("Found unused LR corners");
		printList(lr);
		
		moveRectangle(rectangles.get(3), 20, -20);
		moveElipse(elipses.get(0), 10, -1);
		
		for (int j = 0; j < pa.getWidth(); j++) {
			System.out.println();
		    for (int k = 0; k < pa.getHeight(); k++) {
		        System.out.print(array[j][k]);
		    }
		}
	}
}
