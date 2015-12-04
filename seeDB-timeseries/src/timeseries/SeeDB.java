package timeseries;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * This class defines all the functions that the front end can use to 
 * communicate with the backend
 * 
 */
public class SeeDB {
	private DBConnection connection;							// connection used to access the database
	private BinTimeseries bt;
	private CrossCorrelation cc;
	
	/**
	 * default constructor	
	 */
	public SeeDB() {
		connection = new DBConnection();
		bt = null;
	}
	
	/**
	 * few getters
	 * @return
	 */
	
	public DBConnection getConnection() {
		return connection;
	}
	
	/**
	 * Connect to a database. Each dataset can connect to only one database
	 * @param database String with which to connect to the database
	 * @param databaseType Type of database to connect to
	 * @param username username for database
	 * @param password password for database
	 * @return true if connection is successful    	
	 */
	public boolean connectToDatabase(String database, String databaseType, 
			String username, String password) {
		return connection.connectToDatabase(database, databaseType, 
				username, password);
	}
	
	/**
	 * connectToDatabase with default settings
	 * @param datasetNum
	 * @return
	 */
	public boolean connectToDatabase() {
		return connection.connectToDatabase(DBSetting.getDefault());
	}

	public void closeConnection() {
		this.connection.close();
	}

	public void connectToDatabase(DBSetting dbSetting) {
		this.connectToDatabase(dbSetting.database, dbSetting.databaseType, dbSetting.username, dbSetting.password);
		
	}
	
	/**
	 * Create binned format of data and save as a table in the database. 
	 * Save # of occurrences of hashtags over 1 hour granularity of time
	 * @param toBin table to do binning
	 * @param saveas table name of the binned data 
	 */
	public void binTimeData(String toBin, String saveas) {
		bt = new BinTimeseries(connection, toBin, saveas);
	}
	
	/**
	 * Compute cross correlation coefficients between hashtag and candidate,
	 * and return top n candidate hashtags with highest correlation.
	 * @param hashtag given by user
	 * @param candidates candidate hashtags
	 */
	public void computeCorrelation(String target, String[] candidates) {
		cc = new CrossCorrelation(connection, target, candidates, bt.getBinnedDataName());
	}
	
	public void computeCorrelation(String target) {
		cc = new CrossCorrelation(connection, target, bt.getBinnedDataName());
	}
	
	public HashMap<String, HashMap<Timestamp, Double>> getHighlyCorrelated(int n) {
		return cc.getHighlyCorrelated(n);	
	}
}
