package main;

/*Github Branch Test*/
import java.util.Scanner;

public class Source {
	public static void main(String[] args) {
		int trigger = 0;

		while (trigger == 0) {
			System.out.print("Input Text : ");
			Scanner sc = new Scanner(System.in);

			String str = sc.nextLine();
			if (str.equalsIgnoreCase("Alert")) {
				System.out.println("Cancelled");
				trigger = 1;
				sc.close();
			} else
				System.out.println("Repeat Text : " + str);
		}
		System.out.println("END");
	}
}