package jp.co.atelier.SourceLineCounter.main;

import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;

import jp.co.atelier.SourceLineCounter.model.ApplicationConfigurationModel;
import jp.co.atelier.SourceLineCounter.utility.CheckUtility;
import jp.co.atelier.SourceLineCounter.utility.json.JsonUtility;

public class ApplicationInitialization {

	/**
	 * 설정 파일이 없거나, 내용에 문제가 있는 경우 ( 변환 실패 )
	 */
	private static final String ERROR_CONFIG_NULL = "설정 파일이 부정하거나 존재하지 않습니다.";
	
	/**
	 * 탐색 경로가 없거나, 값이 비어있을 경우
	 */
	private static final String ERROR_PATH_NULLOREMPTY = "탐색할 경로가 지정되어있지 않습니다." + "\n" + "현재 경로를 지정경로로 설정합니다.";
	
	/**
	 * 깊이 플래그가 없거나, 값이 비어있을 경우
	 */
	private static final String ERROR_WALKING_DEEP_NULL = "탐색 깊이 설정 플래그가 지정되어있지 않습니다." + "\n" + "깊이 플래그를 false로 설정합니다.";
	
	/**
	 * 빈 라인 카운팅 플래그가 없거나, 값이 비어있을 경우
	 */
	private static final String ERROR_COUNTING_EMPTY_LINE_NULL = "빈 라인 카운팅 플래그가 지정되어있지 않습니다." + "\n" + "빈 라인 카운팅 플래그를 true로 설정합니다.";
	
	/**
	 * 대상 확장자가 없거나, 값이 비어있을 경우
	 */
	private static final String ERROR_EXTENSIONS_NULLOREMPTY = "대상 확장자가 지정되어있지 않습니다." + "\n" + "모든 확장자를 대상으로 설정합니다.";
	
	/**
	 * 한줄 주석 부호가 없거나, 값이 비어있을 경우
	 */
	private static final String ERROR_SINGLE_COMMENTS_NULLOREMPTY = "한줄 주석 부호가 지정되어있지 않습니다." + "\n" + "모든 한줄 주석을 카운팅 대상으로 설정합니다.";
	
	/**
	 * 그룹 주석 부호가 없거나, 값이 비어있을 경우
	 */
	private static final String ERROR_GROUP_COMMENTS_NULLOREMPTY = "그룹 주석 부호가 지정되어있지 않습니다." + "\n" + "모든 그룹 주석을 카운팅 대상으로 설정합니다.";
	
	/**
	 * 탐색 경로의 기본값
	 */
	private final String currentPath = ".";
	
	/**
	 * 설정 파일을 검사 및 초기화하는 메소드
	 * @param configNode 설정 파일 노드
	 * @return 초기화가 끝난 설정 파일 객체
	 * @throws Exception 설정 파일 검사 및 초기화에 실패
	 */
	public ApplicationConfigurationModel initialization(JsonNode configNode) throws Exception {
		
		JsonUtility jsonUtility = new JsonUtility();
		ApplicationConfigurationModel config = null;
		try {
			// 설정 파일 노드를 설정 파일 객체로 변환
			config = jsonUtility.convert(configNode, ApplicationConfigurationModel.class);
		} catch (Exception e) {
		}
		
		// 변환 실패시 예외 던지기
		if (Objects.isNull(config)) {
			throw new Exception(ERROR_CONFIG_NULL);
		}
		
		if (CheckUtility.isNullOrEmpty(config.path)) {
			System.err.println(ERROR_PATH_NULLOREMPTY);
			config.path = currentPath;
		}
		
		if (Objects.isNull(config.walkingDeep)) {
			System.err.println(ERROR_WALKING_DEEP_NULL);
			config.walkingDeep = false;
		}
		
		if (Objects.isNull(config.countingEmptyLine)) {
			System.err.println(ERROR_COUNTING_EMPTY_LINE_NULL);
			config.countingEmptyLine = true;
		}
		
		if (CheckUtility.isNullOrEmpty(config.extensions)) {
			System.err.println(ERROR_EXTENSIONS_NULLOREMPTY);
			config.extensions = null;
		}
		
		if (CheckUtility.isNullOrEmpty(config.singleComments)) {
			System.err.println(ERROR_SINGLE_COMMENTS_NULLOREMPTY);
			config.singleComments = null;
		}
		
		if (CheckUtility.isNullOrEmpty(config.groupComments)) {
			System.err.println(ERROR_GROUP_COMMENTS_NULLOREMPTY);
			config.groupComments = null;
		}
		
		return config;
	}
}
