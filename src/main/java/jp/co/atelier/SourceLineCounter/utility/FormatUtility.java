package jp.co.atelier.SourceLineCounter.utility;

/**
 * 유틸리티 클래스
 * 기능 : format을 필요 형태로 변환
 */
public final class FormatUtility {
	
	/**
	 * 객체 생성 방지
	 */
	private FormatUtility() {
		
	}
	
	/**
	 * 문자열의 양 끝의 공백 제거
	 * @param str 대상 문자열
	 * @return 공백을 제거한 문자열
	 */
	public static String trim(String str) {
		return str.trim();
	}
}
