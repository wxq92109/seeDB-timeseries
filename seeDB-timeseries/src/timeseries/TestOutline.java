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
		
		long timeBefore = System.currentTimeMillis();
		
		seedb.binTimeData("hashtags", "hashtags_by_hour");
		//seedb.binTimeData("hashtags", "hashtags_by_hour", Timestamp.valueOf("2015-02-24 08:00:00.0"), Timestamp.valueOf("2015-02-24 23:00:00.0"));
		System.out.println("binned data");
		
		String target = "job"; //votelittlemixuk (kca) //voteonedirection //vote5sos (mtv 5 seconds of summer)
		//String[] candidates = {"jobs", "kca", "tweetmyjobs", "vote1duk"};
		//String[] candidates = {"commx", "cuba", "thekindnessoffpeople", "votelittlemixuk", "kca2015", "kca"};
		String[] candidates = seedb.getPopularHashtags();
		//String[] candidates = seedb.getAllHashtags();
		seedb.computeCorrelation(target, candidates);
		//seedb.computeCorrelation(target);
		//seedb.computeCorrelationTimeWindow (target, Timestamp.valueOf("2015-02-24 08:00:00.0"), Timestamp.valueOf("2015-02-24 23:00:00.0")) ;
		//seedb.computeCorrelationNormalized(target);
		//seedb.computeCorrelationDiffGranularity(target, candidates);
		System.out.println("computed cross correlation");
		
		LinkedHashMap<String, HashMap<Timestamp, Double>> results = (LinkedHashMap<String, HashMap<Timestamp, Double>>) seedb.getHighlyCorrelated(10);
		System.out.println("results");
		//results.forEach((k, v) -> System.out.println(k)); // for min bucketing results
		results.forEach((k, v) -> System.out.println(k + "=" + v)); 
		//System.out.println(results);
		
		long timeAfter = System.currentTimeMillis();
		long elapsed = timeAfter - timeBefore;
		System.out.println("elapsed time:" + elapsed);
		
		//seedb.initialize(query, null, settings);
		//result = seedb.computeDifferenceWrapper();
		//result = seedb.computeDifference();
		//System.out.println(result.get(0));
	}
}