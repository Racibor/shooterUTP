package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import shooter.util.CyclicBuffer;

public class CyclicBufferTest {
	
	@Test()
	public void sizeTest() {
		CyclicBuffer<Integer> buffer = new CyclicBuffer<>(4);
		buffer.insert(5);
		int response = buffer.get(-1);
		assertEquals(response, 5);
		
		assertThrows(IllegalArgumentException.class, () -> {
			CyclicBuffer<Integer> buf = new CyclicBuffer<>(3);
		});
	}
}
