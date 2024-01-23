package tw.com.eeit.midtermproject1.daodesign;

import java.io.Serializable;

public class LaborUnions implements Serializable{

	// 成員變數
	private static final long serialVersionUID = 1L;  // 序列化
	private String unionName;
	private String leader;
	private String phone;
	private String cellPhone;
	private String address;
	
	// 無參數的建構子
	public LaborUnions() {
		super(); // 找上層無參數的建構子
	}
	
	// 全參數的建構子。Action class，使用者insert和update資料時會用到。
	public LaborUnions(String unionName, String leader, String phone, String cellPhone, String address) {
		super();
		this.unionName = unionName;
		this.leader = leader;
		this.phone = phone;
		this.cellPhone = cellPhone;
		this.address = address;
	}


	// getter / setter
	public String getUnionName() {
		return unionName;
	}

	public void setUnionName(String unionName) {
		this.unionName = unionName;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
		
}
