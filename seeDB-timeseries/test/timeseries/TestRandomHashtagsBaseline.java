package timeseries;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

public class TestRandomHashtagsBaseline {

	public static void main (String args[]) {
		
		DBSetting s = DBSetting.getDefault();
		
		SeeDB seedb = new SeeDB();
		seedb.connectToDatabase(s.database, s.databaseType, s.username, s.password);
		int runs = 10;
		int threshold = 10;
		
		String[] candidateHashtags = seedb.getPopularHashtags(threshold);
		String[] targetHashtags = new String[runs];
		// choose 10 random hashtags:
		
		Timestamp startTime = Timestamp.valueOf("2015-02-24 00:00:00.0");
		Timestamp endTime = Timestamp.valueOf("2015-02-24 23:00:00.0");
		boolean bool = true;
		HashMap<String, Long> results = new HashMap<String, Long>();

		for (int i=0; i<runs; i++) {
			Random random = new Random();
			int index = random.nextInt(candidateHashtags.length);
			targetHashtags[i] = candidateHashtags[index];
		}
		System.out.println("Random hashtags: " + targetHashtags);
	
		for (int i=0; i<runs; i++) {
			String target = targetHashtags[i];
			System.out.println("Testing target: " + target);

			long timeBefore = System.currentTimeMillis();
			seedb.binTimeData("hashtags", "hashtags_by_hour_window", startTime, endTime, bool);
			String[] candidates = seedb.getPopularHashtags(threshold);
			seedb.computeCorrelationTimeWindow(target, candidates, startTime, endTime, bool);
			LinkedHashMap<String, HashMap<Timestamp, Double>> seedbResults = (LinkedHashMap<String, HashMap<Timestamp, Double>>) seedb.getHighlyCorrelated(5);
			long timeAfter = System.currentTimeMillis();
			long elapsed = timeAfter - timeBefore;
			System.out.println("Finished: " + target);

			results.put(target, elapsed);
		}
		System.out.println("Results" + results);
		System.out.println(results.keySet());
		System.out.println(results.values());
	}

}
