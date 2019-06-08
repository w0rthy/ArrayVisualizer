package sorts;

import static array.visualizer.ArrayVisualizer.clearmarked;
import static array.visualizer.ArrayVisualizer.compare;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.ArrayVisualizer.sleep;
import static array.visualizer.Writes.swap;
import static sorts.HeapSorts.partialHeap;
import static sorts.InsertionSorts.partialInsert;
import static sorts.ShellSort.shellSort;
	
//original Copyright Ralph Unden, 
//http://ralphunden.net/content/tutorials/a-guide-to-introsort/?q=a-guide-to-introsort
//Modifications: Bernhard Pfahringer 
//changes include: local insertion sort, no global array
	
	public class IntroSort 
	{
		static int middle;
		
	  /*
	   * Class Variables
	   */
	  private static int size_threshold = 16;
	  /*
	   * Public interface
	   */
	  public static void introSort(int[] a, int length, int part, int choice, int shellpart) throws Exception
	    {
		  size_threshold = part;
		  if(choice == 1) {
			  introsort_loop(a, 0, length, 2*floor_lg(a.length));
			  clearmarked();
			  partialInsert(a, 0, length, .5);
		  }
		  else if(choice == 2) {
			  shellIntrosort(a, 0, length, 2*floor_lg(a.length));
			  clearmarked();
			  shellSort(a, length, shellpart);
		  }
		  else throw new Exception("Invalid insertion sort!");
	    }

	  /*
	   * Quicksort algorithm modified for Introsort
	   */
	  private static void introsort_loop (int[] a, int lo, int hi, int depth_limit) throws Exception
	    {
	      while (hi-lo > size_threshold)
		  {
		    if (depth_limit == 0)
			{
			  partialHeap(a, lo, hi, 2);
			  return;
			}
		    depth_limit--;
		    int p=partition(a, lo, hi, medianof3(a, lo, lo+((hi-lo)/2)+1, hi-1));
		    introsort_loop(a, p, hi, depth_limit);
		    hi=p;
		  }
	      return;
	    }
	  
	  private static void shellIntrosort (int[] a, int lo, int hi, int depth_limit) throws Exception
	    {
	      if (hi-lo > size_threshold)
		  {
		    if (depth_limit == 0)
			{
			  partialHeap(a, lo, hi, 2);
			  return;
			}
		    depth_limit--;
		    int p=partition(a, lo, hi, medianof3(a, lo, lo+((hi-lo)/2)+1, hi-1));
		    shellIntrosort(a, lo, p, depth_limit);
		    shellIntrosort(a, p, hi, depth_limit);
		    return;
		  }
	    }

	  private static int partition(int[] a, int lo, int hi, int x)
	    {
	      int i=lo, j=hi;
	      while (true)
		  {
		    while (compare(a[i], x) == -1) {
		    	marked.set(1, i);
		    	sleep(0.5);
		    	i++;
		    }
		    j=j-1;
		    while (compare(x, a[j]) == -1) {
		    	marked.set(2, j);
		    	sleep(0.5);
		    	j--;
		    }
		    if(!(i < j)) {
		    	marked.set(1, i);
		    	sleep(0.5);
		    	return i;
		    }
		    if(i == middle || j == middle) {
        		marked.set(3, x);
        	}
		    swap(a,i,j,1,true);
		    i++;
		  }
	    }

	  private static int medianof3(int[] arr, int left, int mid, int right)
	  {
		  if(compare(arr[right], arr[left]) == -1) {
			  swap(arr, left, right, 1, true); 
		  }
		  if(compare(arr[mid], arr[left]) == -1) {
			  swap(arr, mid, left, 1, true);
		  }
		  if(compare(arr[right], arr[mid]) == -1) {
			  swap(arr, right, mid, 1, true);
		  }
		  middle = mid;
		  marked.set(3, mid);
		  return arr[mid];
	  }

	  private static int floor_lg(int a)
	    {
	      return (int)(Math.floor(Math.log(a)/Math.log(2)));
	    }
}