package javagrailsort;

import static javagrailsort.GrailSort.grailSortWithBuffer;
import static javagrailsort.GrailSort.grailSortWithDynBuffer;
import static javagrailsort.GrailSort.grailSortWithoutBuffer;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

import javagrailsort.SortType;
import javagrailsort.SortComparator;

public class Tester {
	static DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
    static DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
    
	private static SortComparator test;
	
	private static int seed = 100000001;
	
	static double newArrayFinish;
	static double generateArrayFinish;
	static double noBufferFinish;
	static double staticBufferFinish;
	static double dynamicBufferFinish;
	
	public static int swaps = 0;
	public static int comps = 0;
	
	/******** Tests *********/
	
	private static int randomNumber(int k){
		seed = seed * 1234565 + 1;
		return seed & 0x7fffffff * k >> 31;
	}


	private static void generateArray(SortType[] arr, int[] keyCenter, int Len, int NKey){
		
		for(int i = 0; i < NKey; i++) {
			keyCenter[i] = 0;
		}
		
		for(int i = 0; i < Len; i++) {
			if(NKey != 0) {
				int key = randomNumber(NKey);
				arr[i].key = key;
				arr[i].value = keyCenter[key]++;
			} else {
				arr[i].key = randomNumber(1000000000);
				arr[i].value = 0;
			}
		}
	}

	private static boolean testArray(SortType[] arr, int Len){
		for(int i = 1; i < Len; i++) {
			int dk = test.compare(arr[i - 1], arr[i]);
			if(dk > 0) return false;
			if(dk == 0 && arr[i - 1].value > arr[i].value) return false;
		}
		return true;
	}
	
	public static void main(String[] args){
		test = new SortComparator();
		symbols.setGroupingSeparator(',');
        formatter.setDecimalFormatSymbols(symbols);
		
		int NMax = 4096;
		int NMaxKey = NMax;
		SortType[] arr = new SortType[NMax];
		long timeStart = System.nanoTime();
		for(int i = 0; i < arr.length; i++) {
			arr[i] = new SortType();
		}
		long timeFinish = System.nanoTime();
		System.out.println("Finished allocating memory for array");
		newArrayFinish = (timeFinish - timeStart) / 1e+6;
		int[] keys = new int[NMaxKey];
		
		timeStart = System.nanoTime();
		generateArray(arr, keys, NMax, 0);
		timeFinish = System.nanoTime();
		System.out.println("Finished generating array");
		generateArrayFinish = (timeFinish - timeStart) / 1e+6;
		SortType[] staticBufferArray = Arrays.copyOf(arr, arr.length);
		SortType[] dynamicBufferArray = Arrays.copyOf(arr, arr.length);
		
		timeStart = System.nanoTime();
		grailSortWithoutBuffer(arr);
		timeFinish = System.nanoTime();
		System.out.println("Finished Grail Sort w/o buffer");
		noBufferFinish = (timeFinish - timeStart) / 1e+6;
		
        timeStart = System.nanoTime();
		grailSortWithBuffer(staticBufferArray);
		timeFinish = System.nanoTime();
		System.out.println("Finished Grail Sort w/ static buffer");
		staticBufferFinish = (timeFinish - timeStart) / 1e+6;
		
        timeStart = System.nanoTime();
		grailSortWithDynBuffer(dynamicBufferArray);
		System.out.println("Finished Grail Sort w/ dynamic buffer");
		timeFinish = System.nanoTime();
		dynamicBufferFinish = (timeFinish - timeStart) / 1e+6;
	
		System.out.println(" ");
		System.out.println("New array of length " + formatter.format(NMax) + " in " + formatter.format(newArrayFinish) + " milliseconds.");
		System.out.println("Generated array of length " + formatter.format(NMax) + " in " + formatter.format(generateArrayFinish) + " milliseconds.");
		
		if(!testArray(arr, arr.length)) System.out.println("Grail Sort without buffers DID NOT sort successfully.");
		else System.out.println("Grail Sorting " + formatter.format(NMax) + " numbers without buffers sorted successfully in " + formatter.format(noBufferFinish) + " milliseconds.");
		
		if(!testArray(staticBufferArray, staticBufferArray.length)) System.out.println("Grail Sort with static buffer DID NOT sort successfully.");
		else System.out.println("Grail Sorting " + formatter.format(NMax) + " numbers with static buffer sorted successfully in " + formatter.format(staticBufferFinish) + " milliseconds.");
		
		if(!testArray(dynamicBufferArray, dynamicBufferArray.length)) System.out.println("Grail Sort with dynamic buffer DID NOT sort successfully.");
		else System.out.println("Grail Sorting " + formatter.format(NMax) + " numbers with dynamic buffer sorted successfully in " + formatter.format(dynamicBufferFinish) + " milliseconds.");
	}
}