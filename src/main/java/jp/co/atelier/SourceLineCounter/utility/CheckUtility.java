package jp.co.atelier.SourceLineCounter.utility;

import java.util.List;
import java.util.Objects;

/**
 * 유틸리티 클래스
 * 기능 : 각종 상태를 체크
 */
public final class CheckUtility {
	
	/**
	 * 객체 생성 방지
	 */
	private CheckUtility() {
		
	}
		
	/**
	 * 문자열이 null 또는 빈 문자열인지 검사
	 * @param str 대상 문자열
	 * @return 검사 결과
	 */
	public static boolean isNullOrEmpty(String str) {
		return Objects.isNull(str) || str.isEmpty();
	}
	
	/**
	 * 리스트가 null 또는 빈 list인지 검사
	 * @param list 대상 리스트
	 * @return 검사 결과
	 */
	public static boolean isNullOrEmpty(@SuppressWarnings("rawtypes") List list) {
		return Objects.isNull(list) || list.isEmpty();
	}
}
