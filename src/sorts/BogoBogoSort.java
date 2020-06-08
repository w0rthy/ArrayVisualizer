package sorts;

import templates.BogoSorting;

public final class BogoBogoSort extends BogoSorting {
    public BogoBogoSort(utils.Delays delayOps, utils.Highlights markOps, utils.Reads readOps, utils.Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Bogobogo");
        this.setRunAllID("Bogobogo Sort");
        this.setReportSortID("Bogobogosort");
        this.setCategory("Distributive Sorts");
        this.isComparisonBased(false); //Comparisons are not used to swap elements
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(true);
        this.setUnreasonableLimit(8);
        this.isBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int bogoLength = 2;
        boolean arrayNotSorted = true;
        
        while(arrayNotSorted) {
            if(bogoIsSorted(array, bogoLength)) {
                if(bogoLength == currentLength) {
                    arrayNotSorted = false;
                }
                else {
                    bogoLength++;
                }
            }
            else {
                bogoLength = 2;
            }
            if(arrayNotSorted) bogoSwap(array, bogoLength, 0);
        }
    }
}