package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

// Refactored from C++ code written by Manish Bhojasia, found here:
// https://www.sanfoundry.com/cpp-program-implement-weak-heap/
final public class WeakHeapSort extends Sort {
    public WeakHeapSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Weak Heap");
        this.setRunAllID("Weak Heap Sort");
        this.setReportSortID("Weak Heapsort");
        this.setCategory("Selection Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }
    
	private static int getBitwiseFlag(int[] bits, int x) {
		return ((bits[(x) >> 3] >> ((x) & 7)) & 1);
	}
	
	private void toggleBitwiseFlag(int[] bits, int x) { 
		int flag = bits[(x) >> 3]; 
		flag ^= 1 << ((x) & 7);
		
		Writes.write(bits, (x) >> 3, flag, 0, true, true);
	}

	/*
	 * Merge Weak Heap
	 */
	private void weakHeapMerge(int[] array, int[] bits, int i, int j) {
	    if (Reads.compare(array[i], array[j]) == -1)
	    {
	        this.toggleBitwiseFlag(bits, j);
	        Writes.swap(array, i, j, 1, true, false);
	    }
	}

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int n = length;
        int i, j, x, y, Gparent;
        
        int bitsLength = (n + 7) / 8;
        int[] bits = new int[bitsLength];
        
        for (i = 0; i < n / 8; ++i) {
            Writes.write(bits, i, 0, 0, false, true);
        }
        
        for (i = n - 1; i > 0; --i) {
            j = i;
            
            while ((j & 1) == WeakHeapSort.getBitwiseFlag(bits, j >> 1))
                j >>= 1;
                Gparent = j >> 1;
                
                this.weakHeapMerge(array, bits, Gparent, i);
        }
        
        for (i = n - 1; i >= 2; --i) {
            Writes.swap(array, 0, i, 1, true, false);
            
            x = 1;
            
            while ((y = 2 * x + WeakHeapSort.getBitwiseFlag(bits, x)) < i)
                x = y;
            
            while (x > 0) {
                this.weakHeapMerge(array, bits, 0, x);
                x >>= 1;
            }
        }
        Writes.swap(array, 0, 1, 1, true, false);   
    }
}