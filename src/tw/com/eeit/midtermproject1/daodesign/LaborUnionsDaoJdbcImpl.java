package tw.com.eeit.midtermproject1.daodesign;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LaborUnionsDaoJdbcImpl implements ILaborUnionsDao{ // 繼承剛剛定義的ILaborUnionsDao介面

	private Connection conn; // 接createConn()的返回值用，因為closeConn()也會用到。  全域變數。
	
	// 使用者會用到
	@Override
	public void add(LaborUnions laberUnions) throws SQLException {          // 透過LaborUnions物件來insert資料
		String sqlstr = "Insert into LaborUnions(unionName, leader, phone, cellPhone, address) Values(?, ?, ?, ?, ?)"; // 放MSSQL語法
		PreparedStatement state = conn.prepareStatement(sqlstr); // 利用Connection物件的method，input MSSQL語法後，建立PreparedStatement物件。
		state.setString(1, laberUnions.getUnionName()); // 第1個問號，放LaborUnions物件的UnionName。
		state.setString(2, laberUnions.getLeader());
		state.setString(3, laberUnions.getPhone());
		state.setString(4, laberUnions.getCellPhone());
		state.setString(5, laberUnions.getAddress());
		state.executeUpdate(); // 放完值之後，執行MSSQL語句。
		state.close(); // 關閉PreparedStatement資源。
	}

	@Override
	public void updateByName(LaborUnions laberUnions) throws SQLException {      // 透過PK key(UnionName)，更新其他所有的欄位。  (基本上跟Insert相同，只是MSSQL語句不同)
		String sqlstr = "Update LaborUnions set Leader=?, Phone=?, CellPhone=?, Address=? where UnionName=?"; // 放MSSQL語法
		PreparedStatement state = conn.prepareStatement(sqlstr);
		state.setString(1, laberUnions.getLeader());
		state.setString(2, laberUnions.getPhone());
		state.setString(3, laberUnions.getCellPhone());
		state.setString(4, laberUnions.getAddress());
		state.setString(5, laberUnions.getUnionName());
		state.executeUpdate();
		state.close();
	}

	@Override
	public void deleteByName(String unionName) throws SQLException {    // 透過PK key(UnionName)，刪除資料。
		unionName = unionName.trim(); // 先將頭尾的空白字元移除。
		String sqlstr = "Delete from LaborUnions where UnionName=?";
		PreparedStatement state = conn.prepareStatement(sqlstr);
		state.setString(1, unionName);
		state.executeUpdate();
		state.close();
	}

	@Override
	public String select(String unionName) throws SQLException { // 單純透過工會名字select資料  // 返回String因為findByName()會用到
		unionName = unionName.trim(); // 先將頭尾的空白字元移除。
		String sqlstr = "Select * from LaborUnions where UnionName=?"; // 放MSSQL語法
		PreparedStatement state = conn.prepareStatement(sqlstr); // 建立PreparedStatement物件
		state.setString(1, unionName); // 塞值
		ResultSet rs = state.executeQuery(); // 執行MSSQL敘述，返回結果，也就是ResultSet物件
		
		if(rs.next()) {      // 查詢如果有資料
			
			// 將查詢結果串聯印到Console
			String str = "";
			for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++) { // 查詢ResultSetMetaData看有幾欄
				if (i != rs.getMetaData().getColumnCount()) {      // 只是設計印出來的格式用
					str += rs.getString(i) + ",";
				} else {
					str += rs.getString(i);
				}
			}
			System.out.println(str);
			rs.close();
			state.close();
			return str; // 如果有資料返回資料
			
		} else {
			System.out.println("No data"); // 如果沒資料印 No data，不產生檔案。
			rs.close();
			state.close();
			return null; // 如果沒有資料返回null
		}
		
		
	}
	
	@Override
	public void findByName(String unionName) throws Exception {    // 透過PK key(UnionName)，查詢資料並建立檔案於C:\\temp裡面。       
		unionName = unionName.trim(); // 先將頭尾的空白字元移除。
		String str = select(unionName); // 接查詢結果
		if(str != null) {      // 查詢如果有資料
			
			// 於C:\temp目錄下建立檔案
			new File("C:\\temp").mkdir();   // 如果沒有目錄，建一個目錄，如果有會使用原本的
			String dir = "C:\\temp\\UnionName=" + unionName + ".txt"; // 檔名
			BufferedWriter out = new BufferedWriter(new FileWriter(dir)); // 建立BufferedWriter物件。
			out.write(str);   // 印出來
			out.flush();      // 因為是bufferedWriter，如果暫存區沒滿就不會寫出，因此強制把暫存區裡面的資料印出來。
			out.newLine();    // 換行
			out.close();
		} 
		
	}
	
	@Override
	public void inputImageByUrl(String imageName, String url) throws Exception { // 根據【URL】新增圖片到Images table
		imageName = imageName.trim();  // 先去除頭尾的空白
		url = url.trim();
		
		// 透過URL創建URL物件，並使用其中的openStream()方法，返回InputStram物件，再轉高階成BufferedInputStream。
		BufferedInputStream bis = new BufferedInputStream(new URL(url).openStream());
		
		String sqlstr = "Insert into Images(FileName, FileContent) values(?, ?)";
		PreparedStatement state = conn.prepareStatement(sqlstr);
		state.setString(1, imageName);
		state.setBinaryStream(2, bis);  // 第二個問號放Inputstream，多型
		state.executeUpdate();
		state.close();
		bis.close();
		System.out.println("Image Stored");

	}

	@Override
	public void inputImageByFile(String imageName, String fileDir) throws Exception { // 根據【檔案路徑】新增圖片到Images table
		imageName = imageName.trim();  // 先去除頭尾的空白
		fileDir = fileDir.trim();
		
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileDir));
			
		String sqlstr = "Insert into Images(FileName, FileContent) values(?, ?)";
		PreparedStatement state = conn.prepareStatement(sqlstr);
		state.setString(1, imageName);
		state.setBinaryStream(2, bis);  // 第二個問號放Inputstream，多型
		state.executeUpdate();
		state.close();
		bis.close();
		System.out.println("Image Stored");
	}

	@Override
	public void OutputImageById(int FileId) throws Exception { // 根據【圖片ID】下載圖片到C:\\temp裡面
		
		String sqlstr = "select * from Images where FileID=?"; // sql語句
		PreparedStatement state = conn.prepareStatement(sqlstr);
		state.setInt(1, FileId); // 設定第一個問號的值
		ResultSet rs = state.executeQuery(); // 執行select語句，返回ResultSet
		
		if (rs.next()) { // 如果有查詢到資料的話
			Blob blob = rs.getBlob(3); // 第三欄是圖片
			new File("C:\\temp").mkdir();
			String dir = "C:\\temp\\" + rs.getString(2) + ".jpg"; // 第二欄是圖片名稱
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dir));
			int length = (int) blob.length(); // 取得圖片多少bytes
			bos.write(blob.getBytes(1, length));  // 寫出全部資料
			bos.flush(); // 因為要緩存區滿才會寫出，所以這裡強制寫出

			bos.close(); // 關閉資源
			rs.close();
			state.close();
		} else {
			System.out.println("No Image");     // 如果沒有這個圖印出
		}
	}
	
	
	
	// 自己用
	@Override
	public void inputCsv() throws Exception { // input 原始LaborUnions.csv到MSSQL
		// 透過URL創建URL物件，並使用其中的openStream()方法，返回InputStram物件，input。
		InputStream input = new URL("https://odws.hccg.gov.tw/001/Upload/25/opendataback/9059/225/fb5a63cd-7580-4bc8-9c95-5e4ae0b645d8.csv").openStream();
		// 利用Input創建InputStreamReader物件讀取資料，然後轉高階的BufferedReader，減少IO操作次數，提高效能。
		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		
		LaborUnions union = new LaborUnions(); // 創一個LaborUnions object(後面會用)，放while外面是希望重複利用該物件。
			
		in.readLine(); // 跳過第一行，欄位名稱。
		String line; // 用來接當前讀取的那一行
		while ((line = in.readLine()) != null) { // 當後面有資料的時候繼續讀
			line = line.replace("\"", ""); // 去除雙引號"
			String[] tokens = line.split(","); // 並用逗號切割
				
			for (int i = 0; i < tokens.length; i++) { // 看資料有幾欄就要跑幾次
				// 如果此token[i]元素是空字串，而不是null時，先把換成null值，不然如果有些欄位是允許多個null，但非null值不能重複時，就會出錯。 雖然本次沒有此狀況。
				if (tokens[i] == "") {tokens[i] = null;}
					
				// 並用 mod 來將相對應的元素丟給相對應的成員變數。
				if (i % tokens.length == 0) { union.setUnionName(tokens[i]);} 
				else if (i % tokens.length == 1) {union.setLeader(tokens[i]);} 
				else if (i % tokens.length == 2) {union.setPhone(tokens[i]);} 
				else if (i % tokens.length == 3) {union.setCellPhone(tokens[i]);} 
				else {
					union.setAddress(tokens[i]);
					add(union); // 最後填完成員變數，insert到資料庫
				}
			}
		}
		
		in.close();
		input.close();
		
	}
	
	@Override
	public void createConn() throws SQLException { // 建立連線
		final String MSSQL_URL = "jdbc:sqlserver://localhost:1433;databaseName=MidtermDB1;user=watcher;password=P@ssw0rd;encrypt=true;trustServerCertificate=true"; // 建立連線的URL
		conn = DriverManager.getConnection(MSSQL_URL);       // 使用DriverManager的getConnection方法，建立連線
		//System.out.println("Connection Status:" + !conn.isClosed());    // 確認成功連上
	}

	@Override
	public void closeConn() throws SQLException { // 關閉連線
		if (conn != null) { // 如果有連線的話關掉連線。
			conn.close();
		}
		//System.out.println("Connection Status:" + !conn.isClosed());
	}



	// 驗證
	public static void main(String[] args) throws Exception {
		LaborUnionsDaoJdbcImpl union = new LaborUnionsDaoJdbcImpl();
		union.createConn();
		
//		union.select("新竹市泥水業職業工會"); // 成功
//		union.select("工會"); // 成功
//		
		union.inputCsv(); // 成功
//		
//		union.add(new LaborUnions("公會","陳冠廷","(02)28340270",null,"地址")); // add成功
//		
//		union.updateByName(new LaborUnions("公會","陳俊廷1","(02)28340270",null,"地址")); // updata原本有的工會名字，成功
//		union.updateByName(new LaborUnions("公會你好","陳俊廷","(02)28340270",null,"地址")); // updata不存在的工會，會沒有加進去。
//		
//		union.deleteByName("公會"); // delete原本有的工會名字，成功
//		union.deleteByName("公會你好"); // delete不存在的工會，會沒有效果。
//		
//		union.findByName("新竹市總工會");        // 成功
//		union.findByName("公會你好");     // 成功
//		
//		union.inputImageByUrl("https://assets-global.website-files.com/60d196db74a1e36ff16ebd5d/63a2bff59eb881cddf87ee60_elena-mozhvilo-UspYqrVBsIo-unsplash.png"); // 成功
//		union.inputImageByFile("C:\\temp\\q6CVnZ2YlKOeqKM.jpg"); // 成功
//		union.OutputImageById(1); // 成功
//		union.OutputImageById(3); // 成功
		
		union.closeConn();
	}
	
	

}
