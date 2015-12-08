package timeseries;

public class TestMetadata {

	public static void main (String args[]) {
		
		DBSetting s = DBSetting.getDefault();
		
		SeeDB seedb = new SeeDB();
		seedb.connectToDatabase(s.database, s.databaseType, s.username, s.password);
		
		//String[] candidates = seedb.getAllHashtags();
		for (int i = 0; i < 21; i ++) {
			String[] candidates = seedb.getPopularHashtags(i);
			System.out.println(i + ":" +candidates.length);
		}		
	}
}