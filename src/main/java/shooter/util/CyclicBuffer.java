package shooter.util;

public  class  CyclicBuffer<T> {
	
	private int index = 0;
	
	private Object[] array;
	
	private int mask;
	
	public CyclicBuffer(int size) throws IllegalArgumentException {
		if(!((size & (size - 1)) == 0)) {
			throw new IllegalArgumentException("size must be the power of 2");
		}
		array =  new Object[size];
		mask = array.length - 1;
	}
	
	public void insert(T obj) {
		array[index] = obj;
		index = (index + 1)&mask;
	}
	
	public T get(int index) {
		return (T) array[(this.index + index) & mask];
	}
	
}
