package sorts;

import static array.visualizer.ArrayVisualizer.compare;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.Writes.swap;

//Code refactored from Python: http://wiki.c2.com/?SlowSort
public class SlowSort {
	
	public static void slowSort(int[] A, int i, int j)
	{	
		
		if (i >= j) {
			return;
		}
	
	    int m = (i + j) / 2;
	    marked.set(3, m);
	
	    slowSort(A, i, m);
	    slowSort(A, m+1, j);
	
	    if (compare(A[m], A[j]) == 1) {
	        swap(A, m, j, 1, true);
	    }
	    
	    marked.set(1, j);
	    marked.set(2, m);
	    slowSort(A, i, j-1);
	    
	}
}