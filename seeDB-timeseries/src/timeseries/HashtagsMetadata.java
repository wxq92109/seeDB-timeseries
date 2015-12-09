package timeseries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HashtagsMetadata {
	private DBConnection conn;

	int threshold;

	private ArrayList<String> hashtags;
	private ArrayList<String> popular;

	public HashtagsMetadata(DBConnection conn, int popThreshold) {
		this.conn = conn;
		this.threshold = popThreshold;
		this.hashtags = new ArrayList<String>();
		this.popular = new ArrayList<String>();
		runQuery();
	}

	// public void runQuery() {
	// String query = "SELECT hashtag, COUNT(*) FROM hashtags GROUP BY hashtag
	// ORDER BY COUNT(*) DESC;";
	// ResultSet rs = conn.executeQuery(query);
	// try {
	// while (rs.next()) {
	// String hashtag = rs.getString(1);
	// hashtags.add(hashtag);
	// int count = rs.getInt(2);
	// if (count > threshold) {
	// popular.add(hashtag);
	// }
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	//
	public void runQuery() {

		String query = "SELECT hashtag, COUNT(*) FROM hashtags GROUP BY hashtag ORDER BY COUNT(*) DESC;";

		ResultSet rs = conn.executeQuery(query);

		try {

			while (rs.next()) {

				String hashtag = rs.getString(1);

				if (!hashtag.equals("kcaᅠ") && !hashtag.equals("kcα") && !hashtag.equals("duquefamily")
						&& !hashtag.equals("gthb20bi̇n")

						&& !hashtag.equals("나영아생일ㅊㅋ")) {

					hashtags.add(hashtag);

					int count = rs.getInt(2);

					if (count > threshold) {

						popular.add(hashtag);

					}

				}

			}

		} catch (SQLException e) {

			e.printStackTrace();

		}

	}

	public String[] getPopular() {
		return popular.toArray(new String[popular.size()]);
	}

	public String[] getAll() {
		return hashtags.toArray(new String[hashtags.size()]);
	}

}