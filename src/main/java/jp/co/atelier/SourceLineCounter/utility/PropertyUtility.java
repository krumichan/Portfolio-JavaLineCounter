package jp.co.atelier.SourceLineCounter.utility;

import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;

import jp.co.atelier.SourceLineCounter.constant.Constants;
import jp.co.atelier.SourceLineCounter.utility.json.JsonUtility;

/**
 * 유틸리티 클래스
 * 기능 : 임의의 경로로부터의 내용을 Property화 시킴
 */
public class PropertyUtility {

	/**
	 * 임의의 경로로부터 습득한 Property
	 */
	private static JsonNode property = null;
	
	// 객체 생성 방지
	private PropertyUtility() {
		
	}
	
	/**
	 * 경로로부터 Property를 읽어들임
	 * @param path 대상 경로
	 * @throws Exception 읽기 실패
	 */
	public static void readProperties(String path) throws Exception {
		property = readAppProp(path);
	}
	
	/**
	 * 임의 경로로부터 습득한 Property를 반환
	 * @return 습득한 Property
	 */
	public static JsonNode getProperty() {
		return property;
	}
	
	/**
	 * 인수로 key를 받아, Property로부터 key에 해당하는 value를 반환 
	 * @param key Property가 보유하고 있는 key 값
	 * @return key에 대응하는 value
	 */
	public static String get(String key) {
		JsonNode target = property.get(key);
		String result = null;
		if (Objects.nonNull(target)) {
			result = FormatUtility.trim(target.asText());
		}
		return result;
	}
	
	/**
	 * 대상경로로부터 얻은 내용을 JsonNode 형식으로 변환
	 * @param path 대상 경로
	 * @return JsonNode 형식으로 변환한 값
	 */
	private static JsonNode readAppProp(String path) {
		JsonUtility ju = new JsonUtility();
		JsonNode result = null;
		try {
			result = ju.jsonRead(path);
		} catch (Exception e) {
			String message = "설정 파일 읽기에 실패하였습니다." + "\n" + "파일명 : " + path;
			DialogUtility.showMessageDialogWithTextArea(Constants.IS_SHOW_MESSAGE_DIALOG, message);
			System.exit(1);
		}
		return result;
	}
}
