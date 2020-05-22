package test;

import java.util.Arrays;

import main.ArrayVisualizer;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Timer;
import utils.Writes;

public class Tester {
    private static Delays Delays;
    private static Highlights Highlights;
    private static Reads Reads;
    private static Timer RealTimer;
    private static Writes Writes;
    
    // Attempts to use insertion sort on [begin, end). Will return false if more than
    // partial_insertion_sort_limit elements were moved, and abort sorting. Otherwise it will
    // successfully sort and return true.
    private static boolean pdqPartialInsertSort(int[] array, int begin, int end) {
        if (begin == end) return true;
        
        int limit = 0;
        for (int cur = begin + 1; cur != end; ++cur) {
            if (limit > 8) return false;

            int sift = cur;
            int siftMinusOne = cur - 1;

            // Compare first so we can avoid 2 moves for an element already positioned correctly.
            if (Reads.compare(array[sift], array[siftMinusOne]) < 0) {
                int tmp = array[sift];

                do { 
                    Writes.write(array, sift--, array[siftMinusOne], 0, false, false);
                } while (sift != begin && Reads.compare(tmp, array[--siftMinusOne]) < 0);

                Writes.write(array, sift, tmp, 0, false, false);
                limit += cur - sift;
            }
        }
        return true;
    }
    
	public static void main(String[] args) throws Exception {
	    int[] testArr = new int[32];
        for(int i = 0; i < testArr.length; i++) {
            testArr[i] = i;
        }
	    
        ArrayVisualizer av = new ArrayVisualizer();
        
	    Delays = new Delays(av);
	    Delays.setSleepRatio(Double.MAX_VALUE);
	    
	    Highlights = new Highlights(av, testArr.length);
	    Highlights.toggleFancyFinishes(false);
	    
	    RealTimer = new Timer();
	    RealTimer.toggleRealTimer(false);
	    
	    Reads = new Reads(av);
		Writes = new Writes(av);
		
		for(int i = 0; i < testArr.length; i++){
    		Writes.swap(testArr, i, (int)(Math.random()*testArr.length), 0, false, false);
    	}
		
		System.out.println(Arrays.toString(testArr));
		
		RealTimer.startLap();
		
		pdqPartialInsertSort(testArr, 0, testArr.length);
		
		RealTimer.stopLap();
		
		RealTimer.toggleRealTimer(true);
		
		System.out.println(Arrays.toString(testArr));
		System.out.println(RealTimer.getRealTime());
	}
}