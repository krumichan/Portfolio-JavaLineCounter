package jp.co.atelier.SourceLineCounter.utility;

import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * 유틸리티 클래스
 * 기능 : Dialog를 출력
 */
public class DialogUtility {
	
	/**
	 * Dialog 출력 실패시의 메세지
	 */
	private static final String MESSAGE_CANNOT_DISPLAY_DIALOG = "Dialog 출력에 실패하였습니다.";

	/**
	 * Terminal에의 출력과 텍스트 박스 Dialog 출력을 결정
	 * @param isDialog 텍스트 박스 Dialog를 출력할지의 여부
	 * @param message 출력 내용
	 */
	public static void showMessageDialogWithText(boolean isDialog, String message) {
		if (isDialog) {
			System.out.println(message);
			try {
				JOptionPane.showMessageDialog(null, message);
			} catch(HeadlessException e) {
				System.out.println(MESSAGE_CANNOT_DISPLAY_DIALOG);
			}
		} else {
			System.out.println(message);
		}
	}
	
	/**
	 * Terminal에의 출력과 텍스트 영역 Dialog 출력을 결정
	 * @param isDialog 텍스트 영역 Dialog를 출력할지의 여부
	 * @param message 출력 내용
	 */
	public static void showMessageDialogWithTextArea(boolean isDialog, String message) {
		if (isDialog) {
			System.out.println(message);
			showMessageDialog(message);
		} else {
			System.out.println(message);
		}
	}
	
	/**
	 * Dialog 내용을 출력할 텍스트 영역을 생성
	 * @param message 텍스트 영역에 출력할 내용
	 */
	private static void showMessageDialog(String message) {
		
		// 메세지를 담은 영역 객체 생성
		JTextArea jta = new JTextArea(message);
		
		// 텍스트 영역의 수정 가능 여부 설정
		jta.setEditable(false);
		
		// 텍스트 영역의 길이를 초과할 경우, 줄바꿈을 넣어, 다음줄에 출력할지 여부를 설정
		jta.setLineWrap(true);
		
		// 텍스트 영역의 투명도 설정
		jta.setOpaque(false);
		
		// 텍스트 영역의 크기 설정
		jta.setSize(480, 10);
		
		// 텍스트 영역의 자동 영역 크기 보정 설정
		jta.setPreferredSize(new Dimension(480, jta.getPreferredSize().height));
		
		try {
			JOptionPane.showMessageDialog(null, jta);
		} catch (HeadlessException e) {
			System.out.println(MESSAGE_CANNOT_DISPLAY_DIALOG);
		}
	}
}
