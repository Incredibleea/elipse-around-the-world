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
		
		r.recognizeElipse();
		
		r.print();
	}
}
