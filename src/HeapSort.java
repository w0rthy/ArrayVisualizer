package array.visualizer;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.Swaps.*;

public class HeapSort {
    
    static int SLP = 1;
    
    static int heapify(int parent, int child1, boolean max){
        aa+=2;
        comps+=1;
        if((max && array[child1]>array[parent]) || (!max && array[child1]<array[parent])){
            swap(array, parent, child1, SLP);
            return child1;
        }
        
        return -1;
    }
    
    static int heapify(int parent, int child1, int child2, boolean max){
        int candidate = 0;
        
        aa+=4;
        comps+=2;
        
        if(max){
            if(array[child1]>array[child2])
                candidate = child1;
            else
                candidate = child2;
            
            if(array[candidate]>array[parent]){
                swap(array, parent, candidate, SLP);
                return candidate;
            }
        }
        else{
            if(array[child1]<array[child2])
                candidate = child1;
            else
                candidate = child2;
            
            if(array[candidate]<array[parent]){
                swap(array, parent, candidate, SLP);
                return candidate;
            }
        }
        
        return -1;
    }
    
    static void maxheapify(int len) {
        for(int i = len/2-1; i >= 0; i--){
            int child2 = i*2+2;
            if(child2>=len)
                //Special case for only one child.
                heapify(i, child2-1, true);
            else
                heapify(i,child2-1,child2,true);
        }
    }
    
//    static void maxheapify(int len){
//        int loc = 0;
//        int child1 = 1;
//        int child2 = 2;
//        
//        while(loc != -1){
//            child1 = loc*2+1;
//            child2 = child1+1;
//            
//            
//            
//            if(child1>=len)
//                //DONE
//                return;
//            
//            System.out.println("loc: "+loc+" child1: "+child1+" child2: "+child2);
//            System.out.println("loc: "+array[loc]+" child1: "+array[child1]+" child2: "+array[child2]);
//            if(child2>=len)
//                //SPECIAL SINGLE CHILD CASE
//                loc = heapify(loc, child1, true);
//            else
//                loc = heapify(loc, child1, child2, true);
//            
//            System.out.println("Chose "+loc);
//        }
//    }
    
    static void maxheapsort(){
        //maxheapifyinitial(array.length);
        //swap(array, 0, array.length-1, SLP);
        for(int i = array.length; i > 1; i--){
            maxheapify(i);
            swap(array, 0, i-1, SLP);
        }
    }

    static void minheapsort(){
    }
}
