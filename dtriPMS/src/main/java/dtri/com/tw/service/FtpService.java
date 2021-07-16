package dtri.com.tw.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import dtri.com.tw.tools.Fm_Time;

@Service
public class FtpService {
	private FTPClient ftpClient;

	/**
	 * Description: 從FTP伺服器下載檔案
	 * 
	 * @Version1.0
	 * 
	 * @param url        FTP伺服器hostname
	 * @param port       FTP伺服器埠
	 * @param username   FTP登入賬號
	 * @param password   FTP登入密碼
	 * @param remotePath FTP伺服器上的相對路徑
	 * @param fileName   要下載的檔名
	 * @param localPath  下載後儲存到本地的路徑
	 * @return
	 */
	public JSONArray downFile(String url, int port, String username, String password, //
			String remotePath, String remotePathBackup, String[] searchName,//
			String localPath,String work_use) {
		System.out.println(new Date());
		JSONArray list = new JSONArray();
		ftpClient = new FTPClient();
		try {
			// 登入 如果採用預設埠，可以使用ftp.connect(url)的方式直接連線FTP伺服器
			ftpClient.connect(url, port);
			ftpClient.login(username, password);
			// 設定檔案傳輸型別為二進位制+UTF-8 傳輸
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.setControlEncoding("UTF-8");
			// 獲取ftp登入應答程式碼
			int reply = ftpClient.getReplyCode();
			// 驗證是否登陸成功
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				System.err.println("FTP server refused connection.");
				return null;
			}
			// 轉移到FTP伺服器目錄至指定的目錄下
			ftpClient.changeWorkingDirectory(new String(remotePath.getBytes("UTF-8"), "iso-8859-1"));

			// 獲取檔案列表(查詢)
			JSONObject one = new JSONObject();
			FTPFile[] fs = ftpClient.listFiles();
			ByteArrayOutputStream is = new ByteArrayOutputStream();
			String line = "", line_all = "";

			for (FTPFile ff : fs) {
				String[] f_n = ff.getName().split("_");
				// 比對_查詢條件
				// 符合條件
				if ((searchName[0].equals("") || f_n[0].indexOf(searchName[0]) != -1)
						&& (searchName[1].equals("") || f_n[1].indexOf(searchName[1]) != -1)
						&& (searchName[2].equals("") || f_n[2].indexOf(searchName[2]) != -1)) {
					/** 不使用下載 **/
					// File localFile = new File(localPath + "/" + ff.getName());
					// OutputStream is = new FileOutputStream(localFile);
					is.reset();
					ftpClient.retrieveFile(ff.getName(), is);
					BufferedReader bufferedReader = new BufferedReader(new StringReader(is.toString("UTF-8")));

					// 第一行抓取+檢驗 (檔頭BOM)去除 是否為正確
					line = bufferedReader.readLine();
					line_all = bufferedReader.readLine();
					if (line != null && line.charAt(0) != '{') {
						line = line.substring(1);
					}
					// 取得所有內容
					StringBuilder everything = new StringBuilder();
					while ((line_all = bufferedReader.readLine()) != null) {
						everything.append(line_all);
						everything.append(System.lineSeparator());
					}

					// 字串 轉 JSON
					// System.out.println(line);
					// System.out.println(new Date() + " | " + ff.getName() + " " + nb++);
					// 如果異常為空
					try {
						new JSONObject(line);
					} catch (Exception ex) {
//						long file_size = ff.getSize();
						one = new JSONObject();
//						one.put("ph_pr_id", f_n[0]);
//						one.put("ph_model", f_n[1]);
//						one.put("pb_sn", f_n[2]);
//						one.put("pb_l_size", file_size);
//						one.put("pb_l_text", everything.toString());
						one.put("check", false);
						list.put(one);
						continue;
					}
					// 轉移檔案
					one = new JSONObject(line);
					String dirPath = remotePathBackup + one.getString("WorkOrder");
					// makeDirectories(ftpClient, dirPath);
					boolean created = ftpClient.makeDirectory(dirPath);

					String re_path = remotePath + "/" + ff.getName();
					String new_path = dirPath + "/" + ff.getName() + "_"+work_use+"_ "+Fm_Time.to_yMd_Hms(new Date());
					ftpClient.rename(re_path, new_path);

					// 補型號/補主機板號/轉16進制
					long file_size = ff.getSize();
					
					// 製令單 排除OQC
					if (f_n[0].indexOf("OQC") != -1) {
						one.put("check", false);
					} else {
						one.put("ph_pr_id", one.getString("WorkOrder"));
						String mbSn[] = (one.getString("UUID").split("-"));
						one.put("MB(UUID)", mbSn[mbSn.length - 1]);
						one.put("ph_model", "");
						one.put("pb_sn", one.getString("SN"));
						one.put("pb_l_size", file_size);
						one.put("pb_l_text", everything.toString());
						one.put("pb_l_path", new_path);
						one.put("check", true);
					}

					list.put(one);

					// 關閉串流
					bufferedReader.close();
				}
			}
			is.close();
			ftpClient.logout();
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject one = new JSONObject();
			one.put("check", false);
			list.put(one);
			return list;
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		System.out.println(new Date());
		return list;
	}

	/**
	 * Creates a nested directory structure on a FTP server 創建資料夾
	 * 
	 * @param ftpClient an instance of org.apache.commons.net.ftp.FTPClient class.
	 * @param dirPath   Path of the directory, i.e /projects/java/ftp/demo
	 * @return true if the directory was created successfully, false otherwise
	 * @throws IOException if any error occurred during client-server communication
	 */
	public static boolean makeDirectories(FTPClient ftpClient, String dirPath) throws IOException {
		String[] pathElements = dirPath.split("/");
		if (pathElements != null && pathElements.length > 0) {
			for (String singleDir : pathElements) {
				if (singleDir.equals("")) {
					continue;
				}
				boolean existed = ftpClient.changeWorkingDirectory(singleDir);
				if (!existed) {
					boolean created = ftpClient.makeDirectory(singleDir);
					if (created) {
						System.out.println("CREATED directory: " + singleDir);
						ftpClient.changeWorkingDirectory(singleDir);
					} else {
						System.out.println("COULD NOT  create directory: " + singleDir);
						return false;
					}
				}
			}
		}
		return true;
	}

}