package shooter.server.packet;


public interface Parser<T, V> {
	
	public T parse(V data);
}
