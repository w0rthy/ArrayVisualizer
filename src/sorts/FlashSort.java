package sorts;

import static array.visualizer.ArrayVisualizer.compare;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.ArrayVisualizer.sleep;
import static array.visualizer.Writes.swap;
import static array.visualizer.Writes.write;
import static sorts.InsertionSorts.partialInsert;

import java.util.Arrays;

public class FlashSort {

	private static int indexOfIntArray(int[] array, int length, int key) {
	    int returnvalue = -1;
	    for (int i = 0; i < length; ++i) {
	        if (key == array[i]) {
	            returnvalue = i;
	            break;
	        }
	    }
	    return returnvalue;
	}
	
	//The flashsort algorithm is attributed to Karl-Dietrich Neubert
	//The translation to C++ is provided by Clint Jed Casper
	//Refactored in Java by MusicTheorist
	//
	//sorts an array in place in O(n) time using 20% of the
	//memory used by the array for storing intermediate,
	//temporary computations
	public static void flashSort(int[] array, int length)
	{
		if(length == 0) return;

		//20% of the number of elements or 0.2n classes will
		//be used to distribute the input data set into
		//there must be at least 2 classes (hence the addition)
		int m = (int)((0.2 * length) + 2);

		//-------CLASS FORMATION-------

		//O(n)
		//compute the max and min values of the input data
		int min, max, maxIndex;
		min = max = array[0];
		maxIndex = 0;

		for(int i = 1; i < length - 1; i += 2)
		{
			int small;
			int big;
			int bigIndex;

			marked.set(1, i);
			
			//which is bigger A(i) or A(i+1)
			if(compare(array[i], array[i + 1]) == -1)
			{
				small = array[i];
				big = array[i + 1];
				bigIndex = i + 1;
			}
			else
			{
				big = array[i];
				bigIndex = i;
				small = array[i + 1];
			}

			if(big > max)
			{
				max = big;
				maxIndex = bigIndex;
			}

			if(small < min)
			{
				min = small;
			}
			
			sleep(1);
		}

		//do the last element
		marked.set(1, length - 1);
		if(compare(array[length - 1], min) == -1)
		{
			min = array[length - 1];
		}
		else if(compare(array[length - 1], max) == 1)
		{
			max = array[length - 1];
			maxIndex = length - 1;
		}

		sleep(1);
		marked.set(1, -5);
		
		if(max == min)
		{
			//all the elements are the same
			return;
		}

		//dynamically allocate the storage for L
		//note that L is in the range 1...m (hence
		//the extra 1)
		int[] L = new int[m + 1];
		
		//O(m)
		//initialize L to contain all zeros (L[0] is unused)
		for(int t = 1; t <= m; t++)
		{
			write(L, t, 0, 0, false, true);
		}

		//O(n)
		//use the function K(A(i)) = 1 + INT((m-1)(A(i)-Amin)/(Amax-Amin))
		//to classify each A(i) into a number from 1...m
		//(note that this is mainly just a percentage calculation)
		//and then store a count of each distinct class K in L(K)
		//For instance, if there are 22 A(i) values that fall into class
		//K == 5 then the count in L(5) would be 22

		//IMPORTANT: note that the class K == m only has elements equal to Amax

		//precomputed constant
		double c = (m - 1.0) / (max - min);
		int K;
		for(int h = 0; h < length; h++)
		{
			
			marked.set(1, h);
		
			//classify the A(i) value
			K = ((int)((array[h] - min) * c)) + 1;

			assert K > 0;
			assert K <= m;
			//assert: K is in the range 1...m

			//add one to the count for this class
			write(L, K, L[K] + 1, 1, false, true);
		}
		marked.set(1, -5);
		
		//O(m)
		//sum over each L(i) such that each L(i) contains
		//the number of A(i) values that are in the ith
		//class or lower (see counting sort for more details)
		for(K = 2; K <= m; K++)
		{
			write(L, K, L[K] + L[K - 1], 0, false, true);
		}

		//-------PERMUTATION-------

		//swap the max value with the first value in the array
		swap(array, maxIndex, 0, 1, true);
		marked.set(1, -5);
		marked.set(2, -5);
		
		//Except when being iterated upwards,
		//j always points to the first A(i) that starts
		//a new class boundary && that class hasn't yet
		//had all of its elements moved inside its borders;

		//This is called a cycle leader since you know 
		//that you can begin permuting again here. You know
		//this because it is the lowest index of the class
		//and as such A(j) must be out of place or else all
		//the elements of this class have already been placed
		//within the borders of the this class (which means
		//j wouldn't be pointing to this A(i) in the first place)
		int j = 0;
		
		//K is the class of an A(i) value. It is always in the range 1..m
		K = m;

		//the number of elements that have been moved
		//into their correct class
		int numMoves = 0;

		//O(n)
		//permute elements into their correct class; each
		//time the class that j is pointing to fills up
		//then iterate j to the next cycle leader
		//
		//do not use the n - 1 optimization because that last element
		//will not have its count decreased (this causes trouble with
		//determining the correct classSize in the last step)
		while(numMoves < length)
		{
			//if j does not point to the beginning of a class
			//that has at least 1 element still needing to be
			//moved to within the borders of the class then iterate
			//j upward until such a class is found (such a class
			//must exist). In other words, find the next cycle leader
			while(j >= L[K])
			{
				j++;
				//classify the A(j) value
				K = ((int)((array[j] - min) * c)) + 1;
			}

			//evicted always holds the value of an element whose location
			//in the array is free to be written into //aka FLASH
			int evicted = array[j];

			//while j continues to meet the condition that it is
			//pointing to the start of a class that has at least one
			//element still outside its borders (the class isn't full)
			while(j < L[K])
			{
				//compute the class of the evicted value
				K = ((int)((evicted - min) * c)) + 1;

				//get a location that is inside the evicted
				//element's class boundaries
				int location = L[K] - 1;

				//swap the value currently residing at the new
				//location with the evicted value
				int temp = array[location];
				write(array, location, evicted, 1, false, false);
				marked.set(1, location);
				evicted = temp;
				
				//decrease the count for this class
				//see counting sort for why this is done
				write(L, K, L[K] - 1, 0, false, true);

				//another element was moved
				numMoves++;
			}
		}
		marked.set(1, -5);
		
		//-------RECURSION or STRAIGHT INSERTION-------

		//if the classes do not have the A(i) values uniformly distributed
		//into each of them then insertion sort will not produce O(n) results;

		//look for classes that have too many elements; ideally each class
		//(except the topmost or K == m class) should have about n/m elements;
		//look for classes that exceed n/m elements by some threshold AND have
		//more than some minimum number of elements to flashsort recursively

		//if the class has 25% more elements than it should
		int threshold = (int)(1.25 * ((length / m) + 1));
		int minElements = 30;
		
		//for each class decide whether to insertion sort its members
		//or recursively flashsort its members;
		//skip the K == m class because it is already sorted
		//since all of the elements have the same value
		for(K = m - 1; K >= 1; K--)
		{
			//determine the number of elments in the Kth class
			int classSize = L[K + 1] - L[K];

			//if the class size is larger than expected but not
			//so small that insertion sort could make quick work
			//of it then...
			if(classSize > threshold && classSize > minElements)
			{
				//...attempt to flashsort the class. This will work 
				//well if the elements inside the class are uniformly
				//distributed throughout the class otherwise it will 
				//perform badly, O(n^2) worst case, since we will have 
				//performed another classification and permutation step
				//and not succeeded in making the problem significantly
				//smaller for the next level of recursion. However,
				//progress is assured since at each level the elements
				//with the maximum value will get sorted.
				flashSort(Arrays.copyOfRange(array, indexOfIntArray(array, length, L[K]), indexOfIntArray(array, length, L[K+1])), classSize);
			}
		}
		partialInsert(array, 1, length, 0.75);
	}
}