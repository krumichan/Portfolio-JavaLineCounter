package jp.co.atelier.SourceLineCounter.utility;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;

/**
 * 유틸리티 클래스
 * 기능 : 파일 관련한 기능들을 제공
 *
 */
public final class FileUtility {
	
	/**
	 * 객체 생성 방지
	 */
	private FileUtility() {
		
	}
	
	/**
	 * 지정된 경로의 파일 또는 디렉토리가 존재하는지 확인
	 * @param filePath 지정된 경로
	 * @return 확인 결과
	 */
	public static boolean exists(String filePath) {
		return new File(filePath).exists();
	}
	
	/**
	 * 대상 경로로부터 모든 디렉토리를 습득
	 * @param curPath 대상 경로
	 * @param deepFlag 서브 디렉토리까지 탐색할지의 여부
	 * @return 습득한 모든 디렉토리 경로를 가지고 있는 Set
	 * @throws Exception 흭득 실패
	 */
	public static Set<String> readDirectories(String curPath, boolean deepFlag) throws Exception {
		Set<String> directories = new HashSet<String>();
				
		// 현재 패스에서 모든 디렉토리 추출
		directories.addAll(readDirectories(curPath));
		
		// 깊이 플래그가 설정되어 있다면, 서브 디렉토리 탐색을 개시
		if (deepFlag) {
			
			// 습득한 디렉토리를 저장할 임시 Set
			Set<String> tmpDirectories = new HashSet<String>();
			
			// 현재 패스에서 습득한 모든 디렉토리를 순환
			for (String subPath : directories) {
				// sub 디렉토리를 재귀적으로 호출
				tmpDirectories.addAll(readDirectories(subPath, deepFlag));
			}
			
			// 습득한 디렉토리들을 본 directories에 저장
			directories.addAll(tmpDirectories);
		}
		
		// 현재 패스를 추가
		// 중복이 있을 경우, Set의 특성상 무시
		directories.add(curPath);
		
		return directories;
	}
	
	/**
	 * 대상 경로로부터 모든 디렉토리를 습득
	 * @param path 대상 경로
	 * @return 습득한 모든 디렉토리 경로를 가지고 있는 Set
	 * @throws Exception 흭득 실패
	 */
	public static Set<String> readDirectories(String path) throws Exception {
		Set<String> paths = new HashSet<String>();
		
		// 경로 문자열을 File로 변환
		File curPath = new File(path);
		
		// 인수로 받은 경로가 디렉토리인지 확인
		if (!curPath.isDirectory()) {
			throw new Exception("경로:" + path + "는 디렉토리가 아닙니다.");
		}
		
		// 변환한 File로부터 모든 파일 및 디렉토리 추출
		File files[] = curPath.listFiles();
		
		// 위에서 추출한 모든 파일 및 디렉토리에서 디렉토리만 추출
		for (File file : files) {
			if (file.isDirectory()) {
				paths.add(file.getPath());
			}
		}
		
		return paths;
	}
	
	/**
	 * 대상 경로로부터 모든 파일을 습득 ( 디렉토리 제외 )
	 * @param pathList 대상 경로를 가지고 있는 리스트
	 * @param extensions 습득할 대상의 확장자 ( null일 경우 모든 파일이 대상 )
	 * @return 습득한 모든 파일 경로를 가지고 있는 Set
	 * @throws Exception 습득 실패
	 */
	public static Set<String> readFiles(Set<String> pathList, List<String> extensions) throws Exception {
		Set<String> files = new HashSet<String>();
		
		// 해당 경로로부터 대상 확장자를 가진 모든 파일 습득
		for (String path : pathList) {
			files.addAll(readFiles(path, extensions));
		}
		
		return files;
	}
	
	/**
	 * 대상 경로로부터 모든 파일을 습득 ( 디렉토리 제외 )
	 * @param path 대상 경로
	 * @param extensions 습득할 대상의 확장자 ( null일 경우 모든 파일이 대상 )
	 * @return 습득한 모든 파일 경로를 가지고 있는 Set
	 * @throws Exception 습득 실패
	 */
	public static Set<String> readFiles(String path, List<String> extensions) throws Exception {
		Set<String> files = new HashSet<String>();
		
		// 경로 문자열을 File로 변환
		File curPath = new File(path);
		
		// 인수로 받은 경로가 디렉토리인지 확인
		if (!curPath.isDirectory()) {
			throw new Exception("경로:" + path + "는 디렉토리가 아닙니다.");
		}
		
		// 변환한 File로부터 모든 파일 및 디렉토리 추출
		File tmpFiles[] = curPath.listFiles();
		
		// 습득한 모든 파일 순회
		for (File file : tmpFiles) {
			
			// 현재 file이 정말 file인가?
			if (file.isFile()) {
				
				// 파일로부터 path 추출
				String filePath = file.getPath();
				
				// path로부터 확장자 추출
				String extension = FilenameUtils.getExtension(filePath);
				
				// 확장자가 지정되어있지 않다면, 모든 파일을 추가한다.
				if (CheckUtility.isNullOrEmpty(extensions)) {
					files.add(filePath);
				} else {
					// 추출한 확장자가 대상 확장자에 포함되는가?
					if (extensions.contains(extension)) {
						files.add(filePath);
					}
				}
			}
		}
		
		return files;
	}
	
	/**
	 * 현재 디렉토리 위치를 습득
	 * @return 현재 디렉토리 위치
	 */
	public static Path getCWD() {
		String cwd = System.getProperty("user.dir", "");
		Path cwdPath = Paths.get(cwd);
		return cwdPath;
	}
}
