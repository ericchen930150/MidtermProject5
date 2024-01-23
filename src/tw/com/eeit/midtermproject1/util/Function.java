package tw.com.eeit.midtermproject1.util;

import java.util.Scanner;

public class Function { // Action會用到的一些功能
	
	// 印功能清單用
	public static void list() {
		System.out.println("請選擇功能：");
		System.out.println("(0) 退出程式");
		System.out.println("(1) 新增資料");
		System.out.println("(2) 更新資料");
		System.out.println("(3) 根據【工會名字】刪除資料");
		System.out.println("(4) 根據【工會名字】讀取資料");
		System.out.println("(5) 根據【工會名字】讀取資料並建立檔案於C:\\temp裡面");
		System.out.println("(6) 根據【URL】新增圖片");
		System.out.println("(7) 根據【檔案路徑】新增圖片");
		System.out.println("(8) 根據【圖片ID】下載圖片到C:\\temp裡面");
		System.out.print("請輸入0~8：");
	}
	
	// 去除空白字元、用,分割、檢查使用者是不是輸入5個值，將空白字元轉換成null，不然MS SQL not null欄還是可以insert進去。
	public static String[] stringToTokens(String str, Scanner sc) {
		String[] tokens = str.replace(" ", "").split(","); // 去除空白字元後，用,切割成token
		
		while(tokens.length != 5) {  // 如果使用者不是輸入五個值，重新輸入
			System.out.println("沒有依規定輸入，請重新輸入");
			str = sc.nextLine();
			tokens = str.replace(" ", "").split(","); // 去除空白字元後，用,切割成token
		}
		
		for(int i = 0; i < tokens.length; i++) {   // 將空字元字元換成null
			if(tokens[i] == "") {
				tokens[i] = null;
			}
		}
		
		return tokens;
	}

}
