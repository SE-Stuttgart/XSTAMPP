package astpa.test.model.controlstructure;

import org.junit.Test;

import astpa.model.controlstructure.components.ConnectionType;

/**
 * 
 * Test class for the ConnectionType enum. Is needed to get full code coverage
 * because of the methods that will be added in the bytecode
 * 
 * @author Fabian Toth
 * 
 */
public class ConnectionTypeTest {
	
	/**
	 * Dummy test to get full code coverage
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testConnectionType() {
		Class<ConnectionType> enumClass = ConnectionType.class;
		try {
			for (Object o : (Object[]) enumClass.getMethod("values").invoke(null)) {
				enumClass.getMethod("valueOf", String.class).invoke(null, o.toString());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}