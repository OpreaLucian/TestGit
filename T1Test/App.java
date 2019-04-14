package T1.T1Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Hello world!
 *
 */
public class App 
{
	private static final String PART_NEXT_PAGE_LINK = "https://www.google.ro/search?q=";
	protected static final String USER_AGENT = 
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";

	private static List<String> googlePagesLinks = new LinkedList<String>();
	private static List<String> googleResultsLinks;
	
    public static void main( String[] args )
    {
    	try {
    		String[] searchWordsList = {"garsoniera sibiu", "apartamente de vanzare sibiu"};
    		String searchSite = "eurosibimobiliare";
    		JSONObject json = new JSONObject();
    		JSONArray arrayElementOneArray = new JSONArray();
    		
    		for(String searchWord:searchWordsList)
    		{
	    		String url = PART_NEXT_PAGE_LINK + searchWord;
	    		Integer rang = 0;
	    		
	    		List<String> links;
	    	    Document htmlDocument;
	    		int statusCode;
	    		int index;
	    		
	    		googlePagesLinks = new LinkedList<String>();
	    		googleResultsLinks = new LinkedList<String>();
	    		
	    		Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
	    		htmlDocument = connection.get();
	    		statusCode = connection.response().statusCode();
	    		CheckPage(htmlDocument);
	    		
	    		
	    		GetNextPageLink(htmlDocument);
	    		googlePagesLinks = googlePagesLinks.subList(0, googlePagesLinks.size()-1);
	    		for(String urlNextPage:googlePagesLinks)
	    		{
	    		    connection = Jsoup.connect(urlNextPage).userAgent(USER_AGENT);
	    			htmlDocument = connection.get();
	    			statusCode = connection.response().statusCode();
	    			
	    			CheckPage(htmlDocument);
	    		}
	    		
	    		index = 1;
	    		for(String resultLink:googleResultsLinks)
	    		{
	    			if(resultLink.contains(searchSite))
	    			{
	    				rang = index;
	    				System.out.println(index);
	    				break;
	    			}
	    			index++;
	    		}
	    		
	//    		BufferedWriter writer = new BufferedWriter(new FileWriter("my.txt", true));
	//    		
	//    		Elements elementLinks = htmlDocument.getElementsByClass("rc");		
	//    		Element s;
	//    		for(Element link : elementLinks) {
	//    			s = (Jsoup.parse(link.outerHtml().toString())).select("a").first();
	//    			writer.append(s.attr("href")+"\n");
	//    		}
	//    	     
	//    	    writer.close();
	        	
	    		JSONObject arrayElementOneArrayElementOne = new JSONObject();
	    		arrayElementOneArrayElementOne.put("cuvantCheie", searchWord);
	    		arrayElementOneArrayElementOne.put("rang", index);
	    		arrayElementOneArray.add(arrayElementOneArrayElementOne);
	    		
	    		
	    		
	    		
	    		json.put(searchSite,arrayElementOneArray);
    		}
            System.out.print(json);
        	
    		}
    		catch(Exception e) {
    			System.out.print("dasd");
    		}
    
    }
    
	private static void CheckPage(Document doc) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("my.txt", true));
		
		Elements elementLinks = doc.getElementsByClass("rc");		
		Element s;
		for(Element link : elementLinks) {
			s = (Jsoup.parse(link.outerHtml().toString())).select("a").first();
			googleResultsLinks.add(s.attr("href"));
			writer.append(s.attr("href")+"\n");
		}
		writer.append("\n"); 
	    writer.close();
		
	}

	private static void GetNextPageLink(Document doc)
	{
		Elements elementLinks = doc.select("a[href]");
		String stringLink;
		 
		for(Element link : elementLinks) {
			stringLink = link.absUrl("href");
			
			if(stringLink.contains(PART_NEXT_PAGE_LINK) && stringLink.contains("&start=")) {
				googlePagesLinks.add(stringLink);
				//System.out.print(stringLink+"\n");
			}
		}
	}
}
