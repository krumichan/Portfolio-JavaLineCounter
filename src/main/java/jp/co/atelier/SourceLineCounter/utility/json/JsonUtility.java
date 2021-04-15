package jp.co.atelier.SourceLineCounter.utility.json;

import java.io.FileInputStream;
import java.io.InputStream;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * JSON Utility
 */
public final class JsonUtility {

	/////////////////////////////////////////
	// deserializer core
	/////////////////////////////////////////
	
	/**
	 * Object Mapper
	 */
	private ObjectMapper om = null;
	
	/**
	 * Constructor
	 */
	public JsonUtility() {
		this.om = new ObjectMapper();
		this.om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.om.configure(DeserializationFeature.WRAP_EXCEPTIONS, false);
		SimpleModule module = new SimpleModule()
				.addDeserializer(String.class,  new JsonStringDeserializer(String.class))
				.addDeserializer(Integer.TYPE, new JsonIntegerDeserializer(Integer.TYPE))
				.addDeserializer(Integer.class, new JsonIntegerDeserializer(Integer.class))
				.addDeserializer(Long.TYPE, new JsonLongDeserializer(Long.TYPE))
				.addDeserializer(Long.class, new JsonLongDeserializer(Long.class))
				.addDeserializer(Boolean.TYPE, new JsonBooleanDeserializer(Boolean.TYPE))
				.addDeserializer(Boolean.class, new JsonBooleanDeserializer(Boolean.class))
				.addDeserializer(Float.TYPE, new JsonFloatDeserializer(Float.TYPE))
				.addDeserializer(Float.class, new JsonFloatDeserializer(Float.class))
				.addDeserializer(Double.TYPE, new JsonDoubleDeserializer(Double.TYPE))
				.addDeserializer(Double.class, new JsonDoubleDeserializer(Double.class));
		om.registerModule(module);
	}
	
	
	/////////////////////////////////////////
	// function
	/////////////////////////////////////////
	
	/**
	 * 대상 JsonNode를 지정된 class 형식으로 변환
	 * @param json 대상 JsonNode
	 * @param clazz 변환할 형식의 class
	 * @return 지정된 class로 변환한 인스턴스
	 * @throws Exception 변환실패 (타입 불일치 등)
	 */
	public <T> T convert(JsonNode json, Class<T> clazz) throws Exception {
		return om.treeToValue(json, clazz);
	}
	
	/**
	 * 대상 파일로부터 json 형식을 읽어들여 지정된 class 형식으로 변환
	 * @param fileName 대상 파일 위치
	 * @param clazz 변환할 형식의 class
	 * @return 지정된 class로 변환한 인스턴스
	 * @throws Exception 변환실패 (파일이 없음, 타입 불일치 등)
	 */
	public <T> T jsonRead(String fileName, Class<T> clazz) throws Exception {
		T value = null;
		try (InputStream is = new FileInputStream(fileName)) {
			value = om.readValue(is, clazz);
		}
		return value;
	}
	
	/**
	 * 대상 파일로부터 json 형식을 읽어들여 JsonNode 형식으로 변환
	 * @param path 대상 파일 위치
	 * @return JsonNode로 변환한 인스턴스
	 * @throws Exception Exception 변환실패 (파일이 없음, 타입 불일치 등)
	 */
	public JsonNode jsonRead(String path) throws Exception {
		JsonNode result = null;
		try (InputStream is = new FileInputStream(path)) {
			result = om.readTree(is);
		}
		return result;
	}
	
	/**
	 * 대상 String 형식의 JSON 데이터로부터 데이터 습득
	 * @param json 변환 대상의 String 데이터
	 * @param Class<T> 변환 타입의 클래스
	 * @return 변환 결과
	 * @throws Exception 변환 실패
	 */
	public <T> T strJsonRead(String json, Class<T> clazz) throws Exception {
		return om.readValue(json, clazz);
	}
}
