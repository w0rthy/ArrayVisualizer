package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 * This example of an O(n^3) sorting algorithm may be found here, written by James Jensen (StriplingWarrayior on StackOverflow):
 * https://stackoverflow.com/questions/27389344/is-there-a-sorting-algorithm-with-a-worst-case-time-complexity-of-n3
 */
 
final public class BadSort extends Sort {
    public BadSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Bad");
        this.setRunAllID("Bad Sort");
        this.setReportSortID("Badsort");
        this.setCategory("Selection Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(true);
        this.setUnreasonableLimit(4096);
        this.isBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLen, int bucketCount) {
        for (int i = 0; i < currentLen; i++) {
            int shortest = i;
            Delays.sleep(0.05);
            
            for (int j = i; j < currentLen; j++) {
                Highlights.markArray(1, j);
                Delays.sleep(0.05);
                
                boolean isShortest = true;
                for (int k = j + 1; k < currentLen; k++) {
                    Highlights.markArray(2, k);
                    Delays.sleep(0.05);
                    
                    if (Reads.compare(array[j], array[k]) == 1) {
                        isShortest = false;
                        break;
                    }
                }
                if(isShortest) {
                    shortest = j;
                    break;
                }
            }
            Writes.swap(array, i, shortest, 0.05, true, false);
        }
    }
}