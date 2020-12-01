import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.LinkedList;

public class WebScraper 
{
	//Make a static call to this method
	public static LinkedList<String> findThisDeal(String s)
	{
		String urlPrefix = "https://dealsea.com/search?q=";
		String urlSufix = "&search_mode=Deals";
		String mid = "";
		
		//Remove all problem characters/ replace spaces with +
		for(int i = 0; i < s.length(); i++)
		{
			if(Character.isWhitespace(s.charAt(i)))
				mid += "+";
			else if(Character.isLetter(s.charAt(i)) || Character.isDigit(s.charAt(i)))
				mid += s.charAt(i);
		}
		
		return scrapeWeb(urlPrefix + s + urlSufix, s);
		
	}
	
	//Pull deals from webpage
	private static LinkedList<String> scrapeWeb(String url, String key)
	{
		//String[] deals = new String[10];
		LinkedList<String> deals = new LinkedList<String>();
		
		try
		{
			final Document doc = Jsoup.connect(url).get();//Pull html from page
			
			int i = 0;
			
			//Find all deals
			for(Element e : doc.select(".dealcontent"))
			{
				if(i >= 10) break;//Return maximum of three deals
				
				Element span = null;
				
				if(e.select("span") != null)//If span element exits (this is where DealSea always lists if the deal is expired)
					span = e.selectFirst("span");
				
				if(span == null || !span.hasClass("colr_red"))//If not expired, add to list
				{
					Element strong = e.selectFirst("strong");
					if (strong.getElementsByTag("a").text().toLowerCase().contains(key.toLowerCase())) 
					{
						deals.add(i, strong.getElementsByTag("a").text());
						i++;
					}
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return deals;
	}

}

//select()
//getElementsByTag()