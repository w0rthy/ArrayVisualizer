package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 * <b>IDeserve <br>
 * <a href="<a class="vglnk" href="https://www.youtube.com/c/IDeserve" rel="nofollow"><span>https</span><span>://</span><span>www</span><span>.</span><span>youtube</span><span>.</span><span>com</span><span>/</span><span>c</span><span>/</span><span>IDeserve</span></a>"><a class="vglnk" href="https://www.youtube.com/c/IDeserve" rel="nofollow"><span>https</span><span>://</span><span>www</span><span>.</span><span>youtube</span><span>.</span><span>com</span><span>/</span><span>c</span><span>/</span><span>IDeserve</span></a></a>
 * Given an array, sort the array using Pancake sort.
 * 
 * @author Saurabh
 * https://www.ideserve.co.in/learn/pancake-sorting
 */

final public class PancakeSort extends Sort {
    public PancakeSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Pancake");
        this.setRunAllID("Pancake Sort");
        this.setReportSortID("Pancake Sorting");
        this.setCategory("Miscellaneous Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }
    
    private boolean sorted(int[] array, int length) {
        for(int i = 0; i < length - 1; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.025);
            
            if(Reads.compare(array[i], array[i + 1]) > 0) return false;
        }
        return true;
    }
    
    private int findMax(int[] arr, int end) {
        int index = 0, max = Integer.MIN_VALUE;
        for (int i = 0; i <= end; i++) {
            Highlights.markArray(1, i);
            
            if (Reads.compare(arr[i], max) == 1) {
                max = arr[i];
                index = i;
            }
            
            Delays.sleep(0.025);
            Highlights.clearMark(1);
        }
        return index;
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int i = length - 1; i >= 0; i--) {
            if(!this.sorted(array, i)) {
                if(i + 1 != length) Highlights.markArray(3, i + 1);
                int index = this.findMax(array, i);

                if(index == 0) {
                    Writes.reversal(array, 0, i, 0.05, true, false);
                }
                else if(index != i) {
                    Writes.reversal(array, 0, index, 0.05, true, false);
                    Writes.reversal(array, 0, i, 0.05, true, false);
                }
            }
            else break;
        }
    }
}