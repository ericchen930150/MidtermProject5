package tw.com.eeit.midtermproject1.daodesign;

public class LaborUnionsDaoFactory {
	
	// 用來new LaborUnionsDaoJdbcImpl object
	public static ILaborUnionsDao createUnionsDaoFactory() {
		return new LaborUnionsDaoJdbcImpl();
	}

}
