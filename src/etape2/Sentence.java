package etape2;

public class Sentence implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	String 		data;
	public Sentence() {
		data = new String("");
	}
	
	public void write(String text) {
		data = text;
	}
	public String read() {
		return data;	
	}
	
}