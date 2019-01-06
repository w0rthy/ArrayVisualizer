/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.sort;

import array.visualizer.ArrayVisualizer;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.sort.InsertionSort.*;
import static array.visualizer.utils.Swaps.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author S630690
 */
public class WeaveMerge {
    static void weaveMerge(int min,int max,int mid){
        try {
            //radixLSDsortnd(2, min, max);

            int i=1;
            int target = (mid-min);
            while(i<=target){
                    //swapUpTo(mid+(i-min), min+(i-min)*2, 0.01);
                    swapUpTo(mid+i, min+i*2-1, 0.01);
                    i++;
                    sleep(1);
            }
            insertionSort(min, max+1,0.15);
            //sleep(100);
                    
        } catch (Exception ex) {
            Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static void push(int s,int e){
        
        for(int i=s;i<e;i++){
            if(array[i]>array[i+1]){
                comps++;
                swap(array,i,i+1);
            }
        } 
    }
    
    public static void weaveMergeSort(int min, int max){
	if(max-min==0){//only one element.
		//no swap
	}
	else if(max-min==1){//only two elements and swaps them
            if(array[min]>array[max])
                swap(array,min,max);
	}
        else{
            int mid=((int) Math.floor((min+max)/2));//The midpoint

            weaveMergeSort(min,mid);//sort the left side
            weaveMergeSort(mid+1,max);//sort the right side
            weaveMerge(min,max,mid);//combines them
        }
    }
}
