
public interface TestStub_itf {

	@Write
	public void test_w1(String text);

	@Read
	public String test_r1();

	@Read
	@Write
	public String test_rw(String text, int i);

	public float test2(char c, float i, String text);
	
	@Read
	public char test_r2();

	@Write
	public void test_w2(int i, char c);
	
	public void test1(char c, int i, String text);
	


}
