package sorts;

import static array.visualizer.ArrayVisualizer.compare;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.ArrayVisualizer.sleep;
import static array.visualizer.Writes.swap;

/*
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE").
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS
 * LICENSE OR COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE.
 * TO THE EXTENT THIS LICENSE MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED HERE IN
 * CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 */

//Code refactored from: https://en.wikipedia.org/wiki/Stooge_sort
public class StoogeSort {

	public static void stoogeSort(int[] A, int i, int j)
	{
	    if (compare(A[i], A[j]) == 1)
	    {
	        swap(A, i, j, 1, true);
	    }
	    sleep(0.025);
	
	    if (j - i + 1 >= 3)
	    {
	        int t = (j - i + 1) / 3;
	
	        marked.set(1, i);
	        marked.set(2, j);
	
	        stoogeSort(A, i, j-t);
	        stoogeSort(A, i+t, j);
	        stoogeSort(A, i, j-t);
	
	        marked.set(1, 0);
	        marked.set(2, 0);
	    }
	}
}