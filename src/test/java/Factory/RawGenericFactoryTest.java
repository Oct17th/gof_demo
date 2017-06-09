package Factory;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import factory.RawGenericFactory;

public class RawGenericFactoryTest {
	/**
	 * Test method for {@link RawGenericFactory#newInstance(String)}
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Test 
	public void testCreateSimpleType() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		RawGenericFactory<String> factory = new RawGenericFactory<String>();
		assertTrue(factory.newInstance("java.lang.String") instanceof String);
	}
	@Test
	public void testReturnAbstractType() throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		RawGenericFactory<Map> factory = new RawGenericFactory<Map>();
		assertTrue(factory.newInstance("java.util.HashMap") instanceof Map);
		assertTrue(factory.newInstance("java.util.Properties") instanceof Map);
	}
}
