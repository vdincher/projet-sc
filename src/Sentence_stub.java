/** @Generated */ 
public class Sentence_stub extends SharedObject implements Sentence_itf, java.io.Serializable {

	 private static final long serialVersionUID = 1L;

	 public Sentence_stub(Object o, int id){
	 	 super(o, id);
	 }

	 public void write(java.lang.String arg0){
	 	 this.lock_write();
	 	 ((Sentence) this.getO()).write(arg0);
	 	 if (Transaction.getCurrentTransaction()==null) {
	 	 	 this.unlock();
	 	 }
	 }

	 public java.lang.String read(){
	 	 this.lock_read();
	 	 java.lang.String returnReadForSentenceStub  = ((Sentence) this.getO()).read();
	 	 if (Transaction.getCurrentTransaction()==null) {
	 	 	 this.unlock();
	 	 }
	 	 return returnReadForSentenceStub;
	 }


}