package QS;

public class QuadtraticSort
{
	// array of random numbers
	public static int[] doRandomArray(int[]myArray) {
		for (int i = 0; i < myArray.length; i++) {
			myArray[i] = (int) (Math.random() * 100);
			System.out.println(myArray[i] + " ");
		}
		return myArray;
	}

	// array of sorted numbers ascending
		 public static int[] doSortedArray(int [] myArray) {
			 int temp=0;
				for (int i = 0; i < myArray.length-1; i++) {
					for (int j = 0; j < myArray.length-1; j++) {
						if (myArray[j] > myArray[j + 1]) {
							temp = myArray[j];
							myArray[j] = myArray[j + 1];
							myArray[j + 1] = temp;
							
						}
					}
					
				}
				System.out.println("Sorted Array:");
				for (int i = 0; i < myArray.length; i++)
			      {
			         System.out.println(+myArray[i] +" ");
			      } 
				return myArray;
	
	}
		
	public static int[] doInverselySortedArray (int[] myArray){
		int temp=0;
		for (int i = 0; i < myArray.length-1; i++) {
			for (int j = 0; j < myArray.length-1; j++) {
				if (myArray[j] < myArray[j + 1]) {
					temp = myArray[j];
					myArray[j] = myArray[j + 1];
					myArray[j + 1] = temp;
					
				}
			}
			
		}
		System.out.println("Inversely Sorted Array:");
		for (int i = 0; i < myArray.length; i++)
	      {
	         System.out.println(+myArray[i] +" ");
	      } 
		return myArray;
	}
	
	public static int[] doTempArray (int[] myArray){
		int[] tempArray = new int[myArray.length];
		for (int j = 0; j < myArray.length; j++) {
				tempArray[j] = myArray[j];
		}
		return tempArray;
	}
	
	// bubblesort
	public static void doBubbleSort(int[] myArray) {
		int temp = 0;
		long swaps=0;
		long comparisons=0;
		for (int i = 0; i < myArray.length-1; i++) {
			
			for (int j = 0; j < myArray.length-1; j++) {
				comparisons++;
				if (myArray[j] > myArray[j + 1]) {
					//swaps++;
					temp = myArray[j];
					myArray[j] = myArray[j + 1];
					myArray[j + 1] = temp;
					swaps++;
				}
			}
		}
		
		for (int i = 0; i < myArray.length; i++)
	      {
	         System.out.print(+myArray[i] +" ");
	      } 
		System.out.println("\nComparisons done: " +comparisons);
		System.out.println("\nSwaps done: " +swaps);
	}
	
	
	// enhanced bubble sort
	public static void doEnhancedBubbleSort(int[] myArray) {

		int temp = 0;
		long swaps=0;
		long comparisons=0;
		Boolean sorted = false;
		for (int i = 0; i < myArray.length - 1 && sorted==false; i++) {
			sorted=true;
			for (int j = 0; j < myArray.length -1-i; j++) {
				comparisons++;
				if (myArray[j] > myArray[j + 1]) {
					temp = myArray[j];
					myArray[j] = myArray[j + 1];
					myArray[j + 1] = temp;
					swaps++;
					sorted = false;
					
				}
			}
			//if (sorted)
			//	break;
		}
		for (int i = 0; i < myArray.length; i++)
	      {
	         System.out.print(+myArray[i] +" ");
	      } 
		System.out.println("\nComparisons done: " +comparisons);
		System.out.println("\nSwaps done: " +swaps);
		
	}
	
	public static int[] doSelectionSort(int[] arr){
        long comparisons=0;
        long swaps=0;
	    for (int i = 0; i < arr.length - 1; i++)	  // start at the beginning of the whole array
		{
		int minimum = i;	// (1) default value of the 1st element index � use this to test against every other element.
		for(int j = i +1; j < arr.length; j++)	  // (2) loop from the beginning of unsorted zone to the end of the array.
		{	
			comparisons++;
		if(arr [j] < arr[minimum])	// compare current element to minimum
		 minimum = j;	// if it's lower, it becomes the new minimum
		}
		
		// swap the two values

		int temp = arr [i]; 
		swaps++;
		arr [i] = arr[minimum];
		arr [minimum] = temp;
		}
	    for(int i:arr){
            System.out.print(i +" ");
		}
	    System.out.println("\nComparisons done: " +comparisons);
		System.out.println("\nSwaps done: " +swaps);
	        return arr;
	    }

	
	// insertion sort
	public static int[] doInsertionSort(int[] list) {
		long swaps=0;
		long comparisons=1;
		for (int i = 1; i < list.length; i++) {
			int next = list[i];
			// find the insertion location while moving all larger element up
			int j = i;
			while (j > 0 && list[j - 1] > next) {
				list[j] = list[j - 1];
				j--;
				swaps++;
				comparisons++;
			}
			// insert the element
			list[j] = next;
			
		}
		for(int i:list){
            System.out.print(i +" ");
		}
		System.out.println("\nComparisons done: " +comparisons);
		System.out.println("\nSwaps done: " +swaps);
		return list;
	}

}
