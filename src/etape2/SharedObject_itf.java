package etape2;

public interface SharedObject_itf {
	public void lock_read();
	public void lock_write();
	public void unlock();
}