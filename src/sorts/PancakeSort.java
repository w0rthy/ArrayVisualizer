package sorts;

import static array.visualizer.ArrayVisualizer.compare;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.ArrayVisualizer.sleep;
import static array.visualizer.Writes.swap;

/**
 * <b>IDeserve <br>
 * <a href="<a class="vglnk" href="https://www.youtube.com/c/IDeserve" rel="nofollow"><span>https</span><span>://</span><span>www</span><span>.</span><span>youtube</span><span>.</span><span>com</span><span>/</span><span>c</span><span>/</span><span>IDeserve</span></a>"><a class="vglnk" href="https://www.youtube.com/c/IDeserve" rel="nofollow"><span>https</span><span>://</span><span>www</span><span>.</span><span>youtube</span><span>.</span><span>com</span><span>/</span><span>c</span><span>/</span><span>IDeserve</span></a></a>
 * Given an array, sort the array using Pancake sort.
 * 
 * @author Saurabh
 * https://www.ideserve.co.in/learn/pancake-sorting
 */

public class PancakeSort {
	public static void pancakeSort(int[] array) {
	      for (int i = array.length - 1; i > 0; i--) {
	          int index = findMax(array, i);
	          
	          flip(array, index);
	          flip(array, i);
	      }
	  }
	  
	  private static void flip(int[] arr, int k) {
	      for (int i = 0; i < (k+1) / 2; i++) {
	          swap(arr, i, k - i, 1, true);
	          marked.set(1, -5);
	          marked.set(2, -5);
	      }
	  }
	  
	  private static int findMax(int[] arr, int end) {
	      int index = 0, max = Integer.MIN_VALUE;
	      for (int i = 0; i <= end; i++) {
	    	  marked.set(1, i);
	          if (compare(arr[i], max) == 1) {
	              max = arr[i];
	              index = i;
	          }
	          sleep(1);
	          marked.set(1, -5);
	      }
	      return index;
	  }
}