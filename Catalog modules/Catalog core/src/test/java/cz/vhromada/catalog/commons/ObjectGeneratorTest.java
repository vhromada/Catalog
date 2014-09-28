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
	public static void setUp() {
		final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:testGeneratorContext.xml");
		objectGenerator = applicationContext.getBean(ObjectGenerator.class);
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
