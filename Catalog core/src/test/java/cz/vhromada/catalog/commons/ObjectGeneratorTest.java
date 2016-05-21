//package cz.vhromada.catalog.commons;
//
//import cz.vhromada.generator.ObjectGenerator;
//
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//
///**
// * An abstract class represents initializing object generator.
// *
// * @author Vladimir Hromada
// */
//public abstract class ObjectGeneratorTest {
//
//    /**
//     * Instance of {@link ConfigurableApplicationContext}
//     */
//    private static ConfigurableApplicationContext applicationContext;
//
//    /**
//     * Instance of {@link ObjectGenerator}
//     */
//    private static ObjectGenerator objectGenerator;
//
//    /**
//     * Initialize instance of {@link ObjectGenerator}.
//     */
//    @BeforeClass
//    public static void setUpClass() {
//        applicationContext = new ClassPathXmlApplicationContext("testGeneratorContext.xml");
//        objectGenerator = applicationContext.getBean(ObjectGenerator.class);
//    }
//
//    /**
//     * Closes application context.
//     */
//    @AfterClass
//    public static void tearDownClass() {
//        applicationContext.close();
//    }
//
//    /**
//     * Returns object generator.
//     *
//     * @return object generator
//     */
//    protected static ObjectGenerator getObjectGenerator() {
//        return objectGenerator;
//    }
//
//    /**
//     * Returns generated object.
//     *
//     * @param clazz class of generated object
//     * @param <T>   type of object
//     * @return generated object
//     */
//    protected <T> T generate(final Class<T> clazz) {
//        return objectGenerator.generate(clazz);
//    }
//
//}
