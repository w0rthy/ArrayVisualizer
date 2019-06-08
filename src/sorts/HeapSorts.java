package sorts;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.Writes.*;

/*
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
*/

public class HeapSorts {
    
    static int SLP = 1;
	private static int heapSize;
	
	private static void buildheap(int[] arr, int length, boolean isMax) {
		/*
		 * As last non leaf node will be at (length-1)/2 
		 * so we will start from this location for heapifying the elements
		 * */
		for(int i=(length-1)/2; i>=0; i--){
			heapify(arr,i,length-1,isMax);
		}
	}
	
	private static void buildpartialheap(int[] arr, int lo, int hi) {
		/*
		 * As last non leaf node will be at (length-1)/2 
		 * so we will start from this location for heapifying the elements
		 * */
		for(int i=hi/2; i>=lo; i--){
			heapify(arr,i,hi,true);
		}
	}

	private static void heapify(int[] arr, int i,int size,boolean isMax) { 
		int left = 2*i+1;
		int right = 2*i+2;
		int selected;
		
		if(isMax) {
			if(left <= size && compare(arr[left], arr[i]) == 1){
				selected=left;
			} else {
				selected=i;
			}

			if(right <= size && compare(arr[right], arr[selected]) == 1) {
				selected=right;
			}
			// If max is not current node, exchange it with max of left and right child
			if(selected!=i) {
				swap(arr,i, selected, SLP, true);
				heapify(arr, selected,size,isMax);
			}
		}
		else {
			if(left <= size && compare(arr[left], arr[i]) == -1){
				selected=left;
			} else {
				selected=i;
			}

			if(right <= size && compare(arr[right], arr[selected]) == -1) {
				selected=right;
			}
			// If max is not current node, exchange it with max of left and right child
			if(selected!=i) {
				swap(arr,i,selected,SLP,true);
				heapify(arr,selected,size,isMax);
			}
		}
	}

	public static void heapSort(int[] arr, int length, boolean isMax) {

		buildheap(arr, length, isMax);
		int sizeOfHeap=length-1;
		for(int i=sizeOfHeap; i>0; i--) {
			swap(arr,0, i, 1,true);
			sizeOfHeap--;
			heapify(arr,0,sizeOfHeap,isMax);
		}
		if(!isMax) {
			for(int left = 0, right = length - 1; left < right; left++, right--) {
				swap(arr, left, right, 1, true);
			}
		}
		marked.set(1, -5);
		marked.set(2, -5);
	}
    
    public static void partialHeap(int[] A, int l, int m, int slp){
        SLP = slp;
    	buildpartialheap(A, l, m);
    	SLP = 1;
    }
    
    //SMOOTH SORT - Provided here:
    // https://stackoverflow.com/questions/1390832/how-to-sort-nearly-sorted-array-in-the-fastest-time-possible-java/28352545#28352545
    
     private static final int[] LP = {
    		 1, 1, 3, 5, 9, 15, 25, 41, 67, 109,
    		 177, 287, 465, 753, 1219, 1973, 3193, 5167, 8361, 13529, 21891,
    		 35421, 57313, 92735, 150049, 242785, 392835, 635621, 1028457,
    		 1664079, 2692537, 4356617, 7049155, 11405773, 18454929, 29860703,
    		 48315633, 78176337, 126491971, 204668309, 331160281, 535828591,
    		 866988873 // the next number is > 31 bits.
     };

     private static void sift(int[] A, int pshift, int head)
     {
    	 // we do not use Floyd's improvements to the heapsort sift, because we
    	 // are not doing what heapsort does - always moving nodes from near
    	 // the bottom of the tree to the root.

    	 int val = A[head];

    	 while (pshift > 1)
    	 {
    		 int rt = head - 1;
    		 int lf = head - 1 - LP[pshift - 2];

    		 if (compare(val, A[lf]) >= 0 && compare(val, A[rt]) >= 0)
    			 break;

    		 if (compare(A[lf], A[rt]) >= 0) {
    			 write(A, head, A[lf], 0.65, true, false);
    			 head = lf;
    			 pshift -= 1;
    		 }
    		 else {
    			 write(A, head, A[rt], 0.65, true, false);
    			 head = rt;
    			 pshift -= 2;
    		 }
    	 }
    	 write(A, head, val, 0.65, true, false);
     }

     private static void trinkle(int[] A, int p, int pshift, int head, boolean isTrusty)
     {
    	 int val = A[head];

    	 while (p != 1)
    	 {
    		 int stepson = head - LP[pshift];

    		 if (compare(A[stepson], val) <= 0)
    			 break; // current node is greater than head. sift.

    		 // no need to check this if we know the current node is trusty,
    		 // because we just checked the head (which is val, in the first
    		 // iteration)
    		 if (!isTrusty && pshift > 1) {
    			 int rt = head - 1;
    			 int lf = head - 1 - LP[pshift - 2];
    			 if (compare(A[rt], A[stepson]) >= 0 ||
    					 compare(A[lf], A[stepson]) >= 0)
    				 break;
    		 }
    		 write(A, head, A[stepson], 0.65, true, false);

    		 head = stepson;
    		 //int trail = Integer.numberOfTrailingZeros(p & ~1);
    		 int trail = Integer.numberOfTrailingZeros(p & ~1);
    		 p >>= trail;
    		 pshift += trail;
    		 isTrusty = false;
    	 }

    	 if (!isTrusty) {
    		 write(A, head, val, 0.65, true, false);
    		 sift(A, pshift, head);
    	 }
     }

     private static void sort(int[] A, int lo, int hi)
     {
    	 int head = lo; // the offset of the first element of the prefix into m

    	 // These variables need a little explaining. If our string of heaps
    	 // is of length 38, then the heaps will be of size 25+9+3+1, which are
    	 // Leonardo numbers 6, 4, 2, 1.
    	 // Turning this into a binary number, we get b01010110 = 0x56. We represent
    	 // this number as a pair of numbers by right-shifting all the zeros and
    	 // storing the mantissa and exponent as "p" and "pshift".
    	 // This is handy, because the exponent is the index into L[] giving the
    	 // size of the rightmost heap, and because we can instantly find out if
    	 // the rightmost two heaps are consecutive Leonardo numbers by checking
    	 // (p&3)==3

    	 int p = 1; // the bitmap of the current standard concatenation >> pshift
    	 int pshift = 1;

    	 while (head < hi)
    	 {
    		 if ((p & 3) == 3) {
    			 // Add 1 by merging the first two blocks into a larger one.
    			 // The next Leonardo number is one bigger.
    			 sift(A, pshift, head);
    			 p >>= 2;
    	 		 pshift += 2;
    		 }
    		 else {
    			 // adding a new block of length 1
    			 if (LP[pshift - 1] >= hi - head) {
    				 // this block is its final size.
    				 trinkle(A, p, pshift, head, false);
    			 } else {
    				 // this block will get merged. Just make it trusty.
    				 sift(A, pshift, head);
    			 }

    			 if (pshift == 1) {
    				 // LP[1] is being used, so we add use LP[0]
    				 p <<= 1;
    				 pshift--;
    			 } else {
    				 // shift out to position 1, add LP[1]
    				 p <<= (pshift - 1);
    				 pshift = 1;
    			 }
    		 }
    		 p |= 1;
    		 head++;
    	 }

    	 trinkle(A, p, pshift, head, false);

    	 while (pshift != 1 || p != 1)
    	 {
    		 if (pshift <= 1) {
    			 // block of length 1. No fiddling needed
    			 int trail = Integer.numberOfTrailingZeros(p & ~1);
    			 p >>= trail;
    		     pshift += trail;
    		 }
    		 else {
    			 p <<= 2;
    			 p ^= 7;
    			 pshift -= 2;

    			 // This block gets broken into three bits. The rightmost bit is a
    			 // block of length 1. The left hand part is split into two, a block
    			 // of length LP[pshift+1] and one of LP[pshift].  Both these two
    			 // are appropriately heapified, but the root nodes are not
    			 // necessarily in order. We therefore semitrinkle both of them

    			 trinkle(A, p >> 1, pshift + 1, head - LP[pshift] - 1, true);
    			 trinkle(A, p, pshift, head - 1, true);
    		 }
    		 head--;
    	 }
     }

     public static void smoothSort(int[] A, int length)
     {
    	 sort(A, 0, length - 1);
     }

     //TERNARY HEAP SORT - written by qbit
     // https://codereview.stackexchange.com/questions/63384/binary-heapsort-and-ternary-heapsort-implementation
     
     private static void buildMaxHeapT(int[] array, int length){
    	 heapSize = length - 1;
    	 for(int i = length - 1  / 3; i >= 0; i--)
    		 maxHeapifyT(array, i);

     }

     private static void maxHeapifyT(int[] array, int i){

    	 int leftChild = leftT(i);
    	 int rightChild = rightT(i);
    	 int middleChild = middleT(i);
    	 int largest;

    	 largest = leftChild <= heapSize && compare(array[leftChild], array[i]) > 0 ? leftChild : i;

    	 if(rightChild <= heapSize && compare(array[rightChild], array[largest]) > 0) {
    		 largest = rightChild;
    	 }

    	 if(middleChild <= heapSize && compare(array[middleChild], array[largest]) > 0) {
    		 largest = middleChild;
    	 }


    	 if(largest != i){
    		 swap(array, i, largest, 1, true);
    		 maxHeapifyT(array, largest);
    	 }
     }
     
     private static int leftT(int i){
    	 return 3 * i + 1;
     }

     private static int middleT(int i){
    	 return 3 * i + 2;
     }

     private static int rightT(int i){
    	 return 3 * i + 3;
     }
     
     public static void ternaryHeapSort(int[] array, int length){
    	 buildMaxHeapT(array, length);

    	 for(int i = length - 1; i >= 0; i--){
    		 swap(array, 0, i, 1, true); //add last element on array, i.e heap root

    		 heapSize = heapSize - 1; //shrink heap by 1
    		 maxHeapifyT(array, 0);
    	 }

     }
}