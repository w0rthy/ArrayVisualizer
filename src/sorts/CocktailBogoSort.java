package sorts;

import templates.BogoSorting;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

final public class CocktailBogoSort extends BogoSorting {
    public CocktailBogoSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Cocktail Bogo");
        this.setRunAllID("Cocktail Bogo Sort");
        this.setReportSortID("Cocktail Bogosort");
        this.setCategory("Distributive Sorts");
        this.isComparisonBased(false); //Comparisons are not used to swap elements
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.isBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int currentLen, int bucketCount) {
        int minIterator = 0;
        int maxIterator = currentLen - 1;
        
        while(minIterator < maxIterator) {
            boolean maxSorted = this.isMaxSorted(array, minIterator, maxIterator);
            boolean minSorted = this.isMinSorted(array, maxIterator + 1, minIterator);
            
            while(!maxSorted && !minSorted) {
                this.bogoSwap(array, maxIterator + 1, minIterator);
                
                maxSorted = this.isMaxSorted(array, minIterator, maxIterator);
                minSorted = this.isMinSorted(array, maxIterator + 1, minIterator);
            }
            
            if(minSorted) {
                Highlights.markArray(1, minIterator);
                minIterator++;
                minSorted = false;
            }
            if(maxSorted) {
                Highlights.markArray(2, maxIterator);
                maxIterator--;
                maxSorted = false;
            }
        }
    }
}