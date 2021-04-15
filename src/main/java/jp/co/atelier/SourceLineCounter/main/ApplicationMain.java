package jp.co.atelier.SourceLineCounter.main;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import jp.co.atelier.SourceLineCounter.constant.Constants;
import jp.co.atelier.SourceLineCounter.model.ApplicationConfigurationKeyModel;
import jp.co.atelier.SourceLineCounter.utility.CheckUtility;
import jp.co.atelier.SourceLineCounter.utility.DialogUtility;
import jp.co.atelier.SourceLineCounter.utility.FileUtility;
import jp.co.atelier.SourceLineCounter.utility.LoggerUtility;
import jp.co.atelier.SourceLineCounter.utility.PropertyUtility;

/**
 * 프로그램의 메인 클래스
 */
public class ApplicationMain {
	
	/**
	 * Logger
	 */
	public static Logger myLogger;
	
	/**
	 * Main 메소드
	 * @param args 설정 파일의 경로를 받음
	 */
	public static void main(String[] args) {
		
		// 설정 파일 존재 여부 확인
		if (!checkArguments(args)) {
			System.exit(1);
			return;
		}
		
		// 설정 파일로부터 JsonNode 흭득
		if (!readAppConf(args[0])) {
			System.exit(1);
			return;
		}
		
		// Logger 사용 준비
		if (!prepareBeforeLogger()) {
			System.exit(1);
			return;
		}

		myLogger.info(Constants.APPLICATION_NAME + " 프로그램을 시작합니다.");
		
		// 프로그램 api 수행 시작
		try {
			ApplicationApi api = new ApplicationApi();
			
			// Load API
			api.LoadApi(PropertyUtility.getProperty());
		} catch(Exception e) {
			myLogger.fatal("프로그램에 이상이 발생하였습니다. 프로그램을 종료합니다." + "\n" + e + (e.getCause() == null ? "" : ": " + e.getCause()), e);
		} finally {
			// don't care...
		}
	}
	
	/**
	 * 인수를 확인
	 * @param args 인수
	 * @return 확인 결과
	 */
	private static boolean checkArguments(String[] args) {
		if (Objects.isNull(args) || args.length != 1) {
			System.out.println("Failed to read the configuration file.");
			return false;
		}
		return true;
	}
	
	/**
	 * 설정 파일로부터 json 형식의 데이터를 json node 형식으로 습득
	 * @param filePath 설정 파일 위치
	 * @param node 습득한 node를 담을 변수
	 * @return 습득 여부
	 */
	private static boolean readAppConf(String filePath) {
		try {
			PropertyUtility.readProperties(filePath);
		} catch (Exception e) {
			System.out.println("Failed to read the configuration file.");
			return false;
		}
		return true;
	}
	
	/**
	 * Logger를 준비하는 메소드
	 * @return Logger 준비 성공 여부
	 */
	private static boolean prepareBeforeLogger() {
		
		// 현재 위치 습득
		Path cwdPath = FileUtility.getCWD();
		
		// 설정 파일로부터 Log 저장 위치 습득
		String logFileDestination = PropertyUtility.get(ApplicationConfigurationKeyModel.LOG_PATH);
		if (CheckUtility.isNullOrEmpty(logFileDestination)) {
			String message = ApplicationConfigurationKeyModel.LOG_PATH + "가 null 또는 공백입니다." + "\n" + "프로그램을 종료합니다.";
			DialogUtility.showMessageDialogWithText(Constants.IS_SHOW_MESSAGE_DIALOG, message);
			return false;
		}
		
		// 현재 위치로부터 Log 저장 위치를 resolve
		logFileDestination += Constants.APPLICATION_NAME + ".log";
		if (!Paths.get(logFileDestination).isAbsolute()) {
			logFileDestination = cwdPath.resolve(logFileDestination).normalize().toString();
		}
		
		// Logger 위치를 시스템 설정에 등록
		if (LoggerUtility.setLogPath(logFileDestination, Constants.LOGGER_NAME, "dir_log")) {
		} else {
			String message = "LogPath의 등록에 실패하였습니다." + "\n" + "프로그램을 종료합니다." + "\n" + "LogPath : " + logFileDestination;
			DialogUtility.showMessageDialogWithText(Constants.IS_SHOW_MESSAGE_DIALOG, message);
		}
		
		// Logger 등록이 제대로 되어, 사용가능한 상황인지 확인
		if (LoggerUtility.canWriteLog(Constants.LOGGER_NAME)) {
			myLogger = LogManager.getLogger(Constants.LOGGER_NAME);
		} else {
			return false;
		}
		
		return true;
	}
}
