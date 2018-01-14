package elipserectangle;

public class Start {
	
	public static void main(String[] args) {
		PixelArray p = PixelArray.getInstance();
		p.printPixelArray();
		
		Recognizer r = new Recognizer();
		r.recognizeRectangle();
		
		System.out.println();
		r.print();
	}
}
