package astpa.test.model.controlaction;

import org.junit.Test;

import astpa.model.controlaction.UnsafeControlActionType;

/**
 * 
 * Test class for the UnsafeControlActionType enum. Is needed to get full code
 * coverage because of the methods that will be added in the bytecode
 * 
 * @author Fabian Toth
 * 
 */
public class UnsafeControlActionTypeTest {
	
	/**
	 * Dummy test to get full code coverage
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testConnectionType() {
		Class<UnsafeControlActionType> enumClass = UnsafeControlActionType.class;
		try {
			for (Object o : (Object[]) enumClass.getMethod("values").invoke(null)) {
				enumClass.getMethod("valueOf", String.class).invoke(null, o.toString());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}