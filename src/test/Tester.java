package test;

import static array.visualizer.ArrayVisualizer.*;
import static sorts.BubbleSort.*;
import static array.visualizer.Writes.swap;

public class Tester {
	public static void main(String[] args) throws Exception {
		int[] testArr = new int[2048];
		initArr(testArr);
		
		for(int i = 0; i < testArr.length; i++){
    		swap(testArr, i, (int)(Math.random()*testArr.length), 0, true);
    	}
		
		long timeStart = System.nanoTime();
		bubbleSort(testArr);
		long timeFinish = System.nanoTime();
		double finish = (timeFinish - timeStart) / 1e+6;
		System.out.println(finish + "ms");
	}
}