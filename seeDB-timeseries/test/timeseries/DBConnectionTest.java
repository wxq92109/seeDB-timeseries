package timeseries;
import static org.junit.Assert.*;

import org.junit.Test;

import timeseries.DBConnection;
import timeseries.DBSetting;

public class DBConnectionTest {

	@Test
	public void isDBSupportedTest() {
		assertTrue(DBConnection.isDBSupported("postgreSQL"));
		assertTrue(DBConnection.isDBSupported("PostgreSQL"));
		assertFalse(DBConnection.isDBSupported("MySQL"));
	}
	
	@Test
	public void connectToDatabaseTest() {
		DBConnection con = new DBConnection();
		assertTrue(con.connectToDatabase(DBSetting.getDefault()));
	}
	
	//@Test
	public void executeQueryTest() {
		fail("Not yet implemented");
	}
	
	//@Test
	public void getTableColumnsTest() {
		fail("Not yet implemented");
	}

}
