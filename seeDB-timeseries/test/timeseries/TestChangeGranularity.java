package timeseries;

public class TestChangeGranularity {

	public static void main (String args[]) {
		
		DBSetting s = DBSetting.getDefault();
		
		SeeDB seedb = new SeeDB();
		seedb.connectToDatabase(s.database, s.databaseType, s.username, s.password);
		
		String target = "job";//"engineering";//"happy"; //votelittlemixuk (kca) //voteonedirection //vote5sos (mtv 5 seconds of summer)
		String[] candidates = seedb.getPopularHashtags(10);
		
		// min-level pre-binning and then aggregate to get hour-level results
		seedb.binTimeData("hashtags", "hashtags_by_min", false);
		long timeBefore = System.currentTimeMillis();
		seedb.computeCorrelationDiffGranularity(target, candidates);
		long timeAfter = System.currentTimeMillis();
		long elapsed = timeAfter - timeBefore;
	
		System.out.println("Change granularity from min to hour. elapsed time:" + elapsed);
		
		// hour-level binning and results
		timeBefore = System.currentTimeMillis();
		seedb.binTimeData("hashtags", "hashtags_by_hour", true);
		seedb.computeCorrelation(target, candidates);
		timeAfter = System.currentTimeMillis();
		elapsed = timeAfter - timeBefore;
		
		System.out.println("Re-binning by hourly. elapsed time:" + elapsed);
	}
}