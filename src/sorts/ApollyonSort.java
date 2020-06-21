///ApollyonSort is a modification of BitonicSort that replaces one operation with CircleSort, and then once the array is almost sorted to an extent, it uses an InsertionSort with a LinearSearch
///It is usually just as fast as BitonicSort, if not 1 ms slower. It is made for real world data such as almost sorted arrays and backwards arrays. On these types of arrays ApollyonSort outperforms BitonicSort
///Features: In-Place, Parallel, Recursive (can be modified to iterative), Unstable, Uses comparisons.
package sorts;

import templates.Sort;
import templates.CircleSorting;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 * This version of Bitonic Sort was taken from here, written by H.W. Lang:
 * http://www.inf.fh-flensburg.de/lang/algorithmen/sortieren/bitonic/oddn.htm
 */

final public class ApollyonSort extends CircleSorting {
    private boolean direction = true;
    
    public ApollyonSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Apollyon");
        this.setRunAllID("Apollyon's Bitonic Sort");
        this.setReportSortID("Apollyon's Bitonic Sort");
        this.setCategory("Hybrid Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }
    
    private static int greatestPowerOfTwoLessThan(int n){
        int k = 1;
        while (k < n) {
            k = k << 1;
        }
        return k >> 1;      
    }
    
    private void compare(int[] A, int i, int j, boolean dir)
	{
    	Highlights.markArray(1, i);
    	Highlights.markArray(2, j);
    	
    	Delays.sleep(0.5);
    	
    	int cmp = Reads.compare(A[i], A[j]);
    	
	    if (dir == (cmp == 1)) Writes.swap(A, i, j, 1, true, false);
	}

	private void bitonicMerge(int[] A, int lo, int n, boolean dir)
	{
	    if (n > 1)
	    {
	        int m = ApollyonSort.greatestPowerOfTwoLessThan(n);

	        for (int i = lo; i < lo + n - m; i++) {
	            this.compare(A, i, i+m, dir);
	        }
	        this.bitonicMerge(A, lo, m, dir);
		this.bitonicMerge(A, lo + m, n - m, dir);
	    }
	}

	private void bitonicSort(int[] A, int lo, int n, boolean dir)
	{
	    if (n > 1)
	    {
	        int m = n / 2;
	        this.bitonicSort(A, lo, m, !dir);
	        this.bitonicMerge(A, lo, n, dir);
	    }
	}

	public void changeDirection(String choice) throws Exception {
	    if(choice.equals("forward")) this.direction = true;
	    else if(choice.equals("backward")) this.direction = false;
	    else throw new Exception("Invalid direction for Bitonic Sort!");
	}
	
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int iterations = 0;
        int threshold = (int) (Math.log(currentLength) / Math.log(2)) / 2;
        
        this.bitonicSort(array, 0, currentLength, this.direction);
        do {
            iterations++;
            
            if(iterations >= threshold) {
                InsertionSort linearInserter = new InsertionSort(this.Delays, this.Highlights, this.Reads, this.Writes);
                linearInserter.customInsertSort(array, 0, currentLength, 0.1, false);
                break;
            }
        } while (this.circleSortRoutine(array, 0, currentLength - 1, 0) != 0);
    }
}
