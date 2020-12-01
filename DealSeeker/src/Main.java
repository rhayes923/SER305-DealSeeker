import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

public class Main implements Runnable {
	
	private static int timeframe;
	private static String keywordsInput;
	private static String[] keywords;
	private static Scanner scanner;
	private static Thread thread;

	public static void main(String[] args) {
		System.out.println("[INFO]: Please limit the number of requests to the website.");
		System.out.println("Too many requests in a short timeframe may lead to network restrictions to the website.");
		System.out.println("---");
		startSearch();
	}
	
	//Retrieve user input for keywords and the time to wait
	public static void startSearch() {
		scanner = new Scanner(System.in);
		System.out.println("Enter keyword(s) to search for, separated by a comma and a space");
		keywordsInput = scanner.nextLine();
		keywords = keywordsInput.split(", ");			
		System.out.println("Enter the amount of time in minutes that you want to wait in between searches");
		try {
			timeframe = scanner.nextInt() * 60000;
		} catch (InputMismatchException e) {
			System.out.println("Invalid Time Input");
			return;
		}
		thread = new Thread(new Main());
		scanner.nextLine();
		thread.start();	
	}
	
	//Determine if another search should be made or close the program
	public static void response() {
		boolean validResponse = false;
		while (!validResponse) {
			System.out.println("Restart Search? Y/N");
			String result = scanner.nextLine();
			if (result.startsWith("Y")) {
				validResponse = true;
			} else if (result.startsWith("N")) {
				System.out.println("Closing Program...");
				scanner.close();
				System.exit(0);
			} else {
				System.out.println("Invalid Response");
				response();
			}
		}
		startSearch();
	}
	
	//Puts the thread to sleep and then retrieves the deals from WebScraper and displays it in a Display window
	@Override
	public void run() {
		try {
			System.out.println("Waiting for " + timeframe/60000 + " minute(s)...");
			Thread.sleep(timeframe);
			System.out.println("Time is up, finding deals for: " + keywordsInput);
			
			String resultText = "<html>Found deals for " + keywordsInput + " ";
			String resultDeals = "<html>";
			for (int i = 0; i < keywords.length; i++) {
				resultDeals += "<strong>Deals for " + keywords[i] + ":</strong><br>";
				LinkedList<String> currDeals = WebScraper.findThisDeal(keywords[i]);
				for (int j = 0; j < currDeals.size(); j++) {
					resultDeals += currDeals.get(j) + "<br>";
				}
			}
			resultText += "on www.dealsea.com<br>";
			resultDeals += "</html>";
			
			System.out.println("Creating Pop-Up Window...");
			new Display(900, 600, resultText + resultDeals);
			
			response();

		} catch (InterruptedException e) {
			e.getStackTrace();
		}
	}
}
