package jp.co.atelier.SourceLineCounter.utility;

import java.io.File;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.NullEnumeration;

import jp.co.atelier.SourceLineCounter.constant.Constants;

/**
 * 유틸리티 클래스
 * 기능 : Logger 생성 및 사용 가능 여부, Logger를 소유
 */
public class LoggerUtility {
	
	/**
	 * Logger
	 */
	private static Logger myLogger = null;
	
	/**
	 * Logger를 반환
	 * @return Logger
	 */
	public static Logger getLogger() {
		return LoggerUtility.myLogger;
	}
	
	/**
	 * Logger가 사용 가능한 상태인지를 반환
	 * @param name 대상 Logger 이름
	 * @return Logger의 사용가능 여부
	 */
	public static boolean canWriteLog(String name) {
		boolean canWriteLog = false;
		
		// Log Manager로부터 대상 Logger를 습득
		myLogger = LogManager.getLogger(name);
		
		// 대상 Logger가 null인지를 확인
		// 만약, 습득에 실패했을 경우, appender에 null이 들어가게 됨.
		if (NullEnumeration.getInstance().equals(myLogger.getAllAppenders())) {
			String message = "log4j의 설정파일 읽기에 실패하였습니다.";
			DialogUtility.showMessageDialogWithTextArea(Constants.IS_SHOW_MESSAGE_DIALOG, message);
			return canWriteLog;
		}
		
		canWriteLog = true;
		return canWriteLog;
	}
	
	/**
	 * Logger의 경로를 설정
	 * @param logFileDestination Log 파일을 저장할 경로
	 * @param name Logger 이름
	 * @param path 시스템 프로퍼티 상에 등록할 경로 이름 ( logFileDestination과 key - value 관계를 형성 )
	 * @return 설정 성공 여붖
	 */
	public static boolean setLogPath(String logFileDestination, String name, String path) {
		boolean canWriteLog = false;
		System.setProperty(path, logFileDestination);
		try {
			File logFile = new File(logFileDestination);
			if (logFile.exists()) {
				if (!logFile.canWrite()) {
					String message = "Log의 출력 위치를 읽을 수 없습니다." + "\n" + "프로그램을 종료합니다." + "\n" + "출력 위치 : " + logFileDestination;
					DialogUtility.showMessageDialogWithTextArea(Constants.IS_SHOW_MESSAGE_DIALOG, message);
					return canWriteLog;
				}
				
				FileChannel fc = null;
				try {
					fc = FileChannel.open(logFile.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
					FileLock lock = fc.tryLock();
					if (Objects.isNull(lock)) {
						throw new Exception();
					} else {
						lock.release();
					}
				} catch (Exception e) {
					String message = "Log의 출력 위치가 다른 프로세스에 의해 Lock이 걸려있습니다." + "\n" + "프로그램을 종료합니다." + "\n" + "출력 위치 : " + logFileDestination;
					DialogUtility.showMessageDialogWithTextArea(Constants.IS_SHOW_MESSAGE_DIALOG, message);
					return canWriteLog;
				}
			} else {
				Boolean createNewFile = logFile.createNewFile();
				if (!createNewFile) {
					String message = "Log의 출력 위치에 이상이 발생했습니다." + "\n" + "프로그램을 종료합니다." + "\n" + "출력 위치 : " + logFileDestination;
					DialogUtility.showMessageDialogWithTextArea(Constants.IS_SHOW_MESSAGE_DIALOG, message);
					return canWriteLog;
				}
			}
		} catch (Exception e) {
			String message = "Log의 출력 위치에 이상이 발생했습니다." + "\n" + "프로그램을 종료합니다." + "\n" + "출력 위치 : " + logFileDestination;
			DialogUtility.showMessageDialogWithTextArea(Constants.IS_SHOW_MESSAGE_DIALOG, message);
			return canWriteLog;
		}
		
		return true;
	}
}
