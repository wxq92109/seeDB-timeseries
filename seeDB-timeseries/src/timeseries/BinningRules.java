package timeseries;

import java.sql.Timestamp;

public class BinningRules {
	
	private Timestamp startTime;
	private Timestamp endTime;
	private final long THRESHOLD = 180;// 3 hours
	// if predictBin is true, we will bin by hour if timewindow > 3 hours - 12:06 - 17:53 -> 12:00 - 18:00; minute otherwise
	// otherwise, default to bin by minute
	private boolean predictBin = true;
	public BinningRules(Timestamp startTime2, Timestamp endTime2) {
		this.startTime = startTime2;
		this.endTime = endTime2;
	}
	
	// determines whether we should bin by hour or minute, based on user's start and end time
	public String determineBinGranularity() {
		
		if (!predictBin) {
			// if it's clean hour to hour, bin by hour
			if (startTime.getMinutes() == 0 && endTime.getMinutes() == 0) {
				return "hour";
			} else {
				return "minute";
			}
		}

		
	    long ms1 = startTime.getTime();
	    long ms2 = endTime.getTime();
	    long diff = ms2 - ms1;
	    long diffMinutes = diff / (60 * 1000);
	    if (diffMinutes < THRESHOLD ) {
	    	return "minute";
	    } else {
	    	return "hour";
	    }
	}

}
