package jp.co.atelier.SourceLineCounter.utility.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

final class JsonFloatDeserializer extends StdDeserializer<Float> {
	
	/**
	 * default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 */
	protected JsonFloatDeserializer(Class<?> clazz) {
		super(clazz);
	}
	
	/**
	 * deserialize
	 */
	public Float deserialize(JsonParser parser, DeserializationContext context) 
			throws IOException, JsonProcessingException {
		if (parser.getCurrentToken() != JsonToken.VALUE_NUMBER_INT &&
			parser.getCurrentToken() != JsonToken.VALUE_NUMBER_FLOAT) {
			throw new JsonParseException("parser error : " + parser.getText(),
					parser.getCurrentLocation());
		}
		
		if (parser.getFloatValue() < -Float.MAX_VALUE ||
			parser.getFloatValue() > Float.MAX_VALUE) {
			throw new JsonParseException("parser range error : " + parser.getText(),
					parser.getCurrentLocation());
		}
		return parser.getFloatValue();
	}
}
