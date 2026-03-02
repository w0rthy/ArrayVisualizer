package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

final public class TernaryHeapSort extends Sort {
    public TernaryHeapSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Ternary Heap");
        this.setRunAllID("Ternary Heap Sort");
        this.setReportSortID("Ternary Heapsort");
        this.setCategory("Selection Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }
    
    // TERNARY HEAP SORT - written by qbit
    // https://codereview.stackexchange.com/questions/63384/binary-heapsort-and-ternary-heapsort-implementation
    
    private int heapSize;
    
    private static int leftBranch(int i) {
        return 3 * i + 1;
    }

    private static int middleBranch(int i) {
        return 3 * i + 2;
    }

    private static int rightBranch(int i) {
        return 3 * i + 3;
    }

    private void maxHeapify(int[] array, int i) {

        int leftChild = TernaryHeapSort.leftBranch(i);
        int rightChild = TernaryHeapSort.rightBranch(i);
        int middleChild = TernaryHeapSort.middleBranch(i);
        int largest;

        largest = leftChild <= heapSize && Reads.compare(array[leftChild], array[i]) > 0 ? leftChild : i;

        if(rightChild <= heapSize && Reads.compare(array[rightChild], array[largest]) > 0) {
            largest = rightChild;
        }

        if(middleChild <= heapSize && Reads.compare(array[middleChild], array[largest]) > 0) {
            largest = middleChild;
        }


        if(largest != i) {
            Writes.swap(array, i, largest, 1, true, false);
            this.maxHeapify(array, largest);
        }
    }
    
    private void buildMaxTernaryHeap(int[] array, int length) {
        heapSize = length - 1;
        for(int i = length - 1  / 3; i >= 0; i--)
            this.maxHeapify(array, i);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.buildMaxTernaryHeap(array, length);

        for(int i = length - 1; i >= 0; i--){
            Writes.swap(array, 0, i, 1, true, false); //add last element on array, i.e heap root

            heapSize = heapSize - 1; //shrink heap by 1
            this.maxHeapify(array, 0);
        }
    }
}