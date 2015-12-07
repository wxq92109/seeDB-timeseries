package timeseries;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.Headers;

public class Server {

  @SuppressWarnings("restriction")
public static void main(String[] args) throws Exception {
    @SuppressWarnings("restriction")
	HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
    server.createContext("/getResult", new ResultHandler());
    server.createContext("/get", new GetHandler());
    server.setExecutor(null); // creates a default executor
    server.start();
  }

  static class ResultHandler implements HttpHandler {
    public void handle(HttpExchange t) throws IOException {
    	// step 1: connect to db
    	
		DBSetting s = DBSetting.getDefault();
		
		SeeDB seedb = new SeeDB();
		seedb.connectToDatabase(s.database, s.databaseType, s.username, s.password);
		System.out.println("connected to db");
		seedb.binTimeData("hashtags", "hashtags_by_hour", Timestamp.valueOf("2015-02-24 00:00:00.0"), Timestamp.valueOf("2015-02-24 05:00:00.0"));
		String target = "job";
		String[] candidates = {"jobs", "kca", "tweetmyjobs", "vote1duk"};
		seedb.computeCorrelation(target, candidates);
		LinkedHashMap<String, HashMap<Timestamp, Double>> results = (LinkedHashMap<String, HashMap<Timestamp, Double>>) seedb.getHighlyCorrelated(5);

		results.forEach((k, v) -> System.out.println(k + "=" + v));
		String response = "Response: ";
		for (Entry<String, HashMap<Timestamp, Double>> result : results.entrySet()) {
			response += result.getKey() + "=" + result.getValue();
		}
//      String response = "Use /get to download a PDF";
      t.sendResponseHeaders(200, response.length());
      OutputStream os = t.getResponseBody();
      os.write(response.getBytes());
      os.close();
    }
  }

  static class GetHandler implements HttpHandler {
    public void handle(HttpExchange t) throws IOException {

      // add the required response header for a PDF file
      Headers h = t.getResponseHeaders();
      h.add("Content-Type", "application/pdf");

      // a PDF (you provide your own!)
      File file = new File ("c:/temp/doc.pdf");
      byte [] bytearray  = new byte [(int)file.length()];
      FileInputStream fis = new FileInputStream(file);
      BufferedInputStream bis = new BufferedInputStream(fis);
      bis.read(bytearray, 0, bytearray.length);

      // ok, we are ready to send the response.
      t.sendResponseHeaders(200, file.length());
      OutputStream os = t.getResponseBody();
      os.write(bytearray,0,bytearray.length);
      os.close();
    }
  }
}