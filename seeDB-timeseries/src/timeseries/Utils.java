package timeseries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Utils {
	private DBConnection conn;
	
	public Utils(DBConnection conn) {
		this.conn = conn;
	}
	
	public Utils() {
		
	}
	public double getTableSize(String table) {
		double size = 0;
		String query = "select pg_size_pretty(pg_relation_size(\'" + table + "\'));";
		ResultSet rs = conn.executeQuery(query);
		try {
			while (rs.next()) {
				String withUnit = rs.getString(1);
				String[] parsed = withUnit.split(" ");
				size = Double.parseDouble(parsed[0]);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return size;
	}
	
	public Timestamp convertUserTime(String startTime) {
		System.out.println(startTime);
		String todaysDate = "2015-02-24";
		DateFormat readFormat = new SimpleDateFormat("hh:mm aa");
		DateFormat writeFormat = new SimpleDateFormat("HH:mm:ss.S");
		Date time = null;
		String result = "";
	    try {
			time = readFormat.parse(startTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    if( time != null ) {
	        String formattedTime = writeFormat.format(time);
		    result = todaysDate + " " + formattedTime;
	    }

		return Timestamp.valueOf(result);
		
	}
	
}