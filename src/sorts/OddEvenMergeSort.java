package sorts;

import static array.visualizer.ArrayVisualizer.compare;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.Writes.swap;

/*
 * This version of Odd-Even Merge Sort was taken from here, written by H.W. Lang:
 * http://www.inf.fh-flensburg.de/lang/algorithmen/sortieren/networks/oemen.htm
 */
public class OddEvenMergeSort {
	public static void oddEvenMergeSort(int[] array, int lo, int n)
	{
		if (n>1)
		{
			int m=n/2;
			oddEvenMergeSort(array, lo, m);
			oddEvenMergeSort(array, lo+m, m);
			oddEvenMerge(array, lo, n, 1);
		}
	}

	/** lo is the starting position and
	 *  n is the length of the piece to be merged,
	 *  r is the distance of the elements to be compared
	 */
	private static void oddEvenMerge(int[] array, int lo, int n, int r)
	{
		int m=r*2;
		if (m<n)
		{
			oddEvenMerge(array, lo, n, m);      // even subsequence
			oddEvenMerge(array, lo+r, n, m);    // odd subsequence
			for (int i=lo+r; i+r<lo+n; i+=m) {
				marked.set(1, i);
				marked.set(2, i + r);
				cmp(array, i, i+r);
			}
		}
		else {
			marked.set(1, lo + r);
			marked.set(2, lo);
			cmp(array, lo, lo+r);
		}
	}

	private static void cmp(int[] array, int i, int j)
	{
		if (compare(array[i], array[j]) > 0)
			swap(array, i, j, 1, true);
	}
}