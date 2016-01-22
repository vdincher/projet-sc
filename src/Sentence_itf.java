
public interface Sentence_itf extends SharedObject_itf {
	
	@Write
	public void write(String text);

	@Read
	public String read();

}
