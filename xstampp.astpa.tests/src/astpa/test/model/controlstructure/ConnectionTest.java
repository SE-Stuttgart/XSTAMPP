package astpa.test.model.controlstructure;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import xstampp.astpa.model.controlstructure.components.Anchor;
import xstampp.astpa.model.controlstructure.components.CSConnection;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.components.ConnectionType;

/**
 * Class that tests connections
 * 
 * @author Fabian Toth
 */
public class ConnectionTest {
	
	/**
	 * Test of a connection
	 * 
	 * @author Fabian Toth
	 */
	@Test
	public void connectionTest() {
		Anchor source = new Anchor(true, 5, 10, UUID.randomUUID());
		Anchor target = new Anchor(false, 10, 5, UUID.randomUUID());
		
		// check parameterized constructor
		CSConnection connection = new CSConnection(source, target, ConnectionType.ARROW_DASHED);
		Assert.assertEquals(source, connection.getSourceAnchor());
		Assert.assertEquals(target, connection.getTargetAnchor());
		Assert.assertEquals(ConnectionType.ARROW_DASHED, connection.getConnectionType());
		Assert.assertEquals(ComponentType.CONNECTION, connection.getComponentType());
		
		// check empty constructor
		connection = new CSConnection();
		connection.setSourceAnchor(source);
		Assert.assertEquals(source, connection.getSourceAnchor());
		connection.setTargetAnchor(target);
		Assert.assertEquals(target, connection.getTargetAnchor());
		connection.setConnectionType(ConnectionType.ARROW_SIMPLE);
		Assert.assertEquals(ConnectionType.ARROW_SIMPLE, connection.getConnectionType());
		UUID id = UUID.randomUUID();
		connection.setId(id);
		Assert.assertEquals(id, connection.getId());
	}
}
