package sorts;

import static array.visualizer.ArrayVisualizer.compare;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.Writes.swap;

//Written by Tom Duff, and found here: http://home.tiac.net/~cri_d/cri/2001/badsort.html
//from https://stackoverflow.com/questions/2609857/are-there-any-worse-sorting-algorithms-than-bogosort-a-k-a-monkey-sort/
public class SillySort {
	public static void sillySort(int a[], int i, int j){
		int m;
		if (i < j) {
		    /* find the middle of the array */
		    m = (i + j) / 2;
		    marked.set(3, m);
		    /* 
		     * use this function (recursively) to find put the minimum elements of 
		     * each half into the first elements of each half
		     */
		    sillySort(a, i, m);
		    sillySort(a, m+1, j);
		    /* 
		     * Choose the smallest element of the two halves, and put that element in
		     * the first position
		     */
		    if (compare(a[i], a[m+1]) >= 0) { 
		      swap(a, i, m+1, 1, true);
		    }
		    
		    marked.set(1, i);
		    marked.set(2, m+1);
		    sillySort(a, i+1, j);
		}
	}
}