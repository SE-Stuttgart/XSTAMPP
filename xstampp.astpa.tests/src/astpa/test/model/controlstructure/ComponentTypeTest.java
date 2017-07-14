package astpa.test.model.controlstructure;

import org.junit.Test;

import xstampp.astpa.model.controlstructure.components.ComponentType;

/**
 * 
 * Test class for the ComponentType enum. Is needed to get full code coverage
 * because of the methods that will be added in the bytecode
 * 
 * @author Fabian Toth
 * 
 */
public class ComponentTypeTest {
	
	/**
	 * Dummy test to get full code coverage
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testComponentType() {
		Class<ComponentType> enumClass = ComponentType.class;
		try {
			for (Object o : (Object[]) enumClass.getMethod("values").invoke(null)) {
				enumClass.getMethod("valueOf", String.class).invoke(null, o.toString());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
