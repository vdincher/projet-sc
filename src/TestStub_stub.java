/** @Generated */ 
public class TestStub_stub extends SharedObject implements TestStub_itf, java.io.Serializable {

	 private static final long serialVersionUID = 1L;

	 public TestStub_stub(Object o, int id){
	 	 super(o, id);
	 }

	 public void test_w1(java.lang.String arg0){
	 	 this.lock_write();
	 	 ((TestStub) this.getO()).test_w1(arg0);
	 	 if (Transaction.getCurrentTransaction()==null) {
	 	 	 this.unlock();
	 	 }
	 }

	 public java.lang.String test_r1(){
	 	 this.lock_read();
	 	 java.lang.String returnReadForTestStubStub  = ((TestStub) this.getO()).test_r1();
	 	 if (Transaction.getCurrentTransaction()==null) {
	 	 	 this.unlock();
	 	 }
	 	 return returnReadForTestStubStub;
	 }

	 public java.lang.String test_rw(java.lang.String arg0, int arg1){
	 	 this.lock_write();
	 	 this.lock_read();
	 	 java.lang.String returnReadForTestStubStub  = ((TestStub) this.getO()).test_rw(arg0, arg1);
	 	 if (Transaction.getCurrentTransaction()==null) {
	 	 	 this.unlock();
	 	 }
	 	 return returnReadForTestStubStub;
	 }

	 public float test2(char arg0, float arg1, java.lang.String arg2){
	 	 return 0.0F;
	 }

	 public char test_r2(){
	 	 this.lock_read();
	 	 char returnReadForTestStubStub  = ((TestStub) this.getO()).test_r2();
	 	 if (Transaction.getCurrentTransaction()==null) {
	 	 	 this.unlock();
	 	 }
	 	 return returnReadForTestStubStub;
	 }

	 public void test_w2(int arg0, char arg1){
	 	 this.lock_write();
	 	 ((TestStub) this.getO()).test_w2(arg0, arg1);
	 	 if (Transaction.getCurrentTransaction()==null) {
	 	 	 this.unlock();
	 	 }
	 }

	 public void test1(char arg0, int arg1, java.lang.String arg2){
	 }


}