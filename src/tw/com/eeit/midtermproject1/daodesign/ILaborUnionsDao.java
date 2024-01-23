package tw.com.eeit.midtermproject1.daodesign;

import java.sql.SQLException;

public interface ILaborUnionsDao {
	
	// 使用者會用到
	public void add(LaborUnions laberUnions) throws SQLException; // 透過LaborUnions物件來insert資料
	public void updateByName(LaborUnions laberUnions) throws SQLException; // 透過LaborUnions物件來update資料
	public void deleteByName(String unionName) throws SQLException; // 透過工會名字delete資料
	public String select(String unionName) throws SQLException; // 單純透過工會名字select資料  // 返回資料String因為findByName()會用到
	public void findByName(String unionName) throws Exception; // 透過工會名字select資料，並寫出檔案。
	public void inputImageByUrl(String imageName, String url) throws Exception; // 根據【URL】新增圖片到Images table
	public void inputImageByFile(String imageName, String fileDir) throws Exception; // 根據【檔案路徑】新增圖片到Images table
	public void OutputImageById(int FileId) throws Exception; // 根據【圖片ID】下載圖片到C:\temp裡面
	
	
	// 自己用
	public void inputCsv() throws Exception; // 最剛開始輸入資料用
	public void createConn() throws SQLException; // 建立連線。
	public void closeConn() throws SQLException; // 關閉連線

}
