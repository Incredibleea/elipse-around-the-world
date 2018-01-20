package elipserectangle;

public class Start {
	
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
