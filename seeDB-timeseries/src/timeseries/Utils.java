package timeseries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Utils {
	private DBConnection conn;
	
	public Utils(DBConnection conn) {
		this.conn = conn;
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
	
}