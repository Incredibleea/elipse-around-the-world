package elipserectangle;

public class Start {
	
	public static void main(String[] args) {
		PixelArray p = PixelArray.getInstance();
		p.printPixelArray();
		
		Recognizer r = new Recognizer();
		r.recognizeRectangle();
		
		r.recognizeEllipse();
		
		r.print();
	}
}
