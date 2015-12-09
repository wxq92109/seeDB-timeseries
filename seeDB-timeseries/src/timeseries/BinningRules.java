package timeseries;

import java.sql.Timestamp;

public class BinningRules {
	
	private Timestamp startTime;
	private Timestamp endTime;
	
	public BinningRules(Timestamp startTime2, Timestamp endTime2) {
		this.startTime = startTime2;
		this.endTime = endTime2;
	}
	
	// determines whether we should bin by hour or minute, based on user's start and end time
	public String determineBinGranularity() {
		return "test";
	}

}
