package timeseries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DistinctHashtags {
	ArrayList<String> hashtags;
	
	public DistinctHashtags (DBConnection conn) {
		String query = "SELECT DISTINCT hashtag FROM hashtags;";
		ResultSet rs = conn.executeQuery(query);
		
		try {
			while (rs.next()) {
				hashtags.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getDistinctHashtags () {
		return hashtags;
	}
}