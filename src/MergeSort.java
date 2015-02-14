/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.Swaps.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author S630690
 */
public class MergeSort {
    static void merge(int min,int max,int mid){
        try {
            //radixLSDsortnd(2, min, max);
                
            
                    int i=min;
                    while(i<=mid){
                            if(array[i]>array[mid+1]){
                                    comps++;
                                    swap(array,i,mid+1,sleepTime(0.005));
                                    push(mid+1,max);
                            }			
                            i++;
                    }		
                    
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
    
    static void mergeSort(int min,int max){
	if(max-min==0){//only one element.
		//no swap
	}
	else if(max-min==1){//only two elements and swaps them
            if(array[min]>array[max])
                swap(array,min,max);
	}
        else{
            int mid=((int) Math.floor((min+max)/2));//The midpoint

            mergeSort(min,mid);//sort the left side
            mergeSort(mid+1,max);//sort the right side
            merge(min,max,mid);//combines them
        }
    }
}
