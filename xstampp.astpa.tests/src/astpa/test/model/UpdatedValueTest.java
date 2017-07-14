package astpa.test.model;

import org.junit.Test;

import xstampp.model.ObserverValue;

/**
 * 
 * Test class for the UpdatedValue enum. Is needed to get full code coverage
 * because of the methods that will be added in the bytecode
 * 
 * @author Fabian Toth
 * 
 */
public class UpdatedValueTest {
	
	/**
	 * Dummy test to get full code coverage
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testUpdatedValue() {
		Class<ObserverValue> enumClass = ObserverValue.class;
		try {
			for (Object o : (Object[]) enumClass.getMethod("values").invoke(null)) {
				enumClass.getMethod("valueOf", String.class).invoke(null, o.toString());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
