package elipserectangle;

public class Start {
	
	public static void main(String[] args) {
		PixelArray p = PixelArray.getInstance();
		p.printPixelArray();
		
		Recognizer r = new Recognizer();
		r.recognizeRectangle();
		
		r.recognizeElipse();
		//r.moveRectangle(r.rectangles.get(2), 20, 20);
		
		r.print();
	}
}
