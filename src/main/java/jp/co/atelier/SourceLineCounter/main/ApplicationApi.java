package jp.co.atelier.SourceLineCounter.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;

import jp.co.atelier.SourceLineCounter.model.ApplicationConfigurationModel;
import jp.co.atelier.SourceLineCounter.utility.CheckUtility;
import jp.co.atelier.SourceLineCounter.utility.FileUtility;
import jp.co.atelier.SourceLineCounter.utility.FormatUtility;
import jp.co.atelier.SourceLineCounter.utility.LoggerUtility;

/**
 * 소스의 라인수를 세는 API
 */
public class ApplicationApi {
	
	/**
	 * Logger
	 */
	private Logger myLogger = null;

	/**
	 * 설정 파일의 값을 담고있는 변수
	 */
	private ApplicationConfigurationModel config = null;
	
	/**
	 * API에서 임시로 사용하는 한줄 주석 부호 리스트
	 */
	private List<String> singleComments = null;
	
	/**
	 * API에서 임시로 사용하는 그룹 주석 부호 리스트
	 */
	private List<List<String>> groupComments = null; 
	
	/**
	 * 총 줄 수
	 */
	private int countingTotal = 0;
	
	/**
	 * 생성자
	 */
	public ApplicationApi() {
		myLogger = LoggerUtility.getLogger();
	}
	
	/**
	 * API를 호출하는 메소드
	 * @param configNode 설정 파일 노드
	 * @throws Exception API 수행 실패
	 */
	public void LoadApi(JsonNode configNode) throws Exception {
		ApplicationInitialization initialization = new ApplicationInitialization();
		config = initialization.initialization(configNode);
		
		// 한줄 주석 부호 모두 추가
		if (config.singleComments != null) {
			for (ApplicationConfigurationModel.SingleComment singleComment : config.singleComments) {
				if (Objects.isNull(singleComments)) {
					singleComments = new ArrayList<String>();
				}
				singleComments.add(singleComment.open);
			}
		}
		
		// 그룹 주석 부호 모두 추가
		if (config.groupComments != null) {
			for (ApplicationConfigurationModel.GroupComment groupComment : config.groupComments) {
				if (Objects.isNull(groupComments)) {
					groupComments = new ArrayList<List<String>>();
				}
				List<String> tmpValue = Arrays.asList(groupComment.open, groupComment.close);
				groupComments.add(tmpValue);
			}
		}
		
		// 실제 API 동작 수행
		this.execute();
		
		myLogger.info("총 라인수 : " + countingTotal);
	}
	
	/**
	 * API 수행 메소드
	 * @throws Exception 습득 실패
	 */
	private void execute() throws Exception {
		
		// 설정 경로로부터 대상 디렉토리 추출
		Set<String> targetPaths = FileUtility.readDirectories(config.path, config.walkingDeep);
		
		// 대상 파일 모두 추출
		Set<String> targetFiles = FileUtility.readFiles(targetPaths, config.extensions);
		
		// 모든 라인 카운팅
		for (String path : targetFiles) {
			int sumLine = lineCounting(path);
			String countingInfo = "path:" + path + " -- 총 라인수:" + sumLine;
			myLogger.info(countingInfo);
			countingTotal += sumLine;
		}
	}
	
	/**
	 * 라인 수를 세는 메소드
	 * @param curPath 대상 경로
	 * @return 셈한 라인수
	 * @throws Exception 라인수 계산 실패
	 */
	private int lineCounting(String curPath) throws Exception {
		
		// 라인 카운팅
		int count = 0;
		
		// 파일 읽기
		try (FileInputStream fis = new FileInputStream(curPath);
			 InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			 BufferedReader br = new BufferedReader(isr)) {
			
			// 라인 읽기
			String line = null;
			
			// 그룹 주석 부호가 열려 있는가?
			boolean groupOpen = false;
			
			// 열렸을 때의 짝이 되는 부호를 저장
			String closeBracket = null;
			
			// 다음 라인을 읽어들인다.
			boolean readNext = true;
			
			// 그룹 주석이 열려있을 때, 만약 그룹 주석을 닫는 부호가 존재할 경우, 그 때의 인덱스를 저장
			// 이후, 그룹 주석을 여는 부호가 같은 줄에 있을 경우 open을 판별하는 것에 사용
			int closeIndex = -1;
			
			// 처리 수행
			while (true) {
				
				// 다음 라인을 읽는 플래그가 true일 경우 다음 라인을 읽는다.
				if (readNext) {
					closeIndex = -1;
					line = br.readLine();
				}
				
				// 읽어들인 다음 라인이 null일 경우 카운팅을 종료
				if (line == null) {
					break;
				}
				
				// 공백 모두 제거
				line = FormatUtility.trim(line);
				
				// 첫번째 검사 : 빈 공백일 경우
				if (!config.countingEmptyLine) {
					if (CheckUtility.isNullOrEmpty(line)) {
						continue;
					}
				}
				
				// 두번째 검사 : 한줄 주석 라인일 경우
				if (singleComments != null) {
					
					// 찾았을 경우를 위한 플래그
					boolean isFound = false;
					
					// 탐색 시작
					for (String prefix : singleComments) {
						
						// 해당 라인의 제일 첫 단어가 한줄 주석인가?
						if (line.startsWith(prefix)) {
							isFound = true;
							break;
						}
					}
					
					// 찾았을 경우 카운팅 무시
					if (isFound) {
						continue;
					}
				}
				
				// 세번째 검사 : 그룹 주석 라인일 경우
				if (groupComments != null) {
					
					// 그룹 주석이 이미 열려있는 경우
					if (groupOpen) {
						
						readNext = true;
						
						// 해당 라인에 그룹 주석을 닫는 부호가 있는가?
						if (line.contains(closeBracket)) {
							groupOpen = false;
							readNext = false;
							closeIndex = line.indexOf(closeBracket) + closeBracket.length();
							
							// 그룹 주석을 닫는 부호가 맨 마지막에 있는가?
							if (line.endsWith(closeBracket)) {
								readNext = true;
							}
							
							closeBracket = null;
						}
						
						// 다음 처리를 수행
						continue;
						
					// 그룹 주석이 열려있지 않은 경우
					} else {
						boolean isContinue = false;
						boolean isFirst = true;
						
						// 해당 라인에서 닫힌 부호가 있는 경우 그 뒷부분만을 추출
						if (closeIndex != -1) {
							isFirst = false;
							line = line.substring(closeIndex);
							line.trim();
						}
						
						// 해당 라인에 그룹 주석 open이 있는지 추출
						for (List<String> fix : groupComments) {
							String prefix = fix.get(0);

							// 라인의 어딘가에 그룹 주석의 시작이 있을 경우
							if (line.contains(prefix)) {
								isContinue = true;
								groupOpen = true;
								readNext = false;
								closeBracket = fix.get(1);
								
								// 그룹 주석의 시작이 라인의 처음일 경우
								if (line.startsWith(prefix)) {
									break;
								}
								
								// 그룹 주석의 시작이 라인의 처음이 아닌 경우
								if (isFirst) {
									count++;
								}
							}
						}
						
						// 처리 재시작 플래그가 true면 처리 재시작
						if (isContinue) {
							continue;
						}
					}
				}
				
				// 라인수를 카운팅
				count++;
				
				// 다음행을 읽도록 플래그 수정
				readNext = true;
			}
		}
		
		return count;
	}
}
