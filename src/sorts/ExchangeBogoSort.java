package sorts;

import templates.BogoSorting;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

final public class ExchangeBogoSort extends BogoSorting {
    public ExchangeBogoSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Exchange Bogo");
        this.setRunAllID("Exchange Bogo Sort");
        this.setReportSortID("Exchange Bogosort");
        this.setCategory("Exchange Sorts");
        this.isComparisonBased(true); //Comparisons ARE used to swap elements
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.isBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int currentLen, int bucketCount) {
        while(!bogoIsSorted(array, currentLen)){
            int index1 = (int) (Math.random() * currentLen),
                index2 = (int) (Math.random() * currentLen);
            
            Highlights.markArray(1, index1);
            Highlights.markArray(2, index2);
            
            if(index1 < index2) {
                if(Reads.compare(array[index1], array[index2]) == 1){
                    Writes.swap(array, index1, index2, 1, true, false);
                }
            }
            else {
                if(Reads.compare(array[index1], array[index2]) == -1){
                    Writes.swap(array, index1, index2, 1, true, false);
                }
            }
        }
    }
}