import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class readHashTags
{ 
	public static void main(String [] args) throws IOException
	{
        String fileName = "tweets/geotweets_2015-02-28.tsv";
        FileReader fileReader = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fileReader);
        
        String saveFileName = "tweets/tweets_cut/tweets_2015-02-28.tsv";
        File savefile = new File(saveFileName);
        BufferedWriter bw = new BufferedWriter(new FileWriter(savefile));
        
        String line = null;

        int recordID = 1918461;
        while ((line = br.readLine()) != null) {
        	
        	String[] values = line.split("\\t");
        	ArrayList<String> asArray = new ArrayList<String> (Arrays.asList(values));

        	bw.write("" + recordID + "\t");
        	bw.write("" + asArray.get(1) + "\t");
        	bw.write("" + asArray.get(5) + "\t");
        	bw.write("" + asArray.get(15) + "\t");

        	List<String> fromHashtag = asArray.subList(23, asArray.size());
        	String endHashtag = "(.*)\\}";
        	ArrayList<String> hashtags = new ArrayList<String> ();
        	for (String hashtag: fromHashtag) {
        		if (hashtag.matches(endHashtag)) {
        			hashtags.add(hashtag.toLowerCase());
        			break;
        		}
        		hashtags.add(hashtag.toLowerCase());
        	}
        	for (String s: hashtags) {
        		s = s.replaceAll("\\{|\\}|#", "");
        		bw.write(s + "\t");
        	}
        	bw.newLine();            
        	recordID ++;
        	  
        }

        bw.close();
        br.close();      
        System.out.println("done");
	}
}