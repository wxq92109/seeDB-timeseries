package timeseries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class BinTimeseries {
	
	private String binnedData;

	public BinTimeseries(DBConnection conn, String toBin, String saveas) {
		binnedData = saveas;
		
		String query = "DROP TABLE IF EXISTS " + binnedData + ";";
		conn.executeQueryWithoutResult(query);
		
		query = "SELECT DISTINCT\n"
						+ "date_trunc(\'hour\', \"timestamp\") AS \"hour\",\n"
						+ "hashtag,\n" 
						+ "count(hashtag) OVER (PARTITION BY hashtag, date_trunc(\'hour\', \"timestamp\")) AS \"cnt\"\n"
						+ "INTO hashtags_by_hour\n"
						+ "FROM hashtags\n"
						+ "ORDER BY hashtag, hour, cnt DESC;";
		/*query = "SELECT DISTINCT\n"
				+ "date_trunc(\'min\', \"timestamp\") AS \"min\",\n"
				+ "hashtag,\n" 
				+ "count(hashtag) OVER (PARTITION BY hashtag, date_trunc(\'min\', \"timestamp\")) AS \"cnt\"\n"
				+ "INTO hashtags_by_min\n"
				+ "FROM hashtags\n"
				+ "ORDER BY hashtag, min;";*/
		
		conn.executeQueryWithoutResult(query);

	}
	
	public String getBinnedDataName() {
		return binnedData;
	}
}