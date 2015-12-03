package timeseries;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class TestOutline {

	public static void main (String args[]) {
		
		DBSetting s = DBSetting.getDefault();
		
		SeeDB seedb = new SeeDB();
		seedb.connectToDatabase(s.database, s.databaseType, s.username, s.password);
		System.out.println("connected to db");
		
		seedb.binTimeData("hashtags", "binned_hashtags");
		System.out.println("binned data");
		
		String target = "job";
		String[] candidates = {"jobs", "kca", "tweetmyjobs", "vote1duk"};
		seedb.computeCorrelation(target, candidates);
		System.out.println("computed cross correlation");
		
		LinkedHashMap<String, HashMap<Timestamp, Double>> results = (LinkedHashMap<String, HashMap<Timestamp, Double>>) seedb.getHighlyCorrelated(1);
		System.out.println(results);
		
		//seedb.initialize(query, null, settings);
		//result = seedb.computeDifferenceWrapper();
		//result = seedb.computeDifference();
		//System.out.println(result.get(0));
	}
}