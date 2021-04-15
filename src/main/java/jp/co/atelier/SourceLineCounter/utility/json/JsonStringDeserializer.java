package jp.co.atelier.SourceLineCounter.utility.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

final class JsonStringDeserializer extends StdDeserializer<String> {
	
	/**
	 * default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 */
	protected JsonStringDeserializer(Class<?> clazz) {
		super(clazz);
	}
	
	/**
	 * deserialize
	 */
	public String deserialize(JsonParser parser, DeserializationContext context) 
			throws IOException, JsonProcessingException {
		if (parser.getCurrentToken() != JsonToken.VALUE_STRING) {
			throw new JsonParseException("parser error : " + parser.getText(),
					parser.getCurrentLocation());
		}
		return parser.getText();
	}
}
