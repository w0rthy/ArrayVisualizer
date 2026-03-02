package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;
 
final public class IterativeMergeSort extends Sort {
    public IterativeMergeSort (Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Iterative Merge");
        this.setRunAllID("Iterative Merge Sort");
        this.setReportSortID("Iterative Mergesort");
        this.setCategory("Merge Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
}
    
   
        /* Iterative mergesort function to sor 
    t arr[0...n-1] */
    private void mergeSort(int arr[], int n, double sleep) 
    { 
          
        // For current size of subarrays to 
        // be merged curr_size varies from  
        // 1 to n/2 
        int curr_size;  
                      
        // For picking starting index of  
        // left subarray to be merged 
        int left_start; 
                          
          
        // Merge subarrays in bottom up  
        // manner. First merge subarrays  
        // of size 1 to create sorted  
        // subarrays of size 2, then merge 
        // subarrays of size 2 to create  
        // sorted subarrays of size 4, and 
        // so on. 
        for (curr_size = 1; curr_size <= n-1;  
                      curr_size = 2*curr_size) 
        { 
              
            // Pick starting point of different 
            // subarrays of current size 
            for (left_start = 0; left_start < n-1; 
                        left_start += 2*curr_size) 
            { 
                // Find ending point of left  
                // subarray. mid+1 is starting  
                // point of right 
                int mid = Math.min(left_start + curr_size - 1, n-1); 
          
                int right_end = Math.min(left_start  
                             + 2*curr_size - 1, n-1); 
          
                // Merge Subarrays arr[left_start...mid] 
                // & arr[mid+1...right_end] 
                merge(arr, left_start, mid, right_end, sleep); 
            } 
        } 
    } 
      
    /* Function to merge the two haves arr[l..m] and 
    arr[m+1..r] of array arr[] */
    private void merge(int arr[], int l, int m, int r, double sleep) 
    { 
        int i, j, k; 
        int n1 = m - l + 1; 
        int n2 = r - m; 
      
        /* create temp arrays */
        int L[] = new int[n1]; 
        int R[] = new int[n2]; 
      
        /* Copy data to temp arrays L[] 
        and R[] */
        for (i = 0; i < n1; i++){
            Highlights.markArray(1, l+i);
            Writes.write(L, i, arr[l+i], sleep, false, true);
        }
        for (j = 0; j < n2; j++){
            Highlights.markArray(1, m+1+j);
            Writes.write(R, j, arr[m+1+j], sleep, false, true);
        }
        Highlights.clearAllMarks();
        /* Merge the temp arrays back into 
        arr[l..r]*/
        i = 0; 
        j = 0; 
        k = l; 
        while (i < n1 && j < n2) 
        { 
            Delays.sleep(sleep);
            if (Reads.compare(L[i], R[j]) <= 0) 
            { 
                Writes.write(arr, k, L[i], sleep, true, false);
                i++; 
            } 
            else
            { 
                Writes.write(arr, k, R[j], sleep, true, false);
                j++; 
            } 
            k++; 
        } 
        Highlights.clearAllMarks();
        /* Copy the remaining elements of  
        L[], if there are any */
        while (i < n1) 
        { 
            Writes.write(arr, k, L[i], sleep, true, false);
            i++; 
            k++; 
        } 
      
        /* Copy the remaining elements of 
        R[], if there are any */
        while (j < n2) 
        { 
            Writes.write(arr, k, R[j], sleep, true, false);
            j++; 
            k++; 
        } 
    } 

    public void customSort(int[] array, int start, int end) {
        this.mergeSort(array, end, 1);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.mergeSort(array, length, 1);
    }
}