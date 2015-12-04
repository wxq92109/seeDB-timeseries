package timeseries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class BinTimeseries {
	
	private String binnedData;

	public BinTimeseries(DBConnection conn, String toBin, String saveas) {
		binnedData = saveas;
		
		String query = "DROP TABLE " + binnedData + ";";
		conn.executeQueryWithoutResult(query);
		
		query = "SELECT DISTINCT\n"
						+ "date_trunc(\'hour\', \"timestamp\") AS \"hour\",\n"
						+ "hashtag,\n" 
						+ "count(hashtag) OVER (PARTITION BY hashtag, date_trunc(\'hour\', \"timestamp\")) AS \"cnt\"\n"
						+ "INTO hashtags_by_hour\n"
						+ "FROM hashtags\n"
						+ "ORDER BY hashtag, hour, cnt DESC;";
		
		conn.executeQueryWithoutResult(query);
		
		/*ResultSet rs = conn.executeQuery(query);
		while (rs.next()) {
			//Timestamp t = rs.getTimestamp(3);
			//String hashtag = rs.getString(2);
			//int id = rs.getInt(1);
			int id = rs.getInt(1);
		}
		rs.close();*/

	}
	
	public String getBinnedDataName() {
		return binnedData;
	}
}