package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 * 
MIT License

Copyright (c) 2019 w0rthy

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

final public class QsSort extends Sort {
    public QsSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Quickselect");
        this.setRunAllID("Quickselect Sort");
        this.setReportSortID("Quickselect Sort");
        this.setCategory("Exchange Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }
  
 
      
    // partition function similar to quick sort  
    // Considers last element as pivot and adds  
    // elements with less value to the left and  
    // high value to the right and also changes  
    // the pivot position to its respective position 
    // in the final array. 
    public static int partition (int[] arr,  
                                 int low, int high) 
    { 
        int pivot = arr[high], pivotloc = low; 
        for (int i = low; i <= high; i++) 
        { 
            // inserting elements of less value  
            // to the left of the pivot location 
            if(arr[i] < pivot) 
            { 
                int temp = arr[i]; 
                arr[i] = arr[pivotloc]; 
                arr[pivotloc] = temp; 
                pivotloc++; 
            } 
        } 
          
        // swapping pivot to the final pivot location 
        int temp = arr[high]; 
        arr[high] = arr[pivotloc]; 
        arr[pivotloc] = temp; 
          
        return pivotloc; 
    } 
      
    // finds the kth position (of the sorted array)  
    // in a given unsorted array i.e this function  
    // can be used to find both kth largest and  
    // kth smallest element in the array.  
    // ASSUMPTION: all elements in arr[] are distinct 
    public static int kthSmallest(int[] arr, int low,  
                                  int high, int k) 
    { 
        // find the partition  
        int partition = partition(arr,low,high); 
  
        // if partition value is equal to the kth position,  
        // return value at k. 
        if(partition == k) 
            return arr[partition];     
              
        // if partition value is less than kth position, 
        // search right side of the array. 
        else if(partition < k ) 
            return kthSmallest(arr, partition + 1, high, k ); 
              
        // if partition value is more than kth position,  
        // search left side of the array. 
        else
            return kthSmallest(arr, low, partition-1, k );          
    } 

    public void customSort(int[] array, int start, int end) {
        this.partition(array, start, end-1);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.partition(array, 0, length-1);
    }
}