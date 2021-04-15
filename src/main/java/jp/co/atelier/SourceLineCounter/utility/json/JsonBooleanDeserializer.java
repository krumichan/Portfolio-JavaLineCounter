package jp.co.atelier.SourceLineCounter.utility.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

final class JsonBooleanDeserializer extends StdDeserializer<Boolean> {
	
	/**
	 * default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 */
	protected JsonBooleanDeserializer(Class<?> clazz) {
		super(clazz);
	}
	
	/**
	 * deserialize
	 */
	public Boolean deserialize(JsonParser parser, DeserializationContext context) 
			throws IOException, JsonProcessingException {
		if (parser.getCurrentToken() != JsonToken.VALUE_TRUE &&
			parser.getCurrentToken() != JsonToken.VALUE_FALSE) {
			throw new JsonParseException("parser error : " + parser.getText(),
					parser.getCurrentLocation());
		}
		return parser.getBooleanValue();
	}
}
