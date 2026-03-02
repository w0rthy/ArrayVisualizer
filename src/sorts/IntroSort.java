package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

// original Copyright Ralph Unden, 
// http://ralphunden.net/content/tutorials/a-guide-to-introsort/?q=a-guide-to-introsort
// Modifications: Bernhard Pfahringer 
// changes include: local insertion sort, no global array

final public class IntroSort extends Sort {
    private MaxHeapSort heapSorter;

    private int middle;
    private int sizeThreshold = 16;
    
    public IntroSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Intro");
        this.setRunAllID("Introspective Sort (std::sort)");
        this.setReportSortID("Introsort");
        this.setCategory("Hybrid Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

    private static int floorLogBaseTwo(int a) {
        return (int) (Math.floor(Math.log(a) / Math.log(2)));
    }
    
    private int medianof3(int[] arr, int left, int mid, int right) {
        if(Reads.compare(arr[right], arr[left]) == -1) {
            Writes.swap(arr, left, right, 1, true, false); 
        }
        if(Reads.compare(arr[mid], arr[left]) == -1) {
            Writes.swap(arr, mid, left, 1, true, false);
        }
        if(Reads.compare(arr[right], arr[mid]) == -1) {
            Writes.swap(arr, right, mid, 1, true, false);
        }
        middle = mid;
        Highlights.markArray(3, mid);
        return arr[mid];
    }
    
    private int partition(int[] a, int lo, int hi, int x) {
        int i = lo, j = hi;
        while (true) {
            while (Reads.compare(a[i], x) == -1) {
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
                i++;
            }

            j--;

            while (Reads.compare(x, a[j]) == -1) {
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
                j--;
            }

            if(!(i < j)) {
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
                return i;
            }

            // Follow the pivot and highlight it.
            if(i == middle) {
                Highlights.markArray(3, j);
            }
            if(j == middle) {
                Highlights.markArray(3, i);
            }

            Writes.swap(a, i, j, 1, true, false);
            i++;
        }
    }
    
    private void introsortLoop (int[] a, int lo, int hi, int depthLimit) {
        while (hi - lo > sizeThreshold) {
            if (depthLimit == 0) {
                heapSorter.customHeapSort(a, lo, hi, 1);
                return;
            }
            depthLimit--;
            int p = partition(a, lo, hi, medianof3(a, lo, lo + ((hi - lo) / 2) + 1, hi - 1));
            introsortLoop(a, p, hi, depthLimit);
            hi = p;
        }
        return;
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        heapSorter = new MaxHeapSort(this.Delays, this.Highlights, this.Reads, this.Writes);

        introsortLoop(array, 0, length, 2 * floorLogBaseTwo(length));
        Highlights.clearAllMarks();

        InsertionSort sort = new InsertionSort(this.Delays, this.Highlights, this.Reads, this.Writes);
        sort.customInsertSort(array, 0, length, 0.5, false);
    }
}