package main;

/**
 * Default type of Perceptron 3-bit AND calculation
 * @author ehdgur316
 *
 */
public class Perceptron {
	final static double LEARNING_RATE = 0.05;
	public static void main(String[] args) {
		/*
		 * problemArray -> {{bias, input1, input2}, ... }
		 * answerArray -> {{input1, input2, output}, ... }
		 * weightArray -> {bias weight, input1 weight, input2 weight}
		 * resultArray -> {output1, output2, output3, output4}
		 */
		int[][] problemArray = {{0,0,0,0}, {0,0,0,1}, {0,0,1,0}, {0,0,1,1}
								, {0,1,0,0}, {0,1,0,1}, {0,1,1,0}, {0,1,1,1}};
		int[][] answerArray = {{0,0,0,0}, {0,0,1,0}, {0,1,0,0}, {0,1,1,0}
								,{1,0,0,0}, {1,0,1,0},{1,1,0,0},{1,1,1,1}};
		double[] weightArray = new double[problemArray[0].length];
		int[] resultArray = new int[problemArray.length];
		
		System.out.println("Problem Array");
		for (int i = 0 ; i < problemArray.length ; i++) {
			for (int j = 1 ; j < problemArray[i].length ; j++) {
				System.out.print(problemArray[i][j] + " ");
			}
			System.out.println();
		}
		
		initWeight(weightArray);
		initBias(problemArray);
		
		boolean isFinished;
		do {
			isFinished = true;
			for (int i = 0; i < problemArray.length; i++) {
				resultArray[i] = calculateWeight(problemArray[i], weightArray, answerArray);
				if (resultArray[i] != answerArray[i][answerArray[i].length - 1]) {
					editWeight(weightArray, problemArray[i], answerArray[i][answerArray[i].length - 1], resultArray[i]);
					isFinished = false;
				}
			}
		} while(!isFinished);
		
		System.out.println("Result Array");
		for (int i = 0 ; i < resultArray.length ; i++) {
			System.out.println(resultArray[i]);
		}
	}
	
	public static void initWeight(double[] weightArray) {
		for (int i = 0 ; i < weightArray.length ; i++) {
			weightArray[i] = Math.random() - 0.5;
		}
	}
	
	public static void initBias(int [][] problemArray) {
		for (int i = 0 ; i < problemArray.length ; i++) {
			problemArray[i][0] = -1;
		}
	}
	
	public static int calculateWeight(int[] problemArray, double[] weightArray, int[][] answerArray) {
		int calculateResult = 0;
		for (int i = 0 ; i < problemArray.length ; i++) {
			calculateResult += problemArray[i] * weightArray[i];
		}
		
		if ( calculateResult > 0){
			return 1;
		}
		else {
			return 0;
		}
	}
	
	public static void editWeight(double[] weightArray, int[] problemArray, int expectedAnswer, int resultWeight ) {
		for (int i = 0 ; i < weightArray.length ; i++) {
			weightArray[i] = weightArray[i] + LEARNING_RATE * problemArray[i] * (expectedAnswer - resultWeight);
		}
	}
}