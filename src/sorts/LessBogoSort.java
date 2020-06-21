package sorts;

import templates.BogoSorting;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

final public class LessBogoSort extends BogoSorting {
    public LessBogoSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Less Bogo");
        this.setRunAllID("Less Bogo Sort");
        this.setReportSortID("Less Bogosort");
        this.setCategory("Impractical Sorts");
        this.isComparisonBased(true); //Comparisons are the only way the item values are evaluated
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.isBogoSort(true);
    }
    
    @Override
    public void runSort(int[] array, int currentLen, int bucketCount) {
        int iterator = 0;
        
        while(iterator != currentLen) {
            while(!this.isMinSorted(array, currentLen, iterator)) {
                this.bogoSwap(array, currentLen, iterator);
            }
            Highlights.markArray(1, iterator);
            iterator++;
        }
    }
}