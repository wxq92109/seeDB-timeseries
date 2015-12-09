package timeseries;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class TestMetadata {

	public static void main (String args[]) {
		
		DBSetting s = DBSetting.getDefault();
		
		SeeDB seedb = new SeeDB();
		seedb.connectToDatabase(s.database, s.databaseType, s.username, s.password);
		
		seedb.binTimeData("hashtags", "hashtags_by_hour", true);
		System.out.println("binned data");
		
		String target = "job";
		for (int i = 0; i < 11; i ++) {
			String[] candidates = seedb.getPopularHashtags(i);
			
			long timeBefore = System.currentTimeMillis();
			seedb.computeCorrelation(target, candidates);
			long timeAfter = System.currentTimeMillis();
			long elapsed = timeAfter - timeBefore;
			
			LinkedHashMap<String, HashMap<Timestamp, Double>> results = (LinkedHashMap<String, HashMap<Timestamp, Double>>) seedb.getHighlyCorrelated(10);
			//results.forEach((k, v) -> System.out.println(k + "=" + v)); 
			//results.forEach((k, v) -> System.out.print(k + " "));
			
			System.out.println(i + ":" + candidates.length + ", elapsed time:" + elapsed);
		}		
	}
}