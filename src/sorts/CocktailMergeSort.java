package sorts;

import templates.Sort;
import templates.TimSorting;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

// Inspired by Sorting Stuff's "Obscure Sorting Algorithms": https://www.youtube.com/watch?v=fWubJgIWyxQ

// Basically, "Cocktail Merge Sort" is a hybrid between Cocktail Shaker Sort and TimSort. It starts by building
// runs of TimSort's minimum length using Cocktail Shaker, then merges all these runs using TimSort. This 
// effectively replaces Binary Insertion Sort, used for building runs in TimSort. Big-O analysis would still say
// this is constant time, as the minrun value is not dependent on the number of elements we are sorting, but
// Cocktail Shaker has worse constant factors than Insertion Sort. So basically, this is just for fun.
// But hey, why not? ;)

public class CocktailMergeSort extends Sort {
    private TimSorting timSortInstance; // TimSort cannot be simply written off as an abstract class, as it creates an instance of itself
                                        // in order to track its state. Plus, it contains both instance and static methods, requiring even
                                        // more refactoring, which would be just doing unnecessary busy work. Instead of what we've done for
                                        // the rest of the algorithms, we'll favor composition over inheritance here and pass "util" objects
                                        // to it.
    
    public CocktailMergeSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Cocktail Merge");
        this.setRunAllID("Cocktail Merge Sort");
        this.setReportSortID("Cocktail Mergesort");
        this.setCategory("Hybrid Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        CocktailShakerSort cocktailShaker = new CocktailShakerSort(Delays, Highlights, Reads, Writes);
        int minRunLen = TimSorting.minRunLength(currentLength);
        
        if (currentLength == minRunLen) {
            cocktailShaker.runSort(array, currentLength, bucketCount);
        }
        else {
            int i = 0;
            for (; i <= (currentLength - minRunLen); i += minRunLen) {
                cocktailShaker.customSort(array, i, i + minRunLen);
            }
            if (i + minRunLen > currentLength) {
                cocktailShaker.customSort(array, i, currentLength);
            }
            
            Highlights.clearAllMarks();
            
            this.timSortInstance = new TimSorting(array, currentLength, this.Highlights, this.Reads, this.Writes);
            TimSorting.sort(this.timSortInstance, array, currentLength);
        }
    }
}