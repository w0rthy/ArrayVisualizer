package array.visualizer;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.Swaps.*;

public class HeapSort {
    
    static int SLP = 1;
    
    static void maxheapifyrec(int pos, boolean max){
        if(pos>=array.length)
            return;
        
        int child1 = pos*2+1;
        int child2 = pos*2+2;
        
        maxheapifyrec(child1,max);
        maxheapifyrec(child2,max);
        
        if(child2>=array.length){
            if(child1>=array.length)
                return; //Done, no children
            if(array[child1]>array[pos])
                swap(array,pos,child1,SLP);
            return;
        }
        
        //Find largest child
        int lrg = child1;
        if(array[child2]>array[child1])
            lrg = child2;
        
        //Swap with largest child
        if(array[lrg]>array[pos]){
            swap(array, pos, lrg,SLP);
            percdwn(lrg, true, array.length);
            return;
        }
    }
    
    static void percdwn(int pos, boolean max, int len){
        int child1 = pos*2+1;
        int child2 = pos*2+2;
        
        if(child2 >= len){
            if(child1 >= len) //Done
                return;
            else{
                //Single Child
                if((max && (array[child1]>array[pos])) || (!max && (array[child1]<array[pos])))
                    swap(array, pos, child1, SLP);
                return;
            }
        }
        
        if(array[child1]>array[child2]){
            //Ensure child1 is the smallest for easy programming
            int tmp = child1;
            child1 = child2;
            child2 = tmp;
        }
        
        if(max && (array[child2]>array[pos])){
            swap(array, pos, child2,SLP);
            percdwn(child2,max,len);
        }
        else if (!max && (array[child1]<array[pos])){
            swap(array, pos, child1,SLP);
            percdwn(child1,max,len);
        }
    }
    
    static void maxheapsort(){
        maxheapifyrec(0,true);
        for(int i = array.length-1; i > 0; i--){
            swap(array, 0, i, SLP);
            percdwn(0,true,i);
        }
    }

    static void minheapsort(){
    }
}
