package cz.vhromada.catalog.commons;

import cz.vhromada.generator.ObjectGenerator;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * An abstract class represents initializing object generator.
 *
 * @author Vladimir Hromada
 */
public abstract class ObjectGeneratorTest {

	/** Instance of {@link ObjectGenerator} */
	private static ObjectGenerator objectGenerator;

	/** Initialize instance of {@link ObjectGenerator}. */
	@BeforeClass
	public static void setUpClass() {
		final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("testGeneratorContext.xml");
		objectGenerator = applicationContext.getBean(ObjectGenerator.class);
	}

	/**
	 * Returns object generator.
	 *
	 * @return object generator
	 */
	protected static ObjectGenerator getObjectGenerator() {
		return objectGenerator;
	}

	/**
	 * Returns generated object.
	 *
	 * @param clazz class of generated object
	 * @param <T>   type of object
	 * @return generated object
	 */
	protected <T> T generate(final Class<T> clazz) {
		return objectGenerator.generate(clazz);
	}

}