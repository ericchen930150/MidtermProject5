package tw.com.eeit.midtermproject1.Action;

import java.util.Scanner;

import tw.com.eeit.midtermproject1.daodesign.ILaborUnionsDao;
import tw.com.eeit.midtermproject1.daodesign.LaborUnions;
import tw.com.eeit.midtermproject1.daodesign.LaborUnionsDaoFactory;
import tw.com.eeit.midtermproject1.util.Function;

public class Action {

	public static void main(String[] args) {
		try {
			ILaborUnionsDao dao = LaborUnionsDaoFactory.createUnionsDaoFactory();
			dao.createConn(); // 建立連線
			Scanner sc = new Scanner(System.in); // 建立Scanner物件
			
			A: while(true) {      // 使用者可以使用功能直到自行退出程式
				Function.list(); // 印功能表單
				int userInput = sc.nextInt(); // 使用者輸入要使用哪個功能
				sc.nextLine(); // 取出nextInt使用過後，在暫存剩下的/r，因為nextLine()讀到/r後會以為使用者已經輸入，導致nextLine()失效。
				
				while(userInput < 0 || userInput > 8) {      // 如果不是0~8重新輸入
					System.out.print("無效的數值，請重新輸入:");
					userInput = sc.nextInt();
					sc.nextLine();
				}
				
				switch(userInput) {
					case 0:{ // (0) 退出程式
						break A;
					}
					case 1:{ // (1) 新增資料
						System.out.println("請依序輸入【工會名稱】、【負責人】、【聯絡電話】、【行動號碼】、【聯絡地址】，並用【英文逗號,】來分隔");
						System.out.println("輸入格式：工會名稱,負責人,聯絡電話,行動號碼,聯絡地址,");
						String str = sc.nextLine();  // 輸入資料
						
						String[] tokens = Function.stringToTokens(str, sc); // 處理使用者輸入的字串，包含去除空白字元，切割、檢查使用者輸入值的數量等等。
						
						LaborUnions laborUnion = new LaborUnions(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4]); // 用建構子將值塞入
						dao.add(laborUnion);  // insert進LaborUnions table
						break;
					}
					case 2:{ // (2) 更新資料
						System.out.println("請依序輸入要更新的完整資料，包含【工會名稱】、【負責人】、【聯絡電話】、【行動號碼】、【聯絡地址】，並用【英文逗號,】來分隔");
						System.out.println("輸入格式：工會名稱,負責人,聯絡電話,行動號碼,聯絡地址,");
						
						while(true) { // 用while迴圈是因為使用者可能輸入錯誤的工會名稱，導致資料沒更新
							String str = sc.nextLine();  // 輸入資料

							String[] tokens = Function.stringToTokens(str, sc); // 處理使用者輸入的字串，包含去除空白字元，切割、檢查使用者輸入值的數量等等。
							
							System.out.print("工會舊資料:");
							String result = dao.select(tokens[0]); // 會先查詢舊資料
							if (result != null) {  // 先查詢是否有此工會，如果有才更新
								LaborUnions laborUnion = new LaborUnions(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4]); // 用建構子將值塞入
								dao.updateByName(laborUnion);  // updata進LaborUnions table
								System.out.print("工會更新後資料:"); // 印出更新後的資料
								dao.select(tokens[0]);
								break;
							} else { // 如果沒有此筆資料
								System.out.println("搜尋不到此【工會名稱】，請輸入正確的【完整資料】:");
							}
						}
						
						break; // Switch的break;
					}
					case 3:{ // (3) 根據【工會名字】刪除資料
						System.out.print("請輸入【工會名字】來刪除資料:");
						while(true) { // 用while迴圈是因為使用者可能輸入錯誤的工會名稱，導致資料沒刪除
							String unionName = sc.nextLine();  // 輸入資料
							
							System.out.print("工會舊資料:");
							String result = dao.select(unionName); // 會先查詢舊資料
							if (result != null) {  // 先查詢是否有此工會，如果有才刪除
								dao.deleteByName(unionName); // 從LaborUnions table刪除
								System.out.println("刪除成功");
								break;
							} else { // 如果沒有此筆資料
								System.out.println("搜尋不到此【工會名稱】，請輸入正確的【完整資料】:");
							}
						}
						
						break; // Switch的break;
					}
					case 4:{ // (4) 根據【工會名字】讀取資料
						System.out.print("請輸入【工會名字】來讀取資料:");
						String str = sc.nextLine();  // 輸入資料
						dao.select(str); // 從LaborUnions table讀取
						break;
					}
					case 5:{ // (5) 根據【工會名字】讀取資料並建立檔案於C:\\temp裡面
						System.out.print("請輸入【工會名字】來讀取資料並建立檔案於C:\\temp裡面:");
						String str = sc.nextLine();  // 輸入資料
						dao.findByName(str); // 從LaborUnions table讀取
						break;
					}
					case 6:{ // (6) 根據URL新增圖片到Images table
						System.out.println("請輸入想要設定的圖片名稱:");
						String imageName = sc.nextLine();  // 輸入資料
						System.out.println("請輸入圖片的URL:");
						String url = sc.nextLine();  // 輸入資料
						
						dao.inputImageByUrl(imageName, url);  // 輸入image到Images table。
						break;
					}
					case 7:{ // (7) 根據【檔案路徑】新增圖片到Images table
						System.out.println("請輸入想要設定的圖片名稱:");
						String imageName = sc.nextLine();  // 輸入資料
						System.out.println("請輸入圖片的檔案路徑:");
						String filedir = sc.nextLine();  // 輸入資料
						dao.inputImageByFile(imageName, filedir); // 輸入image到Images table。
						break;
					}
					default:{
						System.out.print("請輸入圖片的ID:");
						int imageID = sc.nextInt();  // 輸入資料
						dao.OutputImageById(imageID); // 從Images table輸出
						break;
					}
					
				}
				
				System.out.println();   // 調整印出格式用
			}
			
			System.out.println("系統結束");

			sc.close();      // 關閉Scanner
			dao.closeConn(); // 關閉連線
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
