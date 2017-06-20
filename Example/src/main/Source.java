package main;

import java.util.Scanner;

public class Source {
	public static void main(String[] args) {
		
		System.out.print("Input Text : ");
		Scanner sc = new Scanner(System.in);

		String str = sc.nextLine();
		if (str.equalsIgnoreCase("Alert")) System.out.println("Cancelled");
		else System.out.println("Repeat Text : " + str);
		System.out.println("END");
		
		sc.close();
	}
}