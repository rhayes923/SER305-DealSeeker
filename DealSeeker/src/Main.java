import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

public class Main implements Runnable {
	
	private static int timeframe;
	private static String[] keywords;
	private static Scanner scanner;
	private static Thread thread;

	public static void main(String[] args) {
		startSearch();
	}
	
	public static int convertToMilli(int num) {
		return num*60000;
	}
	
	public static void startSearch() {
		scanner = new Scanner(System.in);
		System.out.println("Enter keyword(s) to search for, separated by a comma and a space");
		keywords = scanner.nextLine().split(", ");			
		System.out.println("Enter the amount of time in minutes that you want to wait in between searches");
		try {
			timeframe = convertToMilli(scanner.nextInt());
		} catch (InputMismatchException e) {
			System.out.println("Invalid Time Input");
			return;
		}
		thread = new Thread(new Main());
		scanner.nextLine();
		thread.start();	
	}
	
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
	
	@Override
	public void run() {
		try {
			System.out.println("Waiting for " + timeframe/60000 + " minute(s)...");
			Thread.sleep(timeframe);
			System.out.println("Time is up, finding deals for: ");
			for (int i = 0; i < keywords.length; i++) {
				System.out.println(keywords[i]);
			}
			
			String resultText = "<html>Found deals for ";
			String resultDeals = "<html>";
			for (int i = 0; i < keywords.length; i++) {
				if (keywords[i] != null) {
					resultText += (keywords[i] + "");
					if (i < keywords.length - 1) {
						resultText += ", ";
					} else {
						resultText += " ";
					}
					resultDeals += "<strong>Deals for " + keywords[i] + ":</strong><br>";
					LinkedList<String> currDeals = WebScraper.findThisDeal(keywords[i]);
					for (int j = 0; j < currDeals.size(); j++) {
						resultDeals += currDeals.get(j) + "<br>";
					}
				}
			}
			resultText += "on www.dealsea.com<br>";
			resultDeals += "</html>";
			
			new Display(900, 600, resultText + resultDeals);
			
			response();

		} catch (InterruptedException e) {
			e.getStackTrace();
		}
	}
}
