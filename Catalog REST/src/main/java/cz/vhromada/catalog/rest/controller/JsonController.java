package cz.vhromada.catalog.rest.controller;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import org.springframework.beans.factory.InitializingBean;

/**
 * An abstract class represents controller with data in JSON.
 *
 * @author Vladimir Hromada
 */
public class JsonController implements InitializingBean {

	private Genson genson;

	@Override
	public void afterPropertiesSet() throws Exception {
		genson = new GensonBuilder().create();
	}

	/**
	 * Serialize object to JSON.
	 *
	 * @param object object
	 * @param <T>    type of object
	 * @return serialized object in JSON
	 */
	protected <T> String serialize(final T object) {
		return genson.serialize(object);
	}

	/**
	 * Deserialize object from JSON.
	 *
	 * @param json  object in JSON
	 * @param clazz class of returned object
	 * @param <T>   type of returned object
	 * @return deserialized object from JSON
	 */
	protected <T> T deserialize(final String json, final Class<T> clazz) {
		return genson.deserialize(json, clazz);
	}

}