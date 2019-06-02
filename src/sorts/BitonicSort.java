/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sorts;

import static array.visualizer.ArrayVisualizer.calcReal;
import static array.visualizer.ArrayVisualizer.comps;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.ArrayVisualizer.realTimer;
import static array.visualizer.ArrayVisualizer.sleep;
import static array.visualizer.Writes.swap;

/*
 * This version of Bitonic Sort was taken from here, written by H.W. Lang:
 * http://www.inf.fh-flensburg.de/lang/algorithmen/sortieren/bitonic/oddn.htm
 */
public class BitonicSort {
    private static void compare(int[] A, int i, int j, boolean dir)
	{
    	marked.set(1, i);
    	marked.set(2, j);
    	sleep(1);
    	
    	long time = System.nanoTime();
    	boolean cmp = A[i] > A[j];
    	if(calcReal) realTimer += (System.nanoTime() - time) / 1e+6;
    	
	    if (dir == cmp) swap(A, i, j, 2, true);
	    comps++;
	}

	private static int greatestPowerOfTwoLessThan(int n){
	    int k = 1;
	    while (k < n) {
	    	k = k << 1;
	    }
	    return k >> 1;    	
	}


	private static void bitonicMerge(int[] A, int lo, int n, boolean dir)
	{
	    if (n > 1)
	    {
	        int m = greatestPowerOfTwoLessThan(n);

	        for (int i = lo; i < lo + n - m; i++) {
	            compare(A, i, i+m, dir);
	        }

	        bitonicMerge(A, lo, m, dir);
	        bitonicMerge(A, lo + m, n - m, dir);
	    }
	}

	public static void bitonicSort(int[] A, int lo, int n, boolean dir)
	{
	    if (n > 1)
	    {
	        int m = n / 2;
	        bitonicSort(A, lo, m, !dir);
	        bitonicSort(A, lo + m, n - m, dir);
	        bitonicMerge(A, lo, n, dir);
	    }
	}
}