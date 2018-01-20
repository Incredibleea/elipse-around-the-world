package elipserectangle;

/**
 * Klasa Start jest glowna klasa w projekcie. W niej tworzone sa obiekty pozostalych klas
 * i wywolywane sa metody odpowiedzialne za odczytywanie pliku, rozpoznawanie elips, prostokatow
 * oraz umozliwiona jest zmiana ich polozenia.
 * 
 * @author Wojciech Nowak, Dawid Gadomski
 *
 */
public class Start {
	
	/**
	 * Metoda glowna klasy Start.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		PixelArray p = PixelArray.getInstance();
		p.printPixelArray();
		
		Recognizer r = new Recognizer();
		r.recognizeRectangle();
		r.recognizeEllipse();
		
		r.moveEllipse(3, 4, 4);
		r.moveEllipse(2,4,7);
		r.moveRectangle(3, -3, 4);
		
		r.findIntersections();
		
		r.print();
	}
}
