package timeseries;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class TestTimewindow {

	public static void main (String args[]) {
		
		DBSetting s = DBSetting.getDefault();
		
		SeeDB seedb = new SeeDB();
		seedb.connectToDatabase(s.database, s.databaseType, s.username, s.password);
		
		String target = "job";//"engineering";//"happy"; //votelittlemixuk (kca) //voteonedirection //vote5sos (mtv 5 seconds of summer)
		String[] candidates = seedb.getPopularHashtags(10);
		
		//seedb.binTimeData("hashtags", "hashtags_by_hour");
		int intervalNum = 15;
		int sampleNum = 10;
		long[][] elapsedTimes = new long[intervalNum+1][sampleNum];
		double[][] sizes = new double [intervalNum+1][sampleNum];
		for (int i = 1; i <= intervalNum; i ++) {
			for (int repeat = 0; repeat < sampleNum; repeat ++) {
				Random r = new Random();
				int startInt = r.nextInt(23-i);
				String startTime = "" + startInt;
				if (startInt < 10) startTime = "0" + startTime;
				
				int endInt = startInt + i;
				String endTime = "" + endInt;
				if (endInt < 10) endTime = "0" + endTime;
				
				long timeBefore = System.currentTimeMillis();
				
				seedb.binTimeData("hashtags", "hashtags_by_hour_window", Timestamp.valueOf("2015-02-24 " + startTime + ":00:00.0"), Timestamp.valueOf("2015-02-24 " + endTime + ":00:00.0"));
				seedb.computeCorrelationTimeWindow (target, candidates, Timestamp.valueOf("2015-02-24 " + startTime + ":00:00.0"), Timestamp.valueOf("2015-02-24 " + endTime + ":00:00.0"));
				
				long timeAfter = System.currentTimeMillis();
				long elapsed = timeAfter - timeBefore;
				
				double size = seedb.getTableSize("hashtags_by_hour_window");
				
				//System.out.println(Timestamp.valueOf("2015-02-24 " + startTime + ":00:00.0").toString() + " " + Timestamp.valueOf("2015-02-24 " + endTime + ":00:00.0").toString());
				//System.out.println("elapsed time:" + elapsed);
				elapsedTimes[i-1][repeat] = elapsed;
				sizes[i-1][repeat] = size;
			}
		}
		double[] avgElapsed = new double[intervalNum];
		double[] avgSize = new double[intervalNum];
		double[] stdElapsed = new double[intervalNum];
		double[] stdSize = new double[intervalNum];
		
		for (int i = 1; i <= intervalNum; i ++) {
			DescriptiveStatistics stats1 = new DescriptiveStatistics();
			DescriptiveStatistics stats2 = new DescriptiveStatistics();
			for (int repeat = 0; repeat < sampleNum; repeat ++) {
				stats1.addValue(elapsedTimes[i-1][repeat]);
				stats2.addValue(sizes[i-1][repeat]);
			}
			avgElapsed[i-1] = stats1.getMean();
			avgSize[i-1] = stats2.getMean();
			stdElapsed[i-1] = stats1.getStandardDeviation();
			stdSize[i-1] = stats2.getStandardDeviation();
		}
		//System.out.println(Arrays.deepToString(elapsedTimes));
		//System.out.println(Arrays.deepToString(sizes));
		System.out.println(Arrays.toString(avgElapsed));
		System.out.println(Arrays.toString(stdElapsed));
		System.out.println(Arrays.toString(avgSize));
		System.out.println(Arrays.toString(stdSize));
		
	}
}