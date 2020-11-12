import java.util.InputMismatchException;
import java.util.Scanner;

public class Main implements Runnable {
	
	private static int timeframe, currIndex;
	private static String[] keywords;
	private static Scanner scanner;

	public static void main(String[] args) {
		startSearch();
	}
	
	public static int convertToMilli(int num) {
		return num*60000;
	}
	
	public static void startSearch() {
		keywords = new String[10];
		currIndex = 0;
		scanner = new Scanner(System.in);
		System.out.println("Enter keyword(s) to search for, separated by a comma and a space (maximum of 10 items)");
		String[] items = scanner.nextLine().split(", ");
		for (int i = 0; i < items.length; i++) {
			if (currIndex < keywords.length) {
				if (keywords[currIndex] == null) {
					keywords[currIndex] = items[i];
					currIndex++;		
				} else {
					currIndex++;
				}
			} else {
				System.out.println("Too many items!");
				return;
			}			
		}
		System.out.println("Enter the amount of time in minutes that you want to wait in between searches");
		try {
			timeframe = convertToMilli(scanner.nextInt());
		} catch (InputMismatchException e) {
			System.out.println("Invalid Time Input");
			return;
		}
		Thread thread = new Thread(new Main());
		scanner.nextLine();
		thread.start();	
	}
	
	public static String response() {
		System.out.println("Restart Search? Y/N");
		String result = scanner.nextLine();
		return result;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("Waiting for " + timeframe/60000 + " minute(s)");
			Thread.sleep(timeframe);
			System.out.println("Time is up, finding deals for: ");
			for (int i = 0; i < keywords.length; i++) {
				if (keywords[i] != null) {
					System.out.println(keywords[i] + " ");
				}
			}
	
			//Print results in JFrame
			System.out.println();
			System.out.println("DISPLAY SEARCH RESULTS HERE");
			System.out.println();
			
			String response = response();

			if (response.startsWith("Y")) {
				startSearch();
			} else if (response.startsWith("N")) {		
				scanner.close();
				System.exit(0);
			} else {
				System.out.println("Invalid Response");
				response();
			}

		} catch (InterruptedException e) {
			e.getStackTrace();
		}
	}
}
