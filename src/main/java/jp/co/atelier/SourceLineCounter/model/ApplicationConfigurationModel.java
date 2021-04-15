package jp.co.atelier.SourceLineCounter.model;

import java.util.List;

/**
 * 설정 파일의 내용을 객체로 받는 클래스
 */
public class ApplicationConfigurationModel {

	/**
	 * 라인 카운팅할 대상 경로
	 * 기본값 : 현재 위치
	 */
	public String path;
	
	/**
	 * Log를 저장할 경로
	 * 기본값 : 없음  -> 예외 발생
	 */
	public String logPath;
	
	/**
	 * 서브 디렉토리까지 전부 탐색할 것인지를 결정하는 플래그
	 * 기본값 : false
	 */
	public Boolean walkingDeep;
	
	/**
	 * 빈 라인도 카운팅에 포함시킬 것인지를 결정하는 플래그
	 * 기본값 : true
	 */
	public Boolean countingEmptyLine;
	
	/**
	 * 탐색 대상으로 할 파일의 확장자
	 * 기본값 : null -> 모든 확장자가 대상
	 */
	public List<String> extensions;
	
	/**
	 * 탐색 대상에서 제외할 한줄 주석
	 * 기본값 : null -> 모든 한줄 주석을 대상에 포함
	 */
	public List<SingleComment> singleComments;
	
	/**
	 * 탐색 대상에서 제외할 그룹 주석
	 * 기본값 : null -> 모든 그룹 주석을 대상에 포함
	 */
	public List<GroupComment> groupComments;
	
	/**
	 * 한줄 주석의 정보를 들고 있는 클래스
	 */
	public static class SingleComment {
		
		// 주석 시작 부호
		public String open;
	}
	
	/**
	 * 그룹 주석의 정보를 들고 있는 클래스
	 */
	public static class GroupComment {
		
		// 주석 시작 부호
		public String open;
		
		// 주석 종료 부호
		public String close;
	}
}
