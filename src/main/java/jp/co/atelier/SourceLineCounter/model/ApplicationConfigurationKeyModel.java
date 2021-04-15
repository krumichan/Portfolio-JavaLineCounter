package jp.co.atelier.SourceLineCounter.model;

/**
 * 프로그램 설정 파일의 Key 모델 클래스
 */
public class ApplicationConfigurationKeyModel {

	/**
	 * 라인 카운팅할 대상 경로
	 * 기본값 : 현재 위치
	 */
	public static final String PATH = "path";
	
	/**
	 * Log를 저장할 경로
	 * 기본값 : 없음  -> 예외 발생
	 */
	public static final String LOG_PATH = "logPath";
	
	/**
	 * 서브 디렉토리까지 전부 탐색할 것인지를 결정하는 플래그
	 * 기본값 : false
	 */
	public static final String WALKING_DEEP = "walkingDeep";
	
	/**
	 * 빈 라인도 카운팅에 포함시킬 것인지를 결정하는 플래그
	 * 기본값 : true
	 */
	public static final String COUNTING_EMPTY_LINE = "countingEmptyLine";
	
	/**
	 * 탐색 대상으로 할 파일의 확장자
	 * 기본값 : null -> 모든 확장자가 대상
	 */
	public static final String EXTENSIONS = "extensions";
	
	/**
	 * 탐색 대상에서 제외할 한줄 주석
	 * 기본값 : null -> 모든 한줄 주석을 대상에 포함
	 */
	public static final String SINGLE_COMMENTS = "singleComments";
	
	/**
	 * 탐색 대상에서 제외할 그룹 주석
	 * 기본값 : null -> 모든 그룹 주석을 대상에 포함
	 */
	public static final String GROUP_COMMENTS = "groupComments";
}
